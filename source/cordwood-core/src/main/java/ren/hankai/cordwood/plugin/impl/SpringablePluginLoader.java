
package ren.hankai.cordwood.plugin.impl;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.PluginLoader;
import ren.hankai.cordwood.plugin.api.Pluggable;

import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

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
  private ApplicationContext context;
  private final Map<String, GenericApplicationContext> plugins = new HashMap<>();
  /**
   * 共享的类加载器，用于加载插件所依赖的包。
   */
  private static ClassLoader sharedClassLoader = null;

  @PostConstruct
  private void internalInit() {
    if (sharedClassLoader == null) {
      URL[] urls = Preferences.getLibUrls();
      if ((urls != null) && (urls.length != 0)) {
        sharedClassLoader = new URLClassLoader(urls, context.getClassLoader());
      } else {
        sharedClassLoader = context.getClassLoader();
      }
    }
  }

  /**
   * 解析 jar 文件的 manifest。
   *
   * @param loader 类加载器
   * @return manifest 属性
   * @author hankai
   * @since Oct 13, 2016 1:15:51 PM
   */
  private Attributes parseJarManifest(URLClassLoader loader) {
    Attributes attrs = null;
    JarInputStream jarStream = null;
    URL url = null;
    try {
      URL[] urls = loader.getURLs();
      if ((urls != null) && (urls.length > 0)) {
        url = urls[0];
        InputStream is = url.openStream();
        jarStream = new JarInputStream(is);
        Manifest manifest = jarStream.getManifest();
        attrs = manifest.getMainAttributes();
      }
    } catch (IOException e) {
      logger.error(String.format("Failed to read manifest of plugin at urls: %s", url.toString()));
    } finally {
      IOUtils.closeQuietly(jarStream);
    }
    return attrs;
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
  private AnnotationConfigApplicationContext loadSpringContext(Attributes attrs,
      URLClassLoader loader) {
    if ((attrs != null) && !attrs.isEmpty()) {
      String bootClassName = attrs.getValue(PluginLoader.PLUGIN_SPRING_CONFIG_CLASS);
      if (!StringUtils.isEmpty(bootClassName)) {
        try {
          Class<?> bootClass = Class.forName(bootClassName, true, loader);
          if (bootClass != null) {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.setParent(context);
            ctx.setClassLoader(loader);
            ctx.register(bootClass);
            ctx.refresh();
            return ctx;
          }
        } catch (ClassNotFoundException e) {
          logger.error("Boot class not found in plugin!");
        }
      }
    }
    return null;
  }

  /**
   * 解析 jar 包中设置的插件基包。
   *
   * @param attrs jar 包的 manifest 属性
   * @return 基包数组
   * @author hankai
   * @since Oct 25, 2016 10:24:21 AM
   */
  private String[] resolvePluginBasePackages(Attributes attrs) {
    String str = attrs.getValue(PluginLoader.PLUGIN_BASE_PACKAGES);
    if (StringUtils.isEmpty(str)) {
      throw new RuntimeException("Invalid plugin package: no base packages found in manifest!");
    }
    String[] packages = str.split(",");
    return packages;
  }

  /**
   * 加载插件包中的插件实例。
   *
   * @param loader 插件包类加载器
   * @return 插件实例集合
   * @author hankai
   * @since Oct 13, 2016 1:16:57 PM
   */
  private List<Object> loadPluginInstances(URLClassLoader loader) {
    List<Object> instances = new ArrayList<>();
    // 解析jar包的 MANIFEST.MF 文件中的属性
    Attributes attrs = parseJarManifest(loader);
    // 插件基包
    String[] packages = resolvePluginBasePackages(attrs);
    Reflections reflections = null;
    // 尝试根据插件包属性构建 spring 上下文
    AnnotationConfigApplicationContext ctx = loadSpringContext(attrs, loader);
    // 遍历基包来扫描插件
    for (String packageName : packages) {
      reflections = new Reflections(packageName, loader);
      Set<Class<?>> pluginClasses = reflections.getTypesAnnotatedWith(Pluggable.class);
      Object obj = null;
      for (Class<?> clazz : pluginClasses) {
        if (ctx != null) {
          obj = ctx.getBean(clazz);// 基于spring的插件包，从spring上下文获取装配好的插件实例
        }
        if (obj == null) {
          if (ctx != null) {
            logger.warn(String.format(
                "No bean with type \"%s\" was found. Will try to instantiate it directly.",
                clazz.toString()));
          }
          try {
            obj = clazz.newInstance();// 非spring的插件包，通过反射直接构造插件实例
          } catch (Exception e) {
            throw new RuntimeException(
                "Failed to instantiate plugin instance for class: " + clazz.toString(), e);
          }
        }
        Pluggable pluggable = clazz.getAnnotation(Pluggable.class);
        plugins.put(pluggable.name(), ctx);
        instances.add(obj);
      }
    }
    return instances;
  }

  @Override
  public List<Object> loadPlugins(URL jarFileUrl) {
    URLClassLoader loader = new URLClassLoader(new URL[] {jarFileUrl}, sharedClassLoader);
    /*
     * 设置线程的上下文类加载器，这样，由此加载的插件都会使用此类加载器，这样 就能在运行时动态加载需要的依赖包，同时不同插件不共享类加载器，甚至能实现 同一个依赖包的不同版本同时被载入
     */
    Thread.currentThread().setContextClassLoader(loader);
    return loadPluginInstances(loader);
  }

  @Override
  public boolean unloadPlugin(Object instance) {
    Pluggable pluggable = instance.getClass().getAnnotation(Pluggable.class);
    GenericApplicationContext ctx = plugins.get(pluggable.name());
    if (ctx == null) {
      return true;
    }
    try {
      plugins.remove(pluggable.name());
      BeanDefinitionRegistry registry = ctx;
      String[] names = ctx.getBeanNamesForType(instance.getClass());
      if ((names != null) && (names.length > 0)) {
        for (String beanName : names) {
          registry.removeBeanDefinition(beanName);
        }
      }
      Map<String, Object> beans = ctx.getBeansWithAnnotation(Pluggable.class);
      if ((beans == null) || (beans.size() == 0)) {
        ctx.close();
      }
      return true;
    } catch (NoSuchBeanDefinitionException e) {
      logger.warn("No plugin bean definition was found in spring context!", e);
    } catch (BeansException e) {
      logger.warn("Failed to remove plugin bean from spring context!", e);
    }
    return false;
  }
}