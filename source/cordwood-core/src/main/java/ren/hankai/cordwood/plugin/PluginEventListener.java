
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.domain.Plugin;

/**
 * 事件监听器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 8, 2016 4:43:14 PM
 */
public interface PluginEventListener {

  /**
   * 处理插件事件。
   *
   * @param plugin 插件
   * @param event 事件
   * @author hankai
   * @since Oct 13, 2016 1:03:51 PM
   */
  void handleEvent(Plugin plugin, String event);
}
