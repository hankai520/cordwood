
package ren.hankai.cordwood.plugin.impl;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.plugin.PluginEventEmitter;
import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.PluginRegistry;
import ren.hankai.cordwood.plugin.PluginValidator;
import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认插件管理器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 6:01:21 PM
 */
@Component
public class DefaultPluginManager implements PluginManager, PluginRegistry {

  private static final Logger logger = LoggerFactory.getLogger(DefaultPluginManager.class);
  @Autowired
  private PluginLoader pluginLoader;
  @Autowired
  private PluginValidator pluginValidator;
  @Autowired
  private PluginEventEmitter pluginEventEmitter;
  // 插件名作键，插件包装对象作值
  private final Map<String, Plugin> plugins = new HashMap<>();
  // 插件包SHA1校验和作键，插件包包装对象作值
  private final Map<String, PluginPackage> packages = new HashMap<>();

  /**
   * 启用或禁用插件。
   *
   * @param pluginName 插件名
   * @param active 是否启用
   * @return 是否成功
   * @author hankai
   * @since Oct 13, 2016 1:08:57 PM
   */
  private boolean changeActivationStatus(String pluginName, boolean active) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin != null) {
      plugin.setActive(active);
      plugins.put(plugin.getName(), plugin);
      if (active) {
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_ACTIVATED, plugin);
      } else {
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_DEACTIVATED, plugin);
      }
      return true;
    } else {
      logger.warn(String.format("Attempting to %s nonexistent plugin: \"%s\"",
          (active ? "activate" : "deactivate"), pluginName));
    }
    return false;
  }

  /**
   * 将底层插件示例包装为插件模型对象。
   *
   * @param pluginInstance 插件实例
   * @return 插件模型
   * @author hankai
   * @since Oct 13, 2016 1:09:23 PM
   */
  private Plugin wrapPlugin(Object pluginInstance) {
    final Class<?> clazz = pluginInstance.getClass();
    Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
    final Plugin plugin = new Plugin();
    plugin.setName(pluggable.name());
    plugin.setVersion(pluggable.version());
    plugin.setDescription(pluggable.description());
    plugin.setInstance(pluginInstance);
    plugin.setActive(true);
    // 扫描插件标记的功能
    ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {

      @Override
      public void doWith(final Method method)
          throws IllegalArgumentException, IllegalAccessException {
        Functional functional = AnnotationUtils.getAnnotation(method, Functional.class);
        if (functional != null) {
          PluginFunction function = new PluginFunction();
          function.setMethod(method);
          if (!StringUtils.isEmpty(functional.name())) {
            function.setName(functional.name());
          } else {
            function.setName(method.getName());
          }
          function.setResultType(functional.resultType());
          plugin.getFunctions().put(functional.name(), function);
        }
      }
    }, new ReflectionUtils.MethodFilter() {

      @Override
      public boolean matches(final Method method) {
        return (method.getDeclaringClass() == clazz);
      }
    });
    return plugin;
  }

  /**
   * 从插件包文件中抽取插件包元数据。
   *
   * @param localPath 插件包本地路径
   * @return 插件包模型
   * @author hankai
   * @since Oct 13, 2016 1:09:47 PM
   */
  private PluginPackage extractPackageInfo(URL localPath) {
    InputStream is = null;
    try {
      PluginPackage pluginPackage = new PluginPackage();
      pluginPackage.setFileName(FilenameUtils.getName(localPath.getPath()));
      pluginPackage.setInstallUrl(localPath);
      is = localPath.openStream();
      pluginPackage.setIdentifier(DigestUtils.sha1Hex(is));
      return pluginPackage;
    } catch (IOException e) {
      logger.error(String.format("Failed to calculate the checksum of package \"%s\"", localPath),
          e);
    } finally {
      IOUtils.closeQuietly(is);
    }
    return null;
  }

  /**
   * 将临时插件包文件复制到插件包安装路径，如果插件包已安装过了，则不会覆盖。
   *
   * @param tempUrl 临时插件包路径
   * @return 安装后的本地插件包路径
   * @author hankai
   * @since Oct 13, 2016 1:59:07 PM
   */
  private URL copyPackage(URL tempUrl) {
    URL url = null;
    String protocal = tempUrl.getProtocol().toLowerCase();
    if (protocal.equals("file")) {
      String name = FilenameUtils.getName(tempUrl.getPath());
      String localPath = Preferences.getPluginsDir() + File.separator + name;
      InputStream is = null;
      OutputStream os = null;
      try {
        File localFile = new File(localPath);
        if (!localFile.exists() || localFile.isDirectory()) {
          is = tempUrl.openStream();
          os = new FileOutputStream(localFile);
          FileCopyUtils.copy(is, os);
        }
        url = localFile.toURI().toURL();
      } catch (MalformedURLException e) {
        logger.error(
            String.format("Failed to convert package url to local: %s", tempUrl.toString()), e);
      } catch (IOException e) {
        logger.error(String.format("Failed to copy plugin package file: %s", tempUrl.toString()),
            e);
      } finally {
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
      }
    }
    return url;
  }

  @Override
  public synchronized PluginPackage register(URL packageUrl) {
    if (!pluginValidator.validatePackage(packageUrl)) {
      logger.error(String.format("Failed to verify plugin package at %s", packageUrl.toString()));
    } else {
      URL localPath = copyPackage(packageUrl);
      if (localPath != null) {
        PluginPackage pack = extractPackageInfo(localPath);
        PluginPackage loadedPack = packages.get(pack.getIdentifier());
        if (loadedPack != null) {
          unregister(loadedPack.getIdentifier());
        }
        String name = FilenameUtils.getName(localPath.getPath());
        List<Object> instances = pluginLoader.loadPlugins(localPath);
        if ((instances != null) && !instances.isEmpty()) {
          for (Object instance : instances) {
            Plugin plugin = wrapPlugin(instance);
            pack.addPlugin(plugin);
            plugins.put(plugin.getName(), plugin);
            pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
          }
          packages.put(pack.getIdentifier(), pack);
          logger.info(String.format("Loaded plugin package %s !", name));
          return pack;
        } else {
          logger.error(String.format("No plugin definitions found in package \"%s\" !", name));
        }
      }
    }
    return null;
  }

  @Override
  public synchronized boolean unregister(String packageId) {
    PluginPackage pp = packages.get(packageId);
    if (pp != null) {
      for (Plugin p : pp.getPlugins()) {
        pluginLoader.unloadPlugin(p.getInstance());
        plugins.remove(p.getName());
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_UNLOADED, p);
      }
      packages.remove(packageId);
      logger.info(String.format("Unloaded Plugin package %s !", pp.getFileName()));
      return true;
    }
    return false;
  }

  @Override
  public boolean isRegistered(String packageId) {
    return packages.get(packageId) != null;
  }

  @Override
  public synchronized void initializePlugins(List<String> packageNames) {
    File dir = new File(Preferences.getPluginsDir());
    if (dir.exists() && dir.isDirectory()) {
      File[] plugins = dir.listFiles(new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
          // TODO: 优化一下，每次都判断是否包含。当插件数量大时，性能会降低
          return packageNames.contains(name);
        }
      });
      if ((plugins != null) && (plugins.length > 0)) {
        for (File file : plugins) {
          FileInputStream fis = null;
          String checksum = null;
          try {
            fis = new FileInputStream(file);
            checksum = DigestUtils.sha1Hex(fis);
            if (packages.get(checksum) == null) {
              register(file.toURI().toURL());
            }
          } catch (Exception e) {
            logger.warn(String.format("Failed to register plugin package at url: \"%s\"",
                file.getAbsolutePath()), e);
          } finally {
            IOUtils.closeQuietly(fis);
          }
        }
      }
    }
  }

  @Override
  public synchronized boolean activatePlugin(String pluginName) {
    return changeActivationStatus(pluginName, true);
  }

  @Override
  public synchronized boolean deactivatePlugin(String pluginName) {
    return changeActivationStatus(pluginName, false);
  }

  @Override
  public Plugin getPlugin(String pluginName) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin == null) {
      logger.error(String.format("Plugin with name \"%s\" not found!", pluginName));
    }
    return plugin;
  }
}
