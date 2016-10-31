
package ren.hankai.cordwood.console;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.persist.util.EntitySpecs;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.api.PluginEventEmitter;
import ren.hankai.cordwood.plugin.api.PluginEventListener;
import ren.hankai.cordwood.plugin.api.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 插件初始化器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 11:31:14 AM
 */
@Component
public class PluginInitializer {

  private static final Logger logger = LoggerFactory.getLogger(PluginInitializer.class);

  @Autowired
  private PluginPackageRepository pluginPackageRepository;

  @Autowired
  private PluginManager pluginManager;

  @Autowired
  private PluginEventEmitter pluginEventEmmitter;

  @Autowired
  private PluginRegistry pluginRegistry;

  private PluginWatcher pluginWatcher;

  @Autowired
  private PluginService pluginService;

  @PostConstruct
  private void setupLoggersForPlugins() throws Exception {
    // 注册插件事件监听
    pluginEventEmmitter.addListener(PluginEventEmitter.PLUGIN_LOADED, new PluginEventListener() {

      @Override
      public void handleEvent(Plugin plugin, String eventType) {
        final String clazz = plugin.getInstance().getClass().getName();
        LogbackUtil.setupFileLoggerFor(clazz, Level.WARN, plugin.getName() + ".txt");
      }
    });
    // 启用插件目录监视器
    pluginWatcher = new PluginWatcher();
    pluginWatcher.watch();
    // 加载依赖包
  }

  @PreDestroy
  private void destory() {
    pluginWatcher.stopWatching();
  }

  /**
   * 获取文件 SHA1 校验和。
   *
   * @param file 文件
   * @return 校验和(16进制字符串)
   * @author hankai
   * @since Oct 21, 2016 4:25:28 PM
   */
  private String getChecksum(File file) {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      return DigestUtils.sha1Hex(fis);
    } catch (final Exception e) {
      logger.error(
          String.format("Failed to calculate checksum of file \"%s\"", file.getAbsolutePath()));
    } finally {
      IOUtils.closeQuietly(fis);
    }
    return null;
  }

  /**
   * 安装插件包文件。
   *
   * @param file 文件
   * @param checksum SHA1 校验和
   * @author hankai
   * @since Oct 21, 2016 4:25:06 PM
   */
  private void installPlugin(File file, String checksum) {
    try {
      final PluginPackageBean ppb =
          pluginPackageRepository.findOne(EntitySpecs.field("checksum", checksum));
      if (ppb == null) {
        // TODO: 在安装插件时，用插件的文件名作了补充判断，去更新数据库中已有的插件信息，这是否会有潜在风险？
        final PluginPackageBean possiblePpb =
            pluginPackageRepository.findOne(EntitySpecs.field("fileName", file.getName()));
        if (possiblePpb != null) {
          pluginPackageRepository.delete(possiblePpb.getId());
        }

        pluginService.installPlugin(file.toURI().toURL());
      } else if (!file.getName().equals(ppb.getFileName())) {
        logger.info(String.format(
            "Found duplicate plugin package: \"%s\" and \"%s\" which have same checksum \"%s\"!",
            file.getName(), ppb.getFileName(), checksum));
      }
    } catch (final Exception e) {
      logger.error("Failed to install plugin: " + file.getPath(), e);
    }
  }

  /**
   * 在内存中注销插件，然后在数据库中删除插件信息。注意，本方法不会删除插件包物理文件
   *
   * @param fileName 插件包文件名
   * @author hankai
   * @since Oct 21, 2016 4:24:02 PM
   */
  private void uninstallPlugin(String fileName) {
    final PluginPackageBean ppb =
        pluginPackageRepository.findOne(EntitySpecs.field("fileName", fileName));
    if ((ppb != null) && pluginRegistry.isPackageRegistered(ppb.getChecksum())) {
      pluginRegistry.unregisterPackage(ppb.getChecksum());
      pluginPackageRepository.delete(ppb.getId());
    }
  }

  /**
   * 注册尚未注册的物理插件包文件，通常用来安装通过文件复制而不是控制台途径安装的插件。
   *
   * @author hankai
   * @since Oct 14, 2016 11:53:38 AM
   */
  private void installCopiedPlugins() {
    final File file = new File(Preferences.getPluginsDir());
    File[] files = null;
    if (file.exists() && file.isDirectory()) {
      files = file.listFiles();
    }
    if ((files != null) && (files.length > 0)) {
      for (final File pluginFile : files) {
        if (!FilenameUtils.isExtension(pluginFile.getName(), "jar")) {
          continue;
        }
        installPlugin(pluginFile, getChecksum(pluginFile));
      }
    }
  }

  /**
   * 读取在数据库中注册的插件信息，初始化已安装的插件。
   *
   * @author hankai
   * @since Oct 14, 2016 2:06:35 PM
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 2)
  public void initializeInstalledPlugins() {
    final List<PluginPackageBean> list = pluginPackageRepository.findAll();
    if (list != null) {
      final List<String> names = new ArrayList<>();
      for (final PluginPackageBean ppb : list) {
        names.add(ppb.getFileName());
      }
      pluginManager.initializePlugins(names);
    }
    installCopiedPlugins();
  }

  /**
   * 插件目录观察器，监视插件目录的文件变化。
   *
   * @author hankai
   * @version 1.0.0
   * @since Oct 14, 2016 4:20:38 PM
   */
  private class PluginWatcher extends Thread {

    private final Logger logger = LoggerFactory.getLogger(PluginWatcher.class);

    private boolean shouldStop;

    private boolean watching;

    private WatchService watchService;

    public PluginWatcher() {
      shouldStop = false;
      watching = false;
      setName("Plugin home watcher");
      try {
        watchService = FileSystems.getDefault().newWatchService();
        final Path pluginFolder = Paths.get(Preferences.getPluginsDir());
        pluginFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
      } catch (final IOException e) {
        logger.warn("Failed to start plugin home watcher!", e);
      }
    }

    public void watch() {
      if (watching) {
        return;
      }
      shouldStop = false;
      start();
      watching = true;
    }

    public void stopWatching() {
      if (watching) {
        shouldStop = true;
      }
    }

    private void handleCreation(String path) {
      final File file = new File(path);
      if (file.exists() && !file.isDirectory()) {
        logger.info("Detected new plugin package: " + file.getName());
        final String checksum = getChecksum(file);
        installPlugin(file, checksum);
      }
    }

    private void handleDeletion(String path) {
      final File file = new File(path);
      logger.info("Detected deletion of plugin package: " + file.getName());
      if (!file.exists()) {
        final String fileName = FilenameUtils.getName(path);
        final PluginPackageBean ppb =
            pluginPackageRepository.findOne(EntitySpecs.field("fileName", fileName));
        if (ppb != null) {
          uninstallPlugin(ppb.getFileName());
        }
      }
    }

    private void handleModification(String path) {
      final File file = new File(path);
      logger.info("Detected modification of plugin package: " + file.getName());
      if (file.exists()) {
        uninstallPlugin(file.getName());
        final String checksum = getChecksum(file);
        installPlugin(file, checksum);
      }
    }

    @Override
    public void run() {
      while (!shouldStop) {
        try {
          boolean valid = true;
          final String pluginHome = Preferences.getPluginsDir();
          do {
            final WatchKey watchKey = watchService.take();
            final Map<String, Kind<?>> changes = new HashMap<>();
            for (final WatchEvent<?> event : watchKey.pollEvents()) {
              final String fileName = event.context().toString();
              if (!FilenameUtils.isExtension(fileName, "jar")) {
                continue;
              }
              final Kind<?> kind = event.kind();
              changes.put(fileName, kind);
            }
            for (final String fileName : changes.keySet()) {
              final String path = pluginHome + File.separator + fileName;
              final Kind<?> kind = changes.get(fileName);
              if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                handleCreation(path);
              } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                handleDeletion(path);
              } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                handleModification(path);
              }
            }
            valid = watchKey.reset();
          } while (valid);
        } catch (final Exception e) {
          logger.error("Plugin home watcher error!", e);
        }
      }
      watching = false;
    }
  }
}
