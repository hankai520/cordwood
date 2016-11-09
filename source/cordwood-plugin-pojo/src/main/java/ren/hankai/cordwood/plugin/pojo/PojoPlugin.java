
package ren.hankai.cordwood.plugin.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 示例插件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 3:51:07 PM
 */
@Pluggable(name = "pojo", version = "1.0.0")
public class PojoPlugin implements PluginLifeCycleAware, PluginResourceLoader {

  private static final Logger logger = LoggerFactory.getLogger(PojoPlugin.class);

  /**
   * 求和（使用请求对象手工获取参数）。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 和
   * @author hankai
   * @since Oct 25, 2016 2:18:22 PM
   */
  @Functional(name = "sum", resultType = "text/plain")
  public String sum(HttpServletRequest request, HttpServletResponse response) {
    final int op1 = Integer.parseInt(request.getParameter("op1"));
    final int op2 = Integer.parseInt(request.getParameter("op2"));
    return "Hi, the result is: " + (op1 + op2);
  }

  /**
   * 求和（自动将请求中的参数映射到方法参数）。
   *
   * @param op1 第一个操作数
   * @param op2 第二个操作数
   * @return 和
   * @author hankai
   * @since Oct 29, 2016 1:05:04 AM
   */
  @Functional(name = "sum2", resultType = "text/plain")
  public String sum2(Integer op1, Integer op2) {
    return "Hi, the result is: " + (op1 + op2);
  }

  @Override
  public void pluginDidActivated() {
    logger.warn("plugin activated");
  }

  @Override
  public void pluginDidDeactivated() {
    logger.warn("plugin deactivated");
  }

  @Override
  public void pluginDidLoad() {
    logger.warn("plugin did load");
  }

  @Override
  public void pluginDidUnload() {
    logger.warn("plugin did unload");
  }

  @Override
  public InputStream getResource(String relativeUrl) {
    final URL url = this.getClass().getResource("/static/" + relativeUrl);
    if (url != null) {
      try {
        return url.openStream();
      } catch (final IOException e) {
        logger.error(String.format("Failed to open resource: \"%s\"", relativeUrl), e);
      }
    }
    return null;
  }
}
