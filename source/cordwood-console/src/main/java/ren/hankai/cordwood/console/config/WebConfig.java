/**
 *
 */
package ren.hankai.cordwood.console.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Web 配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 3:19:49 PM
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Autowired
  private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Bean
  public MappingJackson2HttpMessageConverter getJackson2HttpMessageConverter(ObjectMapper om) {
    return new MappingJackson2HttpMessageConverter(om);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new ByteArrayHttpMessageConverter());
    converters.add(new StringHttpMessageConverter());
    converters.add(new ResourceHttpMessageConverter());
    converters.add(new SourceHttpMessageConverter<>());
    converters.add(new AllEncompassingFormHttpMessageConverter());
    converters.add(mappingJackson2HttpMessageConverter);
  }

}
