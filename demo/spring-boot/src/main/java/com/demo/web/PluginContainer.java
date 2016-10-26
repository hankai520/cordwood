package com.demo.web;

import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.util.PathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件容器（用于自启动插件）
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 26, 2016 9:36:52 AM
 */
@Controller
@EnableAutoConfiguration
public class PluginContainer {

  private static final Logger logger = LoggerFactory.getLogger(PluginContainer.class);

  @RequestMapping(value = Pluggable.PLUGIN_BASE_URL + "/{plugin_name}/{function}")
  @ResponseBody
  public ResponseEntity<Object> dispatchPluginRequest(
      @PathVariable("plugin_name") String pluginName, @PathVariable("function") String function,
      HttpServletRequest request, HttpServletResponse response) {
    try {
      Object result = null;
      if (pluginName.equals(DemoWeb.NAME)) {
        DemoWeb dw = new DemoWeb();
        dw.pluginDidLoad();
        if (function.equals("hello")) {
          result = dw.sayHello(request, response);
        }
        return new ResponseEntity<Object>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      logger.error(String.format("Failed to call plugin { %s#%s }", pluginName, function), e);
    } catch (Error e) {
      logger.error(String.format("Failed to call plugin { %s#%s }", pluginName, function), e);
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
    String resourcePath = PathUtil.parseResourcePath(request.getRequestURI());
    try {
      if (pluginName.equals(DemoWeb.NAME)) {
        InputStream is = new DemoWeb().getResource(resourcePath);
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
    SpringApplication.run(PluginContainer.class, args);
  }
}
