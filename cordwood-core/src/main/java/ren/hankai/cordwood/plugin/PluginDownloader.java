
package ren.hankai.cordwood.plugin;

import java.net.URL;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 8, 2016 5:39:29 PM
 */
public interface PluginDownloader {

    /**
     * 如果插件包URL是一个网络地址，则下载插件包
     *
     * @param url 插件包URL地址
     * @return 插件包本地安装路径。非空则表示下载成功
     * @author hankai
     * @since Oct 8, 2016 5:39:58 PM
     */
    URL downloadIfNeeded( URL url );
}
