/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package ren.hankai.cordwood.core.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    om.setSerializationInclusion(Include.NON_NULL);
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
    ms.setCacheSeconds(30);
    ms.setFallbackToSystemLocale(false);
    ms.setUseCodeAsDefaultMessage(true);
    return ms;
  }
}
