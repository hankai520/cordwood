package ren.hankai.cordwood.plugin.impl;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.TokenInfo;
import ren.hankai.cordwood.core.exception.AccessTokenException;
import ren.hankai.cordwood.core.exception.ParameterIntegrityException;
import ren.hankai.cordwood.core.exception.PluginException;
import ren.hankai.cordwood.core.exception.PluginFunctionNotFoundException;
import ren.hankai.cordwood.core.exception.PluginNotFoundException;
import ren.hankai.cordwood.core.exception.PluginResourceNotFoundException;
import ren.hankai.cordwood.core.exception.PluginStatusException;
import ren.hankai.cordwood.core.util.PathUtil;
import ren.hankai.cordwood.core.util.SecurityUtil;
import ren.hankai.cordwood.plugin.ParameterMapper;
import ren.hankai.cordwood.plugin.PluginDriver;
import ren.hankai.cordwood.plugin.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认插件驱动器实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:46:57 AM
 */
@Component
public class DefaultPluginDriver implements PluginDriver {

  private static final Logger logger = LoggerFactory.getLogger(DefaultPluginDriver.class);

  @Autowired
  private ParameterMapper parameterMapper;
  @Autowired
  private PluginManager pluginManager;

  @Override
  public Object handleRequest(String pluginName, String functionName, HttpServletRequest request,
      HttpServletResponse response)
      throws ParameterIntegrityException, AccessTokenException, PluginStatusException,
      PluginNotFoundException, PluginFunctionNotFoundException, PluginException {
    Plugin plugin = pluginManager.getPlugin(pluginName);
    if (plugin == null) {
      throw new PluginNotFoundException(String.format("Plugin \"%s\" not found!", pluginName));
    } else if (!plugin.isActive()) {
      throw new PluginStatusException(
          String.format("Plugin has been \"%s\" disabled!", pluginName));
    } else {
      PluginFunction function = plugin.getFunctions().get(functionName);
      if (function == null) {
        throw new PluginFunctionNotFoundException(
            String.format("Plugin function \"%s\" not found!", functionName));
      }
      if (function.isCheckInboundParameters()
          && !SecurityUtil.verifyParameters(request.getParameterMap())) {
        ParameterIntegrityException ex = new ParameterIntegrityException(
            "Failed to complete plugin request due to invalid parameter signature!");
        logger.warn(ex.getMessage());
        throw ex;
      } else if (function.isCheckAccessToken()) {
        String rawToken = request.getParameter(Preferences.API_ACCESS_TOKEN);
        int result = SecurityUtil.verifyToken(rawToken);
        if (result == TokenInfo.TOKEN_ERROR_INVALID) {
          AccessTokenException ex =
              new AccessTokenException(String.format("Access token is invalid: %s", rawToken));
          logger.warn(ex.getMessage());
          throw ex;
        } else if (result == TokenInfo.TOKEN_ERROR_EXPIRED) {
          AccessTokenException ex =
              new AccessTokenException(String.format("Access token is expired: %s", rawToken));
          logger.warn(ex.getMessage());
          throw ex;
        }
      }
      Object result = null;
      try {
        Object[] args = parameterMapper.mapParameters(function, request, response);
        result = function.getMethod().invoke(plugin.getInstance(), args);
      } catch (Exception e) {
        throw new PluginException("Plugin function invokation failure.", e);
      }
      return result;
    }
  }

  @Override
  public InputStream getResource(String pluginName, HttpServletRequest request)
      throws PluginNotFoundException, PluginStatusException, PluginResourceNotFoundException {
    String resourcePath = PathUtil.parseResourcePath(request.getRequestURI());
    Plugin plugin = pluginManager.getPlugin(pluginName);
    if (plugin == null) {
      throw new PluginNotFoundException(String.format("Plugin \"%s\" not found!", pluginName));
    } else if (!plugin.isActive()) {
      throw new PluginStatusException(
          String.format("Plugin has been \"%s\" disabled!", pluginName));
    } else if (!(plugin.getInstance() instanceof PluginResourceLoader)) {
      PluginResourceNotFoundException ex =
          new PluginResourceNotFoundException("Plugin does not provide any resource!");
      logger.error(ex.getMessage());
      throw ex;
    } else {
      PluginResourceLoader resourceLoader = (PluginResourceLoader) plugin.getInstance();
      InputStream is = resourceLoader.getResource(resourcePath);
      if (is == null) {
        PluginResourceNotFoundException ex = new PluginResourceNotFoundException(
            String.format("Plugin resource \"%s\" not found!", resourcePath));
        logger.error(ex.getMessage());
        throw ex;
      }
      return is;
    }
  }

}
