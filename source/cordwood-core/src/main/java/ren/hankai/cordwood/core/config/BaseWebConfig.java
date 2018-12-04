/**
 *
 */

package ren.hankai.cordwood.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ren.hankai.cordwood.core.convert.StringToDateConverter;

/**
 * web 配置基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:53:27 PM
 */
public abstract class BaseWebConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToDateConverter(null));
  }

  @Bean
  public MappingJackson2HttpMessageConverter getJackson2HttpMessageConverter(ObjectMapper om) {
    return new MappingJackson2HttpMessageConverter(om);
  }
}
