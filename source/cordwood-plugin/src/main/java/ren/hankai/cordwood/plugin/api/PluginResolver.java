package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.Plugin;

/**
 * 插件解析器，用于解析插件结构，构造模型，用于后续使用。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:16:26 AM
 */
public interface PluginResolver {
  /**
   * 解析插件实例。
   *
   * @param pluginInstance 插件实例
   * @return 插件信息
   * @author hankai
   * @since Oct 28, 2016 9:27:54 AM
   */
  Plugin resolvePlugin(Object pluginInstance);
}
