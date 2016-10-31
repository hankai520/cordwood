
package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.Plugin;

/**
 * 插件事件发射器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 8, 2016 4:50:32 PM
 * @see ren.hankai.cordwood.plugin.PluginEventListener
 */
public interface PluginEventEmitter {

  /**
   * 插件被加载。
   */
  public static final String PLUGIN_LOADED = "cw-plugin-loaded";
  /**
   * 插件被卸载。
   */
  public static final String PLUGIN_UNLOADED = "cw-plugin-unloaded";
  /**
   * 插件被启用。
   */
  public static final String PLUGIN_ACTIVATED = "cw-plugin-activated";
  /**
   * 插件被禁用。
   */
  public static final String PLUGIN_DEACTIVATED = "cw-plugin-deactivated";

  /**
   * 添加插件事件监听器。
   *
   * @param event 事件名称
   * @param listener 监听器
   * @author hankai
   * @since Oct 8, 2016 5:01:34 PM
   */
  void addListener(String event, PluginEventListener listener);

  /**
   * 删除特定插件事件监听器。
   *
   * @param event 事件名称
   * @param listener 监听器
   * @author hankai
   * @since Oct 13, 2016 1:01:21 PM
   */
  void removeListener(String event, PluginEventListener listener);

  /**
   * 删除监听器关联的所有事件监听。
   *
   * @param listener 监听器
   * @author hankai
   * @since Oct 13, 2016 1:01:56 PM
   */
  void removeListener(PluginEventListener listener);

  /**
   * 发布事件。
   *
   * @param event 事件名
   * @param sender 事件源
   * @author hankai
   * @since Oct 13, 2016 1:02:46 PM
   */
  void emitEvent(String event, Plugin sender);
}
