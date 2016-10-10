
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.domain.Plugin;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 8, 2016 4:43:14 PM
 */
public interface PluginEventListener {

    void handleEvent( Plugin plugin, String eventType );
}
