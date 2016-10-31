package ren.hankai.cordwood.plugin.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.api.ParameterMapper;
import ren.hankai.cordwood.plugin.api.PluginDriver;
import ren.hankai.cordwood.plugin.api.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;
import ren.hankai.cordwood.plugin.exception.ParameterIntegrityException;
import ren.hankai.cordwood.plugin.exception.PluginException;
import ren.hankai.cordwood.plugin.exception.PluginFunctionNotFoundException;
import ren.hankai.cordwood.plugin.exception.PluginNotFoundException;
import ren.hankai.cordwood.plugin.exception.PluginResourceNotFoundException;
import ren.hankai.cordwood.plugin.exception.PluginStatusException;
import ren.hankai.cordwood.plugin.util.PathUtil;
import ren.hankai.cordwood.web.security.AccessAuthenticator;
import ren.hankai.cordwood.web.security.AccessAuthenticator.TokenInfo;
import ren.hankai.cordwood.web.security.RequestInspector;
import ren.hankai.cordwood.web.security.exception.AccessTokenException;

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
  @Autowired
  private RequestInspector requestInspector;
  @Autowired
  private AccessAuthenticator accessAuthenticator;

  @Override
  public Object handleRequest(String pluginName, String functionName, HttpServletRequest request,
      HttpServletResponse response)
      throws ParameterIntegrityException, AccessTokenException, PluginStatusException,
      PluginNotFoundException, PluginFunctionNotFoundException, PluginException {
    final Plugin plugin = pluginManager.getPlugin(pluginName);
    if (plugin == null) {
      throw new PluginNotFoundException(String.format("Plugin \"%s\" not found!", pluginName));
    } else if (!plugin.isActive()) {
      throw new PluginStatusException(
          String.format("Plugin has been \"%s\" disabled!", pluginName));
    } else {
      final PluginFunction function = plugin.getFunctions().get(functionName);
      if (function == null) {
        throw new PluginFunctionNotFoundException(
            String.format("Plugin function \"%s\" not found!", functionName));
      }
      if (function.isCheckInboundParameters()
          && !requestInspector.verifyRequestParameters(request.getParameterMap())) {
        final ParameterIntegrityException ex = new ParameterIntegrityException(
            "Failed to complete plugin request due to invalid parameter signature!");
        logger.warn(ex.getMessage());
        throw ex;
      } else if (function.isCheckAccessToken()) {
        final String tokenString = request.getParameter(AccessAuthenticator.ACCESS_TOKEN);
        final int result = accessAuthenticator.verifyAccessToken(tokenString);
        if (result == TokenInfo.TOKEN_ERROR_INVALID) {
          final AccessTokenException ex =
              new AccessTokenException(String.format("Access token is invalid: %s", tokenString));
          logger.warn(ex.getMessage());
          throw ex;
        } else if (result == TokenInfo.TOKEN_ERROR_EXPIRED) {
          final AccessTokenException ex =
              new AccessTokenException(String.format("Access token is expired: %s", tokenString));
          logger.warn(ex.getMessage());
          throw ex;
        }
      }
      Object result = null;
      try {
        final Object[] args = parameterMapper.mapParameters(function, request, response);
        result = function.getMethod().invoke(plugin.getInstance(), args);
      } catch (final Exception e) {
        throw new PluginException("Plugin function invokation failure.", e);
      }
      return result;
    }
  }

  @Override
  public InputStream getResource(String pluginName, HttpServletRequest request)
      throws PluginNotFoundException, PluginStatusException, PluginResourceNotFoundException {
    final String resourcePath = PathUtil.parseResourcePath(request.getRequestURI());
    final Plugin plugin = pluginManager.getPlugin(pluginName);
    if (plugin == null) {
      throw new PluginNotFoundException(String.format("Plugin \"%s\" not found!", pluginName));
    } else if (!plugin.isActive()) {
      throw new PluginStatusException(
          String.format("Plugin has been \"%s\" disabled!", pluginName));
    } else if (!(plugin.getInstance() instanceof PluginResourceLoader)) {
      final PluginResourceNotFoundException ex =
          new PluginResourceNotFoundException("Plugin does not provide any resource!");
      logger.error(ex.getMessage());
      throw ex;
    } else {
      final PluginResourceLoader resourceLoader = (PluginResourceLoader) plugin.getInstance();
      final InputStream is = resourceLoader.getResource(resourcePath);
      if (is == null) {
        final PluginResourceNotFoundException ex = new PluginResourceNotFoundException(
            String.format("Plugin resource \"%s\" not found!", resourcePath));
        logger.error(ex.getMessage());
        throw ex;
      }
      return is;
    }
  }

}
