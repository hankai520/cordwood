package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginPackage;

import java.net.URL;

/**
 * 插件解析器，用于解析插件结构，构造模型，用于后续使用。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:16:26 AM
 */
public interface PluginResolver {
  /**
   * 解析插件包。解析插件包并不会同时解析插件包内的插件实例，因为这涉及到插件包的加载逻辑。
   *
   * @param packageUrl 插件包本地路径
   * @return 插件包信息
   * @author hankai
   * @since Oct 28, 2016 9:17:55 AM
   */
  PluginPackage resolvePackage(URL packageUrl);

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
