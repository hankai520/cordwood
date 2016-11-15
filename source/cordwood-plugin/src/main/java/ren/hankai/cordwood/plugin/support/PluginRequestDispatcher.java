package ren.hankai.cordwood.plugin.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ren.hankai.cordwood.plugin.api.PluginDriver;
import ren.hankai.cordwood.plugin.api.annotation.Pluggable;
import ren.hankai.cordwood.plugin.exception.ParameterIntegrityException;
import ren.hankai.cordwood.plugin.exception.PluginException;
import ren.hankai.cordwood.plugin.exception.PluginNotFoundException;
import ren.hankai.cordwood.plugin.exception.PluginResourceNotFoundException;
import ren.hankai.cordwood.plugin.exception.PluginStatusException;
import ren.hankai.cordwood.web.security.exception.AccessTokenException;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件请求分发器基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 29, 2016 7:39:20 PM
 *
 */
public abstract class PluginRequestDispatcher {

  private static final Logger logger = LoggerFactory.getLogger(PluginRequestDispatcher.class);
  @Autowired
  protected PluginDriver pluginDriver;

  /**
   * 将插件以单机形式启动。
   */
  public static final String PROFILE_STANDALONE_MODE = "standalone-plugin-mode";

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
  @RequestMapping(value = Pluggable.PLUGIN_BASE_URL + "/{plugin_name}/{function_name}")
  @ResponseBody
  public ResponseEntity<Object> dispatchPluginRequest(
      @PathVariable("plugin_name") String pluginName,
      @PathVariable("function_name") String functionName, HttpServletRequest request,
      HttpServletResponse response) {
    Object result = null;
    try {
      result = pluginDriver.handleRequest(pluginName, functionName, request, response);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (final ParameterIntegrityException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (final AccessTokenException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (final PluginStatusException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    } catch (final PluginNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (final PluginException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Exception e) {
      logger.error("Unkown error occurred while invoking plugin function.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
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
      final InputStream is = pluginDriver.getResource(pluginName, request);
      return new ResponseEntity<>(new InputStreamResource(is), HttpStatus.OK);
    } catch (final PluginNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (final PluginStatusException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    } catch (final PluginResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (final Exception e) {
      logger.error("Unkown error occurred while loading plugin resource.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error("Unkown error occurred while loading plugin resource.", e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
