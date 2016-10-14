
package ren.hankai.cordwood.plugin;

import java.net.URL;

import ren.hankai.cordwood.core.domain.PluginPackage;

/**
 * 插件注册表
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:44:32 PM
 */
public interface PluginRegistry {

    /**
     * 注册插件包。根据传入的插件包地址，下载或复制插件包文件到程序插件目录，然后载入插件到内存中。
     *
     * @param packageUrl 插件包本地路径，即 file://... 这种形式
     * @return 插件包信息
     * @author hankai
     * @since Sep 30, 2016 10:42:38 AM
     */
    PluginPackage register( URL packageUrl );

    /**
     * 注销插件包。根据传入的插件包注册号，从内存中卸载插件包中所有的插件，然后将插件包文件删除。
     *
     * @param packageId 插件包的SHA1校验和
     * @return 是否注销成功
     * @author hankai
     * @since Sep 30, 2016 10:43:15 AM
     */
    boolean unregister( String packageId );

    /**
     * 检查插件表是否已注册
     * 
     * @param packageId 插件包 SHA1 校验和
     * @return 是否已注册
     * @author hankai
     * @since Oct 14, 2016 4:40:28 PM
     */
    boolean isRegistered( String packageId );
}
