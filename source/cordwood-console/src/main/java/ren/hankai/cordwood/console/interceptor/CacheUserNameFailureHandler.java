
package ren.hankai.cordwood.console.interceptor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ren.hankai.cordwood.console.config.Route;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 缓存用户名的登录失败处理器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 29, 2017 6:19:57 PM
 */
@Component
public class CacheUserNameFailureHandler implements AuthenticationFailureHandler {

  public static final String LOGIN_ERROR = "login_error";

  public static final String LAST_USER_NAME = "last_user_name";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    request.getSession().setAttribute(LOGIN_ERROR, "");
    request.getSession().setAttribute(LAST_USER_NAME, request.getParameter("username"));
    response.sendRedirect(Route.BG_LOGIN);
  }

}
