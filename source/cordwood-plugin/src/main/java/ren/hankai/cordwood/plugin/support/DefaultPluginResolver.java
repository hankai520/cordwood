package ren.hankai.cordwood.plugin.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import ren.hankai.cordwood.plugin.FunctionParameter;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.api.PluginResolver;
import ren.hankai.cordwood.plugin.api.annotation.Functional;
import ren.hankai.cordwood.plugin.api.annotation.Pluggable;
import ren.hankai.cordwood.plugin.api.annotation.Secure;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认插件解析器实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:18:45 AM
 */
@Component
public class DefaultPluginResolver implements PluginResolver {

  private static final Logger logger = LoggerFactory.getLogger(DefaultPluginResolver.class);

  private void resolveParameters(PluginFunction function, Plugin plugin) {
    final List<FunctionParameter> params = new ArrayList<>();
    FunctionParameter fp = null;
    for (final Parameter param : function.getMethod().getParameters()) {
      fp = new FunctionParameter(plugin, function, param);
      params.add(fp);
    }
    final FunctionParameter[] array = new FunctionParameter[params.size()];
    function.setParameters(params.toArray(array));
    plugin.getFunctions().put(function.getName(), function);
  }

  private void resolvePluginFunction(Plugin plugin, Secure pluginSecure, Functional functional,
      Method method) {
    final PluginFunction function = new PluginFunction(plugin, functional, method);
    Secure secure = AnnotationUtils.getAnnotation(method, Secure.class);
    if (secure == null) {
      secure = pluginSecure;
    }
    if (secure != null) {
      function.setCheckAccessToken(secure.checkAccessToken());
      function.setCheckInboundParameters(secure.checkParameterIntegrity());
    }
    resolveParameters(function, plugin);
  }

  @Override
  public Plugin resolvePlugin(Object pluginInstance) {
    final Class<?> clazz = AopUtils.getTargetClass(pluginInstance);
    final Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
    final Plugin plugin = new Plugin(pluggable.name(), pluginInstance);
    plugin.setVersion(pluggable.version());
    plugin.setActive(true);
    final Secure pluginSecure = clazz.getAnnotation(Secure.class);
    ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
      @Override
      public void doWith(final Method method)
          throws IllegalArgumentException, IllegalAccessException {
        final Functional functional = AnnotationUtils.getAnnotation(method, Functional.class);
        if (functional != null) {
          resolvePluginFunction(plugin, pluginSecure, functional, method);
        }
      }
    }, new ReflectionUtils.MethodFilter() {

      @Override
      public boolean matches(final Method method) {
        return (method.getDeclaringClass() == clazz);
      }
    });
    return plugin;
  }

}
