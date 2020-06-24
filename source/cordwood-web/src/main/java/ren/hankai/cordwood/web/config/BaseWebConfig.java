
package ren.hankai.cordwood.web.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ren.hankai.cordwood.core.convert.StringToDateConverter;

import java.nio.charset.Charset;

/**
 * web 配置基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:53:27 PM
 */
public abstract class BaseWebConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addFormatters(final FormatterRegistry registry) {
    registry.addConverter(new StringToDateConverter(null));
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
  public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(final ObjectMapper om) {
    final MappingJackson2HttpMessageConverter cvt = new MappingJackson2HttpMessageConverter(om);
    cvt.setDefaultCharset(Charset.forName("UTF-8"));
    return cvt;
  }

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
    om.setSerializationInclusion(Include.NON_NULL);
    return om;
  }
}
