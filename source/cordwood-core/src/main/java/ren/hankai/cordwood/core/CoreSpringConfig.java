package ren.hankai.cordwood.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Spring 核心配置类，用于定义 cordwood-core 所必要的组件。
 * 
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 4:42:14 PM
 */
@Configuration
public class CoreSpringConfig {

  @Bean
  public ConversionService getConversionService() {
    return new DefaultConversionService();
  }

}
