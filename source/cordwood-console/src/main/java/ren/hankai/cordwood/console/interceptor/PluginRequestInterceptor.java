
package ren.hankai.cordwood.console.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.console.service.PluginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件请求拦截器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 10:24:07 AM
 */
@Component
public class PluginRequestInterceptor implements HandlerInterceptor {

  public static final String PLUGIN_REQUEST = "plugin-request";
  public static final String REQUEST_TIMESTAMP = "request-timestamp";
  public static final String RESPONSE_ERRORS = "response-errors";

  @Autowired
  private PluginService pluginService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    final long begin = (long) request.getAttribute(REQUEST_TIMESTAMP);
    final long timespan = System.currentTimeMillis() - begin;
    final PluginRequestBean pr = (PluginRequestBean) request.getAttribute(PLUGIN_REQUEST);
    pr.setMilliseconds(timespan);
    if (request.getAttribute(RESPONSE_ERRORS) != null) {
      final Throwable errors = (Throwable) request.getAttribute(RESPONSE_ERRORS);
      pr.setErrors(errors.toString());
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    final PluginRequestBean pr = (PluginRequestBean) request.getAttribute(PLUGIN_REQUEST);
    pr.setResponseCode(response.getStatus());
    final String len = response.getHeader(HttpHeaders.CONTENT_LENGTH);
    pr.setResponseBytes(Long.parseLong(len));
    pluginService.savePluginRequest(pr);
  }

}
