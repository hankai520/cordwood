
package ren.hankai.cordwood.plugin.support;

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
import java.net.URL;
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

  /**
   * 注册插件包。
   *
   * @param pluginPackage 插件包
   * @param overwrite 是否覆盖已注册的插件包
   * @return 注册后的插件包
   * @author hankai
   * @since Nov 12, 2016 12:33:25 AM
   */
  private PluginPackage registerPackage(PluginPackage pluginPackage, boolean overwrite) {
    final PluginPackage loadedPack = packages.get(pluginPackage.getIdentifier());
    if ((loadedPack != null) && !overwrite) {
      return loadedPack;
    }
    if (loadedPack != null) {
      unregisterPackage(loadedPack.getIdentifier());
    }
    final List<Object> instances = pluginLoader.loadPlugins(pluginPackage);
    if ((instances != null) && !instances.isEmpty()) {
      for (final Object instance : instances) {
        final Plugin plugin = pluginResolver.resolvePlugin(instance);
        pluginPackage.addPlugin(plugin);
        plugins.put(plugin.getName(), plugin);
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
      }
      packages.put(pluginPackage.getIdentifier(), pluginPackage);
      logger.info(String.format("Loaded plugin package [ %s ]", pluginPackage.getFileName()));
      return pluginPackage;
    } else {
      logger.error(String.format("No plugin definitions found in package [ %s ]",
          pluginPackage.getFileName()));
    }
    return null;
  }

  @Override
  public synchronized PluginPackage registerPackage(URL packageUrl, boolean overwrite) {
    final PluginPackage pluginPackage = new PluginPackage(packageUrl);
    if (!pluginValidator.validatePackage(pluginPackage)) {
      logger.error(String.format("Invalid plugin package at %s", packageUrl.toString()));
      return null;
    } else {
      return registerPackage(pluginPackage, overwrite);
    }
  }

  @Override
  public Plugin registerTransientPlugin(Object pluginInstance, boolean overwrite) {
    if (pluginInstance == null) {
      return null;
    }
    final Plugin plugin = pluginResolver.resolvePlugin(pluginInstance);
    if (overwrite || (plugins.get(plugin.getName()) == null)) {
      plugins.put(plugin.getName(), plugin);
      pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_LOADED, plugin);
    }
    return plugin;
  }

  @Override
  public synchronized boolean unregisterPackage(String packageId) {
    final PluginPackage pp = packages.get(packageId);
    if (pp != null) {
      for (final Plugin p : pp.getPlugins()) {
        if (!pluginLoader.unloadPlugin(p.getInstance())) {
          return false;
        }
        plugins.remove(p.getName());
        pluginEventEmitter.emitEvent(PluginEventEmitter.PLUGIN_UNLOADED, p);
      }
      packages.remove(packageId);
      logger.info(String.format("Unloaded Plugin package [ %s ]", pp.getFileName()));
    }
    return true;
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
  public synchronized void initializePlugins(List<PluginPackage> installedPackages) {
    final File dir = new File(Preferences.getPluginsDir());
    if (dir.exists() && dir.isDirectory()) {
      final File[] plugins = dir.listFiles();
      if ((plugins != null) && (plugins.length > 0)) {
        for (final File file : plugins) {
          try {
            for (final PluginPackage pp : installedPackages) {
              if (file.getName().equals(pp.getFileName())) {
                registerPackage(pp, false);
                break;
              }
            }
          } catch (final Exception ex) {
            logger.error("Failed to initialize plugin: " + file.getName(), ex);
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
      logger.debug(String.format("Plugin with name \"%s\" not found!", pluginName));
    }
    return plugin;
  }

  @Override
  public PluginPackage getPluginPackage(String identifier) {
    final PluginPackage pp = packages.get(identifier);
    if (pp == null) {
      logger.debug(String.format("Plugin package [ %s ] not found!", identifier));
    }
    return pp;
  }

  @Override
  public Iterator<Plugin> getPluginIterator() {
    return plugins.values().iterator();
  }
}
