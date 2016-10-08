
package ren.hankai.cordwood.plugin;

import java.net.URL;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:44:32 PM
 */
public interface PluginRegistry {

    /**
     * 安装插件
     * 
     * @param pluginUrl 插件地址
     * @return 是否安装成功
     * @author hankai
     * @since Sep 30, 2016 10:42:38 AM
     */
    boolean installPlugin( URL pluginUrl );

    /**
     * 卸载插件
     * 
     * @param pluginId 插件ID
     * @return 是否卸载成功
     * @author hankai
     * @since Sep 30, 2016 10:43:15 AM
     */
    boolean uninstallPlugin( String pluginId );
}
