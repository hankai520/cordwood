package ren.hankai.cordwood.console.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.RememberMeServices;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.BaseSecurityConfig;

/**
 * 控制台 web 访问的安全配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 27, 2016 5:15:45 PM
 */
@EnableWebSecurity(debug = false)
public class SecurityConfig {

  @Configuration
  @Order(1)
  public static class BackendWebSecurityConfigurationAdapter extends BaseSecurityConfig {

    @Autowired
    private RememberMeServices rememberMeServices;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher(Route.BACKGROUND_PREFIX + "/**")
          .authorizeRequests()

          .antMatchers(Route.BG_PLUGINS + "/**")
          .hasAnyAuthority(PredefinedRoles.ADMIN, PredefinedRoles.CONFIG)

          .antMatchers(Route.BG_PLUGIN_LOGS + "/**")
          .hasAnyAuthority(PredefinedRoles.ADMIN)

          .antMatchers(Route.BG_USERS + "/**")
          .hasAuthority(PredefinedRoles.ADMIN)

          .anyRequest().authenticated();

      http.formLogin()
          .loginPage(Route.BG_LOGIN)
          .defaultSuccessUrl(Route.BG_DASHBOARD)
          .permitAll();

      http.logout()
          .logoutUrl(Route.BG_LOGOUT)
          .permitAll();

      http.rememberMe()
          .rememberMeServices(rememberMeServices)
          .key(Preferences.getSystemSk())
          .tokenValiditySeconds(60 * 60 * 24 * 7);
    }
  }

  @Configuration
  @Order(3)
  public static class CommonWebSecurityConfigurationAdapter extends BaseSecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().permitAll();
    }
  }

}
