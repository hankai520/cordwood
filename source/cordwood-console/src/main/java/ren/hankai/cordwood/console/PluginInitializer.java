
package ren.hankai.cordwood.console;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginEventEmitter;
import ren.hankai.cordwood.plugin.api.PluginEventListener;
import ren.hankai.cordwood.plugin.api.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

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
  private PluginManager pluginManager;

  @Autowired
  private PluginEventEmitter pluginEventEmmitter;

  @Autowired
  private PluginService pluginService;

  private static boolean suspended = false;

  @PostConstruct
  private void setupLoggersForPlugins() throws Exception {
    // 注册插件事件监听
    pluginEventEmmitter.addListener(PluginEventEmitter.PLUGIN_LOADED, new PluginEventListener() {

      @Override
      public void handleEvent(Plugin plugin, String eventType) {
        final String clazz = plugin.getInstanceClass().getName();
        LogbackUtil.setupFileLoggerFor(clazz, Level.INFO, plugin.getName() + ".txt");
      }
    });
  }

  /**
   * 安装插件包文件。
   *
   * @param file 文件
   * @author hankai
   * @since Oct 21, 2016 4:25:06 PM
   */
  private void installPlugin(File file) {
    try {
      pluginService.installPluginPackage(file.toURI().toURL(), false);
    } catch (final Exception ex) {
      logger.error("Failed to install plugin: " + file.getPath(), ex);
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
        installPlugin(pluginFile);
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
    if (suspended) {
      return;
    }
    final List<PluginPackageBean> list = pluginService.getInstalledPackages(null);
    if (list != null) {
      final List<PluginPackage> packages = new ArrayList<>();
      for (final PluginPackageBean ppb : list) {
        final PluginPackage pp = new PluginPackage(ppb.getInstallationUrl());
        packages.add(pp);
      }
      pluginManager.initializePlugins(packages);
    }
    installCopiedPlugins();
    final Iterator<Plugin> it = pluginManager.getPluginIterator();
    while (it.hasNext()) {
      final Plugin plugin = it.next();
      final PluginBean pb = pluginService.getInstalledPluginByName(plugin.getName());
      plugin.setActive(pb.isActive());
    }
  }

  /**
   * 暂停插件初始化和插件目录观察器。
   *
   * @author hankai
   * @since Dec 8, 2016 6:05:52 PM
   */
  public void suspend() {
    suspended = true;
  }

  /**
   * 恢复件初始化和插件目录观察器。
   *
   * @author hankai
   * @since Dec 8, 2016 6:06:34 PM
   */
  public void resume() {
    suspended = false;
  }
}
