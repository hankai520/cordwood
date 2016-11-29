package ren.hankai.cordwood.console.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import ren.hankai.cordwood.console.service.LoginCredentialService;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.config.PluginConfig;

/**
 * Spring Bean 配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:45:47 PM
 *
 */
@Configuration
@Import(PluginConfig.class)
public class BeanConfig extends CoreSpringConfig {

  @Bean
  public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
      LoginCredentialService loginCredentialService) {
    final PersistentTokenBasedRememberMeServices service =
        new PersistentTokenBasedRememberMeServices(Preferences.getSystemSk(),
            userDetailsService, loginCredentialService);
    return service;
  }

}
