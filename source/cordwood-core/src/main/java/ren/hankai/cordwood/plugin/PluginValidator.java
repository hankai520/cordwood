
package ren.hankai.cordwood.plugin;

import java.net.URL;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:45:09 PM
 */
public interface PluginValidator {

    /**
     * 检查插件包所在地址是否有效，插件包签名是否正确，插件包是否已安装过
     *
     * @param jarUrl 插件包地址
     * @return 插件包是否通过验证
     * @author hankai
     * @since Sep 30, 2016 10:18:07 AM
     */
    boolean validatePackage( URL jarFilePath );
}
