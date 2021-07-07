
package ren.hankai.cordwood.web.support;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 替换Servlet请求为支持多次读取数据的请求包装类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 26, 2018 4:52:12 PM
 */
public class MultiReadFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    final MultiReadHttpServletRequest multiReadRequest =
        new MultiReadHttpServletRequest((HttpServletRequest) request);
    chain.doFilter(multiReadRequest, response);
  }

  @Override
  public void destroy() {}

}
