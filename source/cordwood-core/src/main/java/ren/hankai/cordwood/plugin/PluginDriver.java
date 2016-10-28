package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.core.exception.AccessTokenException;
import ren.hankai.cordwood.core.exception.ParameterIntegrityException;
import ren.hankai.cordwood.core.exception.PluginException;
import ren.hankai.cordwood.core.exception.PluginFunctionNotFoundException;
import ren.hankai.cordwood.core.exception.PluginNotFoundException;
import ren.hankai.cordwood.core.exception.PluginResourceNotFoundException;
import ren.hankai.cordwood.core.exception.PluginStatusException;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件驱动，用于驱使插件完成特定功能。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:43:49 AM
 */
public interface PluginDriver {

  /**
   * 处理 HTTP 请求。
   *
   * @param pluginName 负责处理请求的插件名
   * @param functionName 请求访问的功能名
   * @param request 请求
   * @param response 响应
   * @return 处理结果
   * @throws ParameterIntegrityException 参数一致性异常
   * @throws AccessTokenException 令牌异常
   * @throws PluginStatusException 插件状态异常
   * @throws PluginNotFoundException 插件未找到
   * @throws PluginFunctionNotFoundException 插件功能未找到
   * @throws PluginException 未知插件异常
   * @author hankai
   * @since Oct 28, 2016 9:56:35 AM
   */
  Object handleRequest(String pluginName, String functionName, HttpServletRequest request,
      HttpServletResponse response)
      throws ParameterIntegrityException, AccessTokenException, PluginStatusException,
      PluginNotFoundException, PluginFunctionNotFoundException, PluginException;

  /**
   * 获取插件资源。
   *
   * @param pluginName 插件名
   * @param request 请求
   * @return 插件资源输入流
   * @throws PluginNotFoundException 插件未找到
   * @throws PluginStatusException 插件状态异常
   * @throws PluginResourceNotFoundException 插件资源未找到
   * @author hankai
   * @since Oct 28, 2016 11:05:26 AM
   */
  InputStream getResource(String pluginName, HttpServletRequest request)
      throws PluginNotFoundException, PluginStatusException, PluginResourceNotFoundException;
}
