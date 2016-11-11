
package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.PluginPackage;

/**
 * 插件验证器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:45:09 PM
 */
public interface PluginValidator {

  /**
   * 检查插件包是否有效。
   *
   * @param pluginPackage 插件包
   * @return 插件包是否通过验证
   * @author hankai
   * @since Sep 30, 2016 10:18:07 AM
   */
  boolean validatePackage(PluginPackage pluginPackage);
}
