package ren.hankai.cordwood.console.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

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
  @Order(2)
  public static class FrontendWebSecurityConfigurationAdapter extends BaseSecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher(Route.FOREGROUND_PREFIX + "/**").authorizeRequests().anyRequest()
          .authenticated().and().formLogin().loginPage(Route.FG_LOGIN).permitAll()
          .failureUrl(Route.FG_LOGIN);
    }
  }

  @Configuration
  @Order(1)
  public static class BackendWebSecurityConfigurationAdapter extends BaseSecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // http.antMatcher(Route.BACKGROUND_PREFIX + "/**").authorizeRequests().anyRequest()
      // .hasAuthority("ADMIN").and().httpBasic();
      http.antMatcher(Route.BACKGROUND_PREFIX + "/**").authorizeRequests().anyRequest()
          .authenticated().and().httpBasic();
    }
  }

  // @Configuration
  // @Order(3)
  // public static class CommonWebSecurityConfigurationAdapter extends BaseSecurityConfig {
  //
  // @Override
  // protected void configure(HttpSecurity http) throws Exception {
  // http.authorizeRequests().anyRequest().permitAll();
  // }
  // }

}
