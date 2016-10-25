
package ren.hankai.cordwood.plugin.api;

import java.io.InputStream;

/**
 * 插件静态资源加载接口。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 24, 2016 4:22:43 PM
 */
public interface PluginResourceLoader {

  /**
   * 获取插件资源。
   *
   * @param relativeUrl web请求相对路径，不以 "/"开头，此路径相对于插件资源根路径( http://.../resources/{ 这部分即相对路径 } )
   * @return 资源输入流
   * @author hankai
   * @since Oct 24, 2016 4:25:27 PM
   */
  InputStream getResource(String relativeUrl);
}
