/**
 *
 */
package ren.hankai.cordwood.plugin.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 嵌入式插件容器，用于脱离 cordwood 控制台，单独启动插件。提供基础的基于插件实例，而不是插件包的注册机制。
 * 此内嵌容器是为插件调试而设计的，仅用于快速验证插件功能和调试bug，因此，请不要直接在产品环境直接使用此种模式启动插件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 29, 2016 7:39:20 PM
 *
 */
public class EmbeddedPluginContainer {

  private static final Logger logger = LoggerFactory.getLogger(EmbeddedPluginContainer.class);
  private static final Map<String, Plugin> plugins = new HashMap<>();

  protected final boolean register(Object pluginInstance) {
    final Class<?> clazz = pluginInstance.getClass();
    Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
    if (pluggable == null) {
      logger.error(
          "Failed to register plugin due to invalid instance! Plugin instance class must be marked with @Pluggable.");
      return false;
    }
    Plugin plugin = new Plugin();
    plugin.setName(pluggable.name());
    plugin.setInstance(pluginInstance);
    Method[] methods = clazz.getMethods();
    Functional functional = null;
    PluginFunction function = null;
    for (Method method : methods) {
      functional = method.getAnnotation(Functional.class);
      if (functional != null) {
        function = new PluginFunction();
        function.setMethod(method);
        if (StringUtils.isNotEmpty(functional.name())) {
          function.setName(functional.name());
        } else {
          function.setName(method.getName());
        }
        plugin.getFunctions().put(function.getName(), function);
      }
    }
    plugins.put(plugin.getName(), plugin);
    if (pluginInstance instanceof PluginLifeCycleAware) {
      ((PluginLifeCycleAware) pluginInstance).pluginDidLoad();
    }
    return true;
  }

  protected final Object handleRequest(String pluginName, String functionName,
      HttpServletRequest request, HttpServletResponse response) {
    Plugin plugin = plugins.get(pluginName);
    if (plugin == null) {
      logger.error(String.format("Plugin \"%s\" not found!", pluginName));
      return null;
    }
    PluginFunction function = plugin.getFunctions().get(functionName);
    if (function == null) {
      logger.error(String.format("Plugin function \"%s\" not found!", functionName));
      return null;
    }
    ConversionService cs = new DefaultConversionService();
    List<Object> args = new ArrayList<>();
    Parameter[] params = function.getMethod().getParameters();
    if ((params != null) && (params.length > 0)) {
      for (Parameter param : params) {
        if (param.getType().isAssignableFrom(HttpServletRequest.class)) {
          args.add(request);
        } else if (param.getType().isAssignableFrom(HttpServletResponse.class)) {
          args.add(response);
        } else if (param.getType().isAssignableFrom(String[].class)) {
          args.add(request.getParameterValues(param.getName()));
        } else {
          String str = request.getParameter(param.getName());
          if (cs.canConvert(String.class, param.getType())) {
            Object convertedParam = cs.convert(str, param.getType());
            args.add(convertedParam);
          }
        }
      }
    }
    try {
      return function.getMethod().invoke(plugin.getInstance(), args.toArray());
    } catch (Exception e) {
      logger.error(String.format("Failed to call \"%s -> %s\"", pluginName, functionName), e);
    }
    return null;
  }

  protected final InputStream getResource(String pluginName, HttpServletRequest request) {
    String resourcePath = PathUtil.parseResourcePath(request.getRequestURI());
    Plugin plugin = plugins.get(pluginName);
    if (plugin == null) {
      logger.error(String.format("Plugin \"%s\" not found!", pluginName));
      return null;
    } else if (!(plugin.getInstance() instanceof PluginResourceLoader)) {
      logger.error("Plugin does not provide any resource!");
      return null;
    } else {
      PluginResourceLoader resourceLoader = (PluginResourceLoader) plugin.getInstance();
      InputStream is = resourceLoader.getResource(resourcePath);
      if (is == null) {
        logger.error(String.format("Plugin resource \"%s\" not found!", resourcePath));
        return null;
      }
      return is;
    }
  }

  private static class Plugin {
    private String name;
    private Object instance;
    private Map<String, PluginFunction> functions = new HashMap<>();

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Object getInstance() {
      return instance;
    }

    public void setInstance(Object instance) {
      this.instance = instance;
    }

    public Map<String, PluginFunction> getFunctions() {
      return functions;
    }
  }

  private static class PluginFunction {
    private String name;
    private Method method;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Method getMethod() {
      return method;
    }

    public void setMethod(Method method) {
      this.method = method;
    }

  }
}
