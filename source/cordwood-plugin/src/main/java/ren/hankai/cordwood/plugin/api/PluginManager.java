
package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;

import java.util.Iterator;
import java.util.List;

/**
 * 插件管理器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:44:44 PM
 */
public interface PluginManager {

  /**
   * 启用插件。
   *
   * @param pluginName 插件标识符
   * @return 是否启用成功
   * @author hankai
   * @since Sep 30, 2016 10:46:18 AM
   */
  boolean activatePlugin(String pluginName);

  /**
   * 禁用插件。
   *
   * @param pluginName 插件标识符
   * @return 是否禁用成功
   * @author hankai
   * @since Sep 30, 2016 10:46:34 AM
   */
  boolean deactivatePlugin(String pluginName);

  /**
   * 获取插件模型。
   *
   * @param pluginName 插件标识符
   * @return 插件模型
   * @author hankai
   * @since Sep 30, 2016 10:46:53 AM
   */
  Plugin getPlugin(String pluginName);

  /**
   * 获取插件包模型。
   *
   * @param identifier 插件表标识
   * @return 插件包模型
   * @author hankai
   * @since Nov 11, 2016 11:39:30 PM
   */
  PluginPackage getPluginPackage(String identifier);

  /**
   * 初始化已安装的插件。
   *
   * @param installedPackages 已登记的插件包集合
   * @author hankai
   * @since Oct 9, 2016 10:56:22 AM
   */
  void initializePlugins(List<PluginPackage> installedPackages);

  /**
   * 获取插件迭代器。
   *
   * @return 迭代器
   * @author hankai
   * @since Nov 10, 2016 10:41:26 AM
   */
  Iterator<Plugin> getPluginIterator();
}
