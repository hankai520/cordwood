
package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;

import java.net.URL;

/**
 * 插件注册表。
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
   * @param overwrite 是否覆盖已有插件包
   * @return 插件包信息
   * @author hankai
   * @since Sep 30, 2016 10:42:38 AM
   */
  PluginPackage registerPackage(URL packageUrl, boolean overwrite);

  /**
   * 注册内存中已有的插件（注册信息不回被持久化）。
   *
   * @param pluginInstance 插件实例
   * @return 插件信息
   * @author hankai
   * @since Oct 31, 2016 11:27:47 PM
   */
  Plugin registerTransientPlugin(Object pluginInstance);

  /**
   * 注销插件包。根据传入的插件包注册号，从内存中卸载插件包中所有的插件，然后将插件包文件删除。
   *
   * @param packageId 插件包的SHA1校验和
   * @return 是否注销成功
   * @author hankai
   * @since Sep 30, 2016 10:43:15 AM
   */
  boolean unregisterPackage(String packageId);

  /**
   * 注销内存中已有的插件（这对通过插件包注册的插件同样有效）。
   *
   * @param pluginName 插件名称
   * @return 是否成功
   * @author hankai
   * @since Oct 31, 2016 11:31:01 PM
   */
  boolean unregisterTransientPlugin(String pluginName);

  /**
   * 检查插件表是否已注册。
   *
   * @param packageId 插件包 SHA1 校验和
   * @return 是否已注册
   * @author hankai
   * @since Oct 14, 2016 4:40:28 PM
   */
  boolean isPackageRegistered(String packageId);

  /**
   * 检查插件是否被注册（包含通过 registerTransientPlugin 注册的插件）。
   *
   * @param pluginName 插件名
   * @return 是否已注册
   * @author hankai
   * @since Oct 31, 2016 11:32:55 PM
   */
  boolean isPluginRegistered(String pluginName);
}
