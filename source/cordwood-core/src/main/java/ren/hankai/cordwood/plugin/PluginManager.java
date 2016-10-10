
package ren.hankai.cordwood.plugin;

import java.util.List;

import ren.hankai.cordwood.core.domain.Plugin;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:44:44 PM
 */
public interface PluginManager {

    /**
     * 启用插件
     *
     * @param pluginName 插件标识符
     * @return 是否启用成功
     * @author hankai
     * @since Sep 30, 2016 10:46:18 AM
     */
    boolean activatePlugin( String pluginName );

    /**
     * 禁用插件
     *
     * @param pluginName 插件标识符
     * @return 是否禁用成功
     * @author hankai
     * @since Sep 30, 2016 10:46:34 AM
     */
    boolean deactivatePlugin( String pluginName );

    /**
     * 获取插件
     *
     * @param pluginName 插件标识符
     * @return 插件
     * @author hankai
     * @since Sep 30, 2016 10:46:53 AM
     */
    Plugin getPlugin( String pluginName );

    /**
     * 初始化已安装的插件
     *
     * @param packageNames 插件包文件名
     * @author hankai
     * @since Oct 9, 2016 10:56:22 AM
     */
    void initializePlugins( List<String> packageNames );
}
