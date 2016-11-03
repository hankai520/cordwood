/**
 *
 */
package ren.hankai.cordwood.console.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Web 配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 3:19:49 PM
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Bean
  public ITemplateResolver htmlTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/templates/html/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean
  public ITemplateResolver jsTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/templates/js/");
    templateResolver.setSuffix(".js");
    templateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean
  public ITemplateResolver cssTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/templates/css/");
    templateResolver.setSuffix(".css");
    templateResolver.setTemplateMode(TemplateMode.CSS);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean
  public TemplateEngine templateEngine(
      @Autowired @Qualifier("htmlTemplateResolver") ITemplateResolver htmlTemplateResolver,
      @Autowired @Qualifier("jsTemplateResolver") ITemplateResolver jsTemplateResolver,
      @Autowired @Qualifier("cssTemplateResolver") ITemplateResolver cssTemplateResolver) {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    final Set<ITemplateResolver> resolvers = new HashSet<>();
    resolvers.add(htmlTemplateResolver);
    resolvers.add(jsTemplateResolver);
    resolvers.add(cssTemplateResolver);
    templateEngine.setTemplateResolvers(resolvers);
    templateEngine.setEnableSpringELCompiler(true);
    return templateEngine;
  }

  @Bean
  public ThymeleafViewResolver viewResolver(TemplateEngine templateEngine) {
    final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine);
    viewResolver.setOrder(1);
    viewResolver.setViewNames(new String[] {"*.html", "*.js", "*.css"});
    return viewResolver;
  }

  @Bean
  public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(ObjectMapper om) {
    return new MappingJackson2HttpMessageConverter(om);
  }

  @Autowired
  private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new ByteArrayHttpMessageConverter());
    converters.add(new StringHttpMessageConverter());
    converters.add(new ResourceHttpMessageConverter());
    converters.add(new SourceHttpMessageConverter<>());
    converters.add(new AllEncompassingFormHttpMessageConverter());
    converters.add(mappingJackson2HttpMessageConverter);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(Route.FG_LOGIN).setViewName("home/login");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }

}
