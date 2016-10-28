package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.domain.PluginFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件功能参数映射器。将请求中的参数映射为方法所需参数。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:40:53 AM
 */
public interface ParameterMapper {
  /**
   * 将 HTTP 请求中的参数映射到插件功能所需的参数上。
   * 
   * @param function 插件功能
   * @param request 请求
   * @param response 响应
   * @return 插件功能参数列表
   * @author hankai
   * @since Oct 28, 2016 10:35:31 AM
   */
  Object[] mapParameters(PluginFunction function, HttpServletRequest request,
      HttpServletResponse response);
}
