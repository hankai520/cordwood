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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.BaseWebConfig;
import ren.hankai.cordwood.web.pfms.StabilizationInterceptor;

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

  @Autowired
  private StabilizationInterceptor stabilizationInterceptor;

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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(stabilizationInterceptor).addPathPatterns("/**");
  }

}
