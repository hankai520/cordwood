
package ren.hankai.cordwood.plugin.support;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginEventEmitter;
import ren.hankai.cordwood.plugin.api.PluginLoader;
import ren.hankai.cordwood.plugin.api.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginRegistry;
import ren.hankai.cordwood.plugin.api.PluginResolver;
import ren.hankai.cordwood.plugin.api.PluginValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
  private PluginResolver pluginResolver;
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
    final Plugin plugin = plugins.get(pluginName);
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

  @Override
  public synchronized PluginPackage registerPackage(URL packageUrl, boolean overwrite) {
    if (!pluginValidator.validatePackage(packageUrl)) {
      logger.error(String.format("Failed to verify plugin package at %s", packageUrl.toString()));
    } else {
      final PluginPackage pack = pluginResolver.resolvePackage(packageUrl);
      final PluginPackage loadedPack = packages.get(pack.getIdentifier());
      if ((loadedPack != null) && !overwrite) {
        return loadedPack;
      }
      if (loadedPack != null) {
        unregisterPackage(loadedPack.getIdentifier());
      }
      final String name = FilenameUtils.getName(packageUrl.getPath());
      final List<Object> instances = pluginLoader.loadPlugins(packageUrl);
      if ((instances != null) && !instances.isEmpty()) {
        final List<Plugin> pluginsToPut = new ArrayList<>();
        for (final Object instance : instances) {
          final Plugin plugin = pluginResolver.resolvePlugin(instance);
          pack.addPlugin(plugin);
          pluginsToPut.add(plugin);
        }
        pack.setInstallUrl(packageUrl);
        for (final Plugin plugin : pluginsToPut) {
          plugins.put(plugin.getName(), plugin);
          pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
        }
        packages.put(pack.getIdentifier(), pack);
        logger.info(String.format("Loaded plugin package [ %s ]", name));
        return pack;
      } else {
        logger.error(String.format("No plugin definitions found in package \"%s\" !", name));
      }
    }
    return null;
  }

  @Override
  public Plugin registerTransientPlugin(Object pluginInstance) {
    if (pluginInstance == null) {
      return null;
    }
    final Plugin plugin = pluginResolver.resolvePlugin(pluginInstance);
    plugins.put(plugin.getName(), plugin);
    pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
    return plugin;
  }

  @Override
  public synchronized boolean unregisterPackage(String packageId) {
    final PluginPackage pp = packages.get(packageId);
    if (pp != null) {
      for (final Plugin p : pp.getPlugins()) {
        pluginLoader.unloadPlugin(p.getInstance());
        plugins.remove(p.getName());
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_UNLOADED, p);
      }
      packages.remove(packageId);
      logger.info(String.format("Unloaded Plugin package [ %s ]", pp.getFileName()));
      return true;
    }
    return false;
  }

  @Override
  public boolean unregisterTransientPlugin(String pluginName) {
    final Plugin plugin = plugins.get(pluginName);
    if (plugin != null) {
      if (plugin.getPackageId() != null) {
        final PluginPackage pack = packages.get(plugin.getPackageId());
        pack.getPlugins().remove(plugin);
      }
      plugins.remove(pluginName);
      pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_UNLOADED, plugin);
      return true;
    }
    return false;
  }

  @Override
  public boolean isPackageRegistered(String packageId) {
    return packages.get(packageId) != null;
  }

  @Override
  public boolean isPluginRegistered(String pluginName) {
    return plugins.get(pluginName) != null;
  }

  @Override
  public synchronized void initializePlugins(List<String> packageNames) {
    final File dir = new File(Preferences.getPluginsDir());
    if (dir.exists() && dir.isDirectory()) {
      final File[] plugins = dir.listFiles(new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
          // TODO: 优化一下，每次都判断是否包含。当插件数量大时，性能会降低
          return packageNames.contains(name);
        }
      });
      if ((plugins != null) && (plugins.length > 0)) {
        for (final File file : plugins) {
          FileInputStream fis = null;
          String checksum = null;
          try {
            fis = new FileInputStream(file);
            checksum = DigestUtils.sha1Hex(fis);
            if (packages.get(checksum) == null) {
              registerPackage(file.toURI().toURL(), false);
            }
          } catch (final Exception e) {
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
    final Plugin plugin = plugins.get(pluginName);
    if (plugin == null) {
      logger.error(String.format("Plugin with name \"%s\" not found!", pluginName));
    }
    return plugin;
  }

  @Override
  public Iterator<Plugin> getPluginIterator() {
    return plugins.values().iterator();
  }
}
