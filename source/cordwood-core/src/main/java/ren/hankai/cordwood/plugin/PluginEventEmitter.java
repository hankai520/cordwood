
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.domain.Plugin;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 8, 2016 4:50:32 PM
 */
public interface PluginEventEmitter {

    public static final String PLUGIN_LOADED      = "cw-plugin-loaded";
    public static final String PLUGIN_UNLOADED    = "cw-plugin-unloaded";
    public static final String PLUGIN_ACTIVATED   = "cw-plugin-activated";
    public static final String PLUGIN_DEACTIVATED = "cw-plugin-deactivated";

    /**
     * 添加插件事件监听器
     *
     * @param event 事件名称
     * @param listener 监听器
     * @author hankai
     * @since Oct 8, 2016 5:01:34 PM
     */
    void addListener( String event, PluginEventListener listener );

    void removeListener( String event, PluginEventListener listener );

    void removeListener( PluginEventListener listener );

    void emitEvent( String event, Plugin sender );
}
