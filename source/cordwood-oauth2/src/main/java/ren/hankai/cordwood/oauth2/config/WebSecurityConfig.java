package ren.hankai.cordwood.oauth2.config;

import org.master.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

@Profile("oauth2-server")
@Configuration
@EnableAuthorizationServer
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;

  @Autowired
  OAuth2ClientContext oauth2ClientContext;

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    // Configure spring security's authenticationManager with custom
    // user details service
    auth.userDetailsService(userService);
  }

  @Override
  @Bean // share AuthenticationManager for web and oauth
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/user/**").authenticated()
        .anyRequest().permitAll()
        .and().exceptionHandling()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .and()
        .formLogin().loginPage("/login").loginProcessingUrl("/login.do")
        .defaultSuccessUrl("/user/info")
        .failureUrl("/login?err=1")
        .permitAll()
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .permitAll()

        .and().addFilterBefore(githubFilter(), BasicAuthenticationFilter.class);

  }

  private Filter githubFilter() {
    final OAuth2ClientAuthenticationProcessingFilter githubFilter =
        new OAuth2ClientAuthenticationProcessingFilter("/login/github");
    final OAuth2RestTemplate githubTemplate =
        new OAuth2RestTemplate(githubClient().getClient(), oauth2ClientContext);
    githubFilter.setRestTemplate(githubTemplate);
    githubFilter.setTokenServices(new UserInfoTokenServices(
        githubClient().getResource().getUserInfoUri(), githubClient().getClient().getClientId()));
    return githubFilter;
  }

  @Bean
  @ConfigurationProperties("github")
  public ClientResources githubClient() {
    return new ClientResources();
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(
      OAuth2ClientContextFilter filter) {
    final FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
    }
  }
}
