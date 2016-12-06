package ren.hankai.cordwood.console.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import ren.hankai.cordwood.console.service.LoginCredentialService;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.config.PluginConfig;
import ren.hankai.cordwood.web.breadcrumb.BreadCrumbInterceptor;

import java.nio.charset.Charset;

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

  @Override
  public ReloadableResourceBundleMessageSource getMessageSource() {
    final ReloadableResourceBundleMessageSource ms = super.getMessageSource();
    final String externalFile = "file:" + Preferences.getConfigFilePath("i18n");
    ms.addBasenames(externalFile);
    ms.setCacheSeconds(0);
    return ms;
  }

  /**
   * 记住登录业务逻辑实现。
   *
   * @param userDetailsService 用户信息业务逻辑
   * @param loginCredentialService 登录凭证业务逻辑
   * @return 记住登录业务逻辑实现。
   * @author hankai
   * @since Dec 6, 2016 3:04:23 PM
   */
  @Bean
  public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
      LoginCredentialService loginCredentialService) {
    final PersistentTokenBasedRememberMeServices service =
        new PersistentTokenBasedRememberMeServices(Preferences.getSystemSk(),
            userDetailsService, loginCredentialService);
    return service;
  }

  /**
   * HTTP JSON 消息转换器。
   *
   * @param om jackson json 核心对象
   * @return HTTP JSON 消息转换器
   * @author hankai
   * @since Nov 8, 2016 8:47:02 AM
   */
  @Bean
  public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(ObjectMapper om) {
    final MappingJackson2HttpMessageConverter cvt = new MappingJackson2HttpMessageConverter(om);
    cvt.setDefaultCharset(Charset.forName("UTF-8"));
    return cvt;
  }

  /**
   * 面包屑导航拦截器。
   *
   * @return 面包屑导航拦截器
   * @author hankai
   * @since Dec 6, 2016 3:05:15 PM
   */
  @Bean
  public BreadCrumbInterceptor breadCrumbInterceptor() {
    return new BreadCrumbInterceptor();
  }

}
