
package ren.hankai.cordwood.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ren.hankai.cordwood.core.Preferences;

/**
 * Spring 核心配置类，用于定义 cordwood-core 所必要的组件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 4:42:14 PM
 */
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
public abstract class CoreSpringConfig {

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
    final String externalFile = "file:" + Preferences.getConfigFilePath("i18n");
    ms.setBasenames(externalFile, "WEB-INF/i18n/messages", "WEB-INF/i18n/validation");
    ms.setDefaultEncoding("UTF-8");
    ms.setCacheSeconds(30);
    ms.setFallbackToSystemLocale(false);
    ms.setUseCodeAsDefaultMessage(true);
    return ms;
  }
}
