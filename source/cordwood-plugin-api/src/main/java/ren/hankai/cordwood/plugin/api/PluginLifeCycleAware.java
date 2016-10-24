
package ren.hankai.cordwood.plugin.api;

/**
 * 插件生命周期关注接口，实现此接口后，能感知到插件状态发生的变化。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 9:49:28 AM
 */
public interface PluginLifeCycleAware {

  /**
   * 插件被加载
   *
   * @author hankai
   * @since Oct 13, 2016 9:17:13 AM
   */
  void pluginDidLoad();

  /**
   * 插件被卸载
   *
   * @author hankai
   * @since Oct 13, 2016 9:17:33 AM
   */
  void pluginDidUnload();

  /**
   * 插件被启用（激活）
   * 
   * @author hankai
   * @since Oct 13, 2016 9:18:35 AM
   */
  void pluginDidActivated();

  /**
   * 插件被禁用（失活）
   * 
   * @author hankai
   * @since Oct 13, 2016 9:18:48 AM
   */
  void pluginDidDeactivated();
}
