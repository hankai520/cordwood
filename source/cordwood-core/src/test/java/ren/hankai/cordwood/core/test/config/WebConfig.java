
package ren.hankai.cordwood.core.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.BaseWebConfig;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Web MVC核心配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:51:57 PM
 */
@EnableWebMvc
@Configuration
@Profile(Preferences.PROFILE_TEST)
public class WebConfig extends BaseWebConfig {

  @Autowired
  private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new ByteArrayHttpMessageConverter());
    converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    converters.add(new ResourceHttpMessageConverter());
    converters.add(new SourceHttpMessageConverter<>());
    converters.add(new AllEncompassingFormHttpMessageConverter());
    converters.add(mappingJackson2HttpMessageConverter);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

  }

}
