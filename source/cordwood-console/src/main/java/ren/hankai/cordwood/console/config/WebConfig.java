package ren.hankai.cordwood.console.config;

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
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import ren.hankai.cordwood.console.interceptor.PluginRequestInterceptor;
import ren.hankai.cordwood.plugin.api.annotation.Pluggable;
import ren.hankai.cordwood.web.breadcrumb.BreadCrumbInterceptor;

import java.nio.charset.Charset;
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

  /**
   * 用于在页面渲染前传递页面级提示消息。
   */
  public static final String WEB_PAGE_MESSAGE = "pageMessage";
  /**
   * 用于在页面渲染前传递页面级错误。
   */
  public static final String WEB_PAGE_ERROR = "pageError";

  @Autowired
  private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
  @Autowired
  private BreadCrumbInterceptor breadCrumbInterceptor;
  @Autowired
  private PluginRequestInterceptor pluginRequestInterceptor;

  /**
   * HTML 模板解析器。
   *
   * @param applicationContext spring 上下文
   * @return 模板解析器
   * @author hankai
   * @since Nov 8, 2016 8:44:56 AM
   */
  @Bean
  public ITemplateResolver htmlTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("classpath:/templates/html/");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  /**
   * JS 模板解析器。
   *
   * @param applicationContext spring 上下文
   * @return 模板解析器
   * @author hankai
   * @since Nov 8, 2016 8:45:24 AM
   */
  @Bean
  public ITemplateResolver jsTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("classpath:/templates/js/");
    templateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  /**
   * CSS 模板解析器。
   *
   * @param applicationContext spring 上下文
   * @return 模板解析器
   * @author hankai
   * @since Nov 8, 2016 8:45:38 AM
   */
  @Bean
  public ITemplateResolver cssTemplateResolver(ApplicationContext applicationContext) {
    final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("classpath:/templates/css/");
    templateResolver.setTemplateMode(TemplateMode.CSS);
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  /**
   * 模板引擎。
   *
   * @param htmlTemplateResolver HTML模板解析器
   * @param jsTemplateResolver JS模板解析器
   * @param cssTemplateResolver CSS模板解析器
   * @return 模板引擎
   * @author hankai
   * @since Nov 8, 2016 8:45:51 AM
   */
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

  /**
   * Thymeleaf 视图解析器。
   *
   * @param templateEngine 模板引擎
   * @return 视图解析器
   * @author hankai
   * @since Nov 8, 2016 8:46:30 AM
   */
  @Bean
  public ViewResolver viewResolver(TemplateEngine templateEngine) {
    final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine);
    viewResolver.setOrder(1);
    viewResolver.setViewNames(new String[] { "*.html", "*.js", "*.css" });
    viewResolver.setCharacterEncoding("UTF-8");
    return viewResolver;
  }

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
    registry.addViewController(Route.BG_LOGIN).setViewName("admin_login.html");
    registry.addViewController(Route.FOREGROUND_PREFIX).setViewName("home_index.html");
    registry.addViewController("/").setViewName("home_index.html");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(breadCrumbInterceptor).addPathPatterns(Route.BACKGROUND_PREFIX + "/**");
    registry.addInterceptor(pluginRequestInterceptor)
        .addPathPatterns(Pluggable.PLUGIN_BASE_URL + "/**");
  }

}
