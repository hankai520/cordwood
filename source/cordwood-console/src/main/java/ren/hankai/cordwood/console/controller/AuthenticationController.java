
package ren.hankai.cordwood.console.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.web.bind.annotation.RequestMapping;

import ren.hankai.cordwood.console.config.Route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @Controller
public class AuthenticationController {

  @RequestMapping(Route.BG_LOGOUT)
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      final CookieClearingLogoutHandler cookieLogoutHandler = new CookieClearingLogoutHandler(
          AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
      cookieLogoutHandler.logout(request, response, auth);
      final SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
      contextLogoutHandler.logout(request, response, auth);
    }
    return String.format("redirect:%s?logout", Route.BG_LOGIN);
  }

}
