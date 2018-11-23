
package ren.hankai.cordwood.core.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;

import java.nio.charset.Charset;

/**
 * 用于测试的 Spring 核心配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:47:40 PM
 */
@Configuration
@Profile(Preferences.PROFILE_TEST)
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
}
