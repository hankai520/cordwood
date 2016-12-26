
package ren.hankai.cordwood.console.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.console.persist.model.PluginRequestBean.RequestChannel;
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
    final Device device = DeviceUtils.getCurrentDevice(request);
    if (device.isMobile()) {
      pr.setChannel(RequestChannel.MobilePhone);
    } else if (device.isTablet()) {
      pr.setChannel(RequestChannel.Tablet);
    } else if (isRequestSentViaWebBrowser(request)) {
      pr.setChannel(RequestChannel.Desktop);
    } else {
      pr.setChannel(RequestChannel.Other);
    }
    pluginService.savePluginRequest(pr);
  }

  /**
   * 判断请求是否来自主流浏览器。
   *
   * @param request HTTP 请求
   * @return 是否来自主流浏览器
   * @author hankai
   * @since Dec 14, 2016 4:05:02 PM
   */
  private boolean isRequestSentViaWebBrowser(HttpServletRequest request) {
    String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    if (StringUtils.isNotEmpty(userAgent)) {
      userAgent = userAgent.toLowerCase();
      if (userAgent.matches("^.*(firefox|seamonkey|chrome|chromium|safari|opr|opera|msie){1}.*$")) {
        return true;
      }
    }
    return false;
  }

}
