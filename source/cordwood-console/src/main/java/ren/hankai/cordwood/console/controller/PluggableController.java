package ren.hankai.cordwood.console.controller;

import ren.hankai.cordwood.core.exception.AccessTokenException;
import ren.hankai.cordwood.core.exception.ParameterIntegrityException;
import ren.hankai.cordwood.core.exception.PluginException;
import ren.hankai.cordwood.core.exception.PluginNotFoundException;
import ren.hankai.cordwood.core.exception.PluginResourceNotFoundException;
import ren.hankai.cordwood.core.exception.PluginStatusException;
import ren.hankai.cordwood.plugin.PluginDriver;
import ren.hankai.cordwood.plugin.api.Pluggable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 插件访问分发控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:28:48 PM
 */
@Controller
public class PluggableController {

  private static final Logger logger = LoggerFactory.getLogger(PluggableController.class);

  @Autowired
  private PluginDriver pluginDriver;

  /**
   * 分发插件访问请求。
   *
   * @param pluginName 插件名
   * @param functionName 插件功能
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 插件功能返回内容
   * @author hankai
   * @since Oct 25, 2016 10:54:24 AM
   */
  @RequestMapping(value = Pluggable.PLUGIN_BASE_URL + "/{plugin_name}/{function}")
  @ResponseBody
  public ResponseEntity<Object> dispatchPluginRequest(
      @PathVariable("plugin_name") String pluginName,
      @PathVariable("functionName") String functionName, HttpServletRequest request,
      HttpServletResponse response) {
    Object result = null;
    try {
      result = pluginDriver.handleRequest(pluginName, functionName, request, response);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (ParameterIntegrityException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (AccessTokenException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (PluginStatusException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    } catch (PluginNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (PluginException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      logger.error("Unkown error occurred while invoking plugin function.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Error e) {
      logger.error("Unkown error occurred while invoking plugin function.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
      InputStream is = pluginDriver.getResource(pluginName, request);
      return new ResponseEntity<>(new InputStreamResource(is), HttpStatus.OK);
    } catch (PluginNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (PluginStatusException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    } catch (PluginResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      logger.error("Unkown error occurred while loading plugin resource.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Error e) {
      logger.error("Unkown error occurred while loading plugin resource.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
