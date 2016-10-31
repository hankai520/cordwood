package ren.hankai.cordwood.plugin.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.plugin.PluginResolver;
import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.Secure;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

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

  @Override
  public PluginPackage resolvePackage(URL packageUrl) {
    InputStream is = null;
    try {
      final PluginPackage pluginPackage = new PluginPackage();
      pluginPackage.setFileName(FilenameUtils.getName(packageUrl.getPath()));
      pluginPackage.setInstallUrl(packageUrl);
      is = packageUrl.openStream();
      pluginPackage.setIdentifier(DigestUtils.sha1Hex(is));
      return pluginPackage;
    } catch (final IOException e) {
      logger.error(String.format("Failed to calculate the checksum of package \"%s\"", packageUrl),
          e);
    } finally {
      IOUtils.closeQuietly(is);
    }
    return null;
  }

  @Override
  public Plugin resolvePlugin(Object pluginInstance) {
    final Class<?> clazz = pluginInstance.getClass();
    final Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
    final Plugin plugin = new Plugin();
    plugin.setName(pluggable.name());
    plugin.setVersion(pluggable.version());
    plugin.setDescription(pluggable.description());
    plugin.setInstance(pluginInstance);
    plugin.setActive(true);
    // 扫描插件标记的功能
    final Secure pluginSecure = clazz.getAnnotation(Secure.class);
    ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {

      @Override
      public void doWith(final Method method)
          throws IllegalArgumentException, IllegalAccessException {
        final Functional functional = AnnotationUtils.getAnnotation(method, Functional.class);
        if (functional != null) {
          final PluginFunction function = new PluginFunction();
          function.setMethod(method);
          if (!StringUtils.isEmpty(functional.name())) {
            function.setName(functional.name());
          } else {
            function.setName(method.getName());
          }
          function.setResultType(functional.resultType());
          Secure secure = AnnotationUtils.getAnnotation(method, Secure.class);
          if (secure == null) {
            secure = pluginSecure;
          }
          if (secure != null) {
            function.setCheckAccessToken(secure.checkAccessToken());
            function.setCheckInboundParameters(secure.checkParameterIntegrity());
          }
          function.setParameters(method.getParameters());
          plugin.getFunctions().put(functional.name(), function);
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
