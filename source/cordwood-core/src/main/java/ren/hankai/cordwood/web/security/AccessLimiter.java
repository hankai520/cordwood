
package ren.hankai.cordwood.web.security;

import org.springframework.web.servlet.HandlerExecutionChain;

import javax.servlet.http.HttpServletResponse;

/**
 * 访问限制器，通过限流、熔断机制保护服务器，增强稳定性。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 12, 2018 10:51:56 AM
 */
public interface AccessLimiter {

  /**
   * 处理访问。
   *
   * @param requestHandler 请求处理器
   * @param response HTTP响应，用于写入响应代码
   * @return 是否允许处理访问
   * @author hankai
   * @since Oct 12, 2018 10:57:35 AM
   */
  boolean handleAccess(HandlerExecutionChain requestHandler, HttpServletResponse response);

}
