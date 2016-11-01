package ren.hankai.cordwood.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Spring 核心配置类，用于定义 cordwood-core 所必要的组件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 4:42:14 PM
 */
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
public class CoreSpringConfig {

  /**
   * JSON 序列化/反序列化。
   *
   * @return Jackson 核心对象
   * @author hankai
   * @since Oct 31, 2016 11:03:24 PM
   */
  @Bean
  public ObjectMapper getObjectMapper() {
    final ObjectMapper om = new ObjectMapper();
    om.enable(SerializationFeature.INDENT_OUTPUT);
    return om;
  }

  /**
   * 国际化字符串。
   *
   * @return MessageSource 的实现类
   * @author hankai
   * @since Jun 21, 2016 12:56:51 PM
   */
  @Bean(name = "messageSource")
  public ReloadableResourceBundleMessageSource getMessageSource() {
    final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasenames("WEB-INF/i18n/messages", "WEB-INF/i18n/validation");
    ms.setDefaultEncoding("UTF-8");
    ms.setCacheSeconds(0);
    ms.setFallbackToSystemLocale(false);
    ms.setUseCodeAsDefaultMessage(true);
    return ms;
  }

}
