
package ren.hankai.cordwood.plugin;

import java.net.URL;
import java.util.List;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:45:01 PM
 */
public interface PluginLoader {

    /**
     * 载入插件集合
     *
     * @param jarFileUrl 插件地址
     * @return 插件实例，返回 null 表示加载失败
     * @author hankai
     * @since Sep 30, 2016 10:43:38 AM
     */
    List<Object> loadPlugins( URL jarFileUrl );

    /**
     * 载出插件，插件载出后，将无法在程序中使用该插件。载出只针对内存，不会影响插件包物理文件。
     *
     * @param instance 插件实例
     * @return 是否载出成功
     * @author hankai
     * @since Sep 30, 2016 10:44:05 AM
     */
    boolean unloadPlugin( Object instance );
}
