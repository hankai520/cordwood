package ren.hankai.cordwood.plugin.advance.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ren.hankai.cordwood.plugin.advance.AdvancePlugin;
import ren.hankai.cordwood.plugin.advance.config.PluginBootstrap;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.util.EmbeddedPluginContainer;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件容器（用于自启动插件），此嵌入式容器仅供调试使用。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 26, 2016 9:36:52 AM
 */
/*
 * 将此配置标记为仅在单机容器模式下载入，因为 @EnableAutoConfiguration 注解会尝试从类路径中包含的类来自动 配置需要的
 * bean，而这一过程所使用的类加载器并不是由插件容器提供的，因此无法加载到插件所依赖的 jar 包，因而会 导致类加载问题。
 */
@Profile("ignored")
@EnableAutoConfiguration
@Import(PluginBootstrap.class)
@Controller
public class PluginContainer extends EmbeddedPluginContainer {

  private static final Logger logger = LoggerFactory.getLogger(PluginContainer.class);

  /**
   * 要调试的插件类。
   */
  private static final Class<?>[] pluginClasses = new Class<?>[] {AdvancePlugin.class};
  /**
   * 嵌入式插件容器是否以调试模式启动。
   */
  private static final boolean enableDebug = false;
  /**
   * 嵌入式插件容器监听本地什么 TCP 端口。
   */
  private static final String serverPort = "8000";

  /**
   * 分发插件请求。
   *
   * @param pluginName 插件名称
   * @param functionName 功能名称
   * @param request 请求
   * @param response 响应
   * @return 响应内容
   * @author hankai
   * @since Oct 28, 2016 8:28:52 PM
   */
  @RequestMapping(value = Pluggable.PLUGIN_BASE_URL + "/{plugin_name}/{function_name}")
  @ResponseBody
  public ResponseEntity<Object> dispatchPluginRequest(
      @PathVariable("plugin_name") String pluginName,
      @PathVariable("function_name") String functionName, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      final Object result = handleRequest(pluginName, functionName, request, response);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (final Exception e) {
      logger.error(String.format("Failed to call plugin { %s#%s }", pluginName, functionName), e);
    } catch (final Error e) {
      logger.error(String.format("Failed to call plugin { %s#%s }", pluginName, functionName), e);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 分发插件资源访问请求。
   *
   * @param pluginName 插件名
   * @param request HTTP 请求
   * @return HTTP 响应内容
   * @author hankai
   * @since Oct 25, 2016 10:59:26 AM
   */
  @RequestMapping(value = Pluggable.PLUGIN_RESOURCE_BASE_URL + "/{plugin_name}/**")
  @ResponseBody
  public ResponseEntity<Object> dispatchPluginResourceRequest(
      @PathVariable("plugin_name") String pluginName, HttpServletRequest request) {
    try {
      final InputStream is = getResource(pluginName, request);
      if (is != null) {
        return new ResponseEntity<>(new InputStreamResource(is), HttpStatus.OK);
      }
    } catch (final Exception e) {
      logger.error("Resource not found!", e);
    } catch (final Error e) {
      logger.error("Resource not found!", e);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static void main(String[] args) throws Exception {
    System.setProperty("server.port", serverPort);
    if (enableDebug) {
      System.setProperty("debug", "true");
    }
    final SpringApplication app = new SpringApplication(PluginContainer.class);
    app.setAdditionalProfiles("ignored");

    final ApplicationContext context = app.run(args);
    for (final Class<?> clazz : pluginClasses) {
      register(context.getBean(clazz));
    }
  }
}
