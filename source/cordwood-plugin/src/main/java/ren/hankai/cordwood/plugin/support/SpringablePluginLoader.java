
package ren.hankai.cordwood.plugin.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginLoader;
import ren.hankai.cordwood.plugin.api.annotation.Pluggable;

import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * 默认插件加载器，支持基于spring框架的插件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 8:55:55 AM
 */
@Component
public class SpringablePluginLoader implements PluginLoader {

  private static final Logger logger = LoggerFactory.getLogger(SpringablePluginLoader.class);
  @Autowired
  private AbstractApplicationContext context;
  private final Map<String, GenericApplicationContext> plugins = new HashMap<>();
  /**
   * 共享的类加载器，用于加载插件所依赖的包。
   */
  private static ClassLoader sharedClassLoader = null;

  @PostConstruct
  private void internalInit() {
    if (sharedClassLoader == null) {
      final URL[] urls = Preferences.getLibUrls();
      if ((urls != null) && (urls.length != 0)) {
        sharedClassLoader = new PluginDependenciesClassLoader(urls, context.getClassLoader());
      } else {
        sharedClassLoader = context.getClassLoader();
      }
    }
  }

  /**
   * 加载插件包的 spring 上下文。
   *
   * @param attrs 插件包 manifest 属性
   * @param loader 插件包类加载器
   * @return 基于注解的 spring 上下文
   * @author hankai
   * @since Oct 13, 2016 1:16:18 PM
   */
  private AnnotationConfigApplicationContext loadSpringContext(PluginPackage pluginPackage) {
    final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.setParent(context);
    ctx.setClassLoader(pluginPackage.getClassLoader());
    final List<Class<?>> configClasses = new ArrayList<>();
    if (!StringUtils.isEmpty(pluginPackage.getConfigClass())) {
      try {
        final Class<?> bootClass =
            Class.forName(pluginPackage.getConfigClass(), true, pluginPackage.getClassLoader());
        if (bootClass != null) {
          configClasses.add(bootClass);
        }
      } catch (final ClassNotFoundException e) {
        logger.error("Plugin config class not found!", e);
      }
    }
    configClasses.add(PluginCacheConfig.class);
    final Class<?>[] classes = new Class<?>[configClasses.size()];
    configClasses.toArray(classes);
    ctx.register(classes);
    ctx.refresh();
    return ctx;
  }

  /**
   * 搜索基包中的插件类。
   *
   * @param basePackage 基包
   * @return 插件类集合
   * @author hankai
   * @since Nov 9, 2016 7:16:01 PM
   */
  private List<Object> instantiatePluginClasses(String basePackage, GenericApplicationContext ctx) {
    final List<Object> instances = new ArrayList<>();
    final ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
    final MetadataReaderFactory mrf = new CachingMetadataReaderFactory(rpr);
    String pack = SystemPropertyUtils.resolvePlaceholders(basePackage);
    pack = ClassUtils.convertClassNameToResourcePath(pack);
    final String searchPath =
        ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + pack + "/" + "**/*.class";
    try {
      final Resource[] resources = rpr.getResources(searchPath);
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      for (final Resource resource : resources) {
        if (resource.isReadable() && (resource instanceof UrlResource)) {
          final MetadataReader metadataReader = mrf.getMetadataReader(resource);
          final Class<?> clazz = cl.loadClass(metadataReader.getClassMetadata().getClassName());
          final Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
          if ((pluggable != null) && (Modifier.PUBLIC == clazz.getModifiers())) {
            String beanName = ClassUtils.getShortName(clazz.getName());
            beanName = Introspector.decapitalize(beanName);
            if (!ctx.containsBeanDefinition(beanName)) {
              final GenericBeanDefinition gb = new GenericBeanDefinition();
              gb.setBeanClass(clazz);
              gb.setAutowireCandidate(true);
              gb.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
              ctx.registerBeanDefinition(beanName, gb);
            }
            final Object obj = ctx.getBean(beanName);
            if (obj == null) {
              logger.error(
                  String.format("Could not instantiate plugin class \"%s\"", clazz.getName()));
            }
            plugins.put(pluggable.name(), ctx);
            instances.add(obj);
          }
        }
      }
    } catch (final Exception e) {
      throw new RuntimeException(
          String.format("Failed to instantiate plugin in base package \"%s\"", basePackage), e);
    }
    return instances;
  }

  /**
   * 加载插件包中的插件实例。
   *
   * @param loader 插件包类加载器
   * @return 插件实例集合
   * @author hankai
   * @since Oct 13, 2016 1:16:57 PM
   */
  private List<Object> loadPluginInstances(PluginPackage pluginPackage) {
    final List<Object> instances = new ArrayList<>();
    // 尝试根据插件包属性构建 spring 上下文
    final AnnotationConfigApplicationContext ctx = loadSpringContext(pluginPackage);
    // 遍历基包来扫描插件
    for (final String packageName : pluginPackage.getBasePackages()) {
      final List<Object> objs = instantiatePluginClasses(packageName, ctx);
      instances.addAll(objs);
    }
    return instances;
  }

  @Override
  public List<Object> loadPlugins(PluginPackage pluginPackage) {
    final URL[] urls = new URL[] {pluginPackage.getInstallationUrl()};
    pluginPackage.setClassLoader(new URLClassLoader(urls, sharedClassLoader));
    /*
     * 设置线程的上下文类加载器，以便在运行时能动态加载插件需要的依赖包，不同插件不共享类加载器。
     */
    Thread.currentThread().setContextClassLoader(pluginPackage.getClassLoader());
    return loadPluginInstances(pluginPackage);
  }

  @Override
  public boolean unloadPlugin(Object instance) {
    final Class<?> targetClass = AopUtils.getTargetClass(instance);
    final Pluggable pluggable = targetClass.getAnnotation(Pluggable.class);
    final GenericApplicationContext ctx = plugins.get(pluggable.name());
    if (ctx == null) {
      return true;
    }
    try {
      plugins.remove(pluggable.name());
      final BeanDefinitionRegistry registry = ctx;
      final String[] names = ctx.getBeanNamesForType(targetClass);
      if ((names != null) && (names.length > 0)) {
        for (final String beanName : names) {
          registry.removeBeanDefinition(beanName);
        }
      }
      final Map<String, Object> beans = ctx.getBeansWithAnnotation(Pluggable.class);
      if ((beans == null) || (beans.size() == 0)) {
        ctx.close();
      }
      return true;
    } catch (final NoSuchBeanDefinitionException e) {
      logger.warn("No plugin bean definition was found in spring context!", e);
    } catch (final BeansException e) {
      logger.warn("Failed to remove plugin bean from spring context!", e);
    }
    return false;
  }

  @EnableCaching(mode = AdviceMode.ASPECTJ)
  @EnableAspectJAutoProxy(proxyTargetClass = true)
  public static class PluginCacheConfig {
  }
}
