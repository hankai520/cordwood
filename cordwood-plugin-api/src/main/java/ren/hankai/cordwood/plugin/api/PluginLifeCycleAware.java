
package ren.hankai.cordwood.plugin.api;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 9:49:28 AM
 */
public interface PluginLifeCycleAware {

    void pluginDidLoad();

    void pluginDidDestroy();
}
