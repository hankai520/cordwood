package ren.hankai.cordwood.plugin.advance.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;

/**
 * 插件容器（用于自启动插件）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 26, 2016 9:36:52 AM
 */
@Controller
@EnableAutoConfiguration
@Import(PluginBootstrap.class)
public class PluginContainer extends EmbeddedPluginContainer {

  private static final Logger logger = LoggerFactory.getLogger(PluginContainer.class);

  @Autowired
  private AdvancePlugin advancePlugin;

  @PostConstruct
  private void registerPlugins() {
    register(advancePlugin);
  }

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
      Object result = handleRequest(pluginName, functionName, request, response);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      logger.error(String.format("Failed to call plugin { %s#%s }", pluginName, functionName), e);
    } catch (Error e) {
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
      InputStream is = getResource(pluginName, request);
      if (is != null) {
        return new ResponseEntity<>(new InputStreamResource(is), HttpStatus.OK);
      }
    } catch (Exception e) {
      logger.error("Resource not found!", e);
    } catch (Error e) {
      logger.error("Resource not found!", e);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static void main(String[] args) throws Exception {
    System.setProperty("server.port", "8000");
    // System.setProperty("debug", "true");
    SpringApplication.run(PluginContainer.class, args);
  }
}
