package ren.hankai.cordwood.console.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ren.hankai.cordwood.console.interceptor.PluginRequestInterceptor;
import ren.hankai.cordwood.console.persist.model.App;
import ren.hankai.cordwood.console.persist.model.PluginRequest;
import ren.hankai.cordwood.console.service.AppService;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.plugin.support.PluginRequestDispatcher;
import ren.hankai.cordwood.web.security.AccessAuthenticator;
import ren.hankai.cordwood.web.security.AccessAuthenticator.TokenInfo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * 插件访问分发控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:28:48 PM
 */
@Controller
public class PluggableController extends PluginRequestDispatcher {

  @Autowired
  private PluginService pluginService;
  @Autowired
  private AccessAuthenticator authenticator;
  @Autowired
  private AppService appService;

  @Override
  protected void beforeProcessingPluginRequest(String pluginName, String functionName,
      HttpServletRequest request) {
    final long now = System.currentTimeMillis();
    request.setAttribute(PluginRequestInterceptor.REQUEST_TIMESTAMP, now);
    PluginRequest pr = null;
    final Object object = request.getAttribute(PluginRequestInterceptor.PLUGIN_REQUEST);
    if (object == null) {
      pr = new PluginRequest();
    } else {
      pr = (PluginRequest) object;
    }
    pr.setPlugin(pluginService.getInstalledPluginByName(pluginName));
    final String tokenString = request.getParameter(AccessAuthenticator.ACCESS_TOKEN);
    if (StringUtils.isNotEmpty(tokenString)) {
      final TokenInfo tokenInfo = authenticator.parseAccessToken(tokenString);
      final App app = appService.getAppByAppKey(tokenInfo.getUserKey());
      pr.setApp(app);
    }
    pr.setClientIp(request.getRemoteAddr());
    if (request.getContentLengthLong() > 0) {
      pr.setRequestBytes(request.getContentLengthLong());
    } else {
      pr.setRequestBytes(0);
    }
    pr.setRequestUrl(request.getRequestURL().toString());
    pr.setRequestMethod(request.getMethod().toUpperCase());
    pr.setCreateTime(new Date(now));
    request.setAttribute(PluginRequestInterceptor.PLUGIN_REQUEST, pr);
  }

  @Override
  protected void afterProcessingPluginRequest(String pluginName, String functionName,
      HttpServletRequest request, Throwable errors) {
    if (errors != null) {
      request.setAttribute(PluginRequestInterceptor.RESPONSE_ERRORS, errors);
    }
  }


}
