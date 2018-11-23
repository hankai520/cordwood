
package ren.hankai.cordwood.web.pfms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.hankai.cordwood.web.security.AccessLimiter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 稳定拦截器，用来对请求限流、熔断，保证服务稳定性。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 11, 2018 6:17:42 PM
 */
@Component
public class StabilizationInterceptor implements HandlerInterceptor {

  // TODO: 单元测试稳定器
  @Autowired
  @Lazy
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Autowired
  private AccessLimiter accessLimiter;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    final HandlerExecutionChain chain = requestMappingHandlerMapping.getHandler(request);
    return accessLimiter.handleAccess(chain, response);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {}

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {}

}
