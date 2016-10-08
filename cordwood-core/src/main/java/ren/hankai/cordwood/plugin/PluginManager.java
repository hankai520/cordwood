
package ren.hankai.cordwood.plugin;

import java.util.Iterator;

import ren.hankai.cordwood.core.domain.Plugin;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:44:44 PM
 */
public interface PluginManager extends PluginRegistry {

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
     * 初始化所有已安装的插件
     *
     * @author hankai
     * @since Sep 30, 2016 11:07:12 AM
     */
    void initializeInstalledPlugins();

    /**
     * 获取插件迭代器
     *
     * @return 插件迭代器
     * @author hankai
     * @since Sep 30, 2016 3:54:19 PM
     */
    Iterator<Plugin> getPluginIterator();
}
