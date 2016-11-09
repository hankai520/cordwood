
package ren.hankai.cordwood.plugin.support;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLoader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      final URL[] urls = Preferences.getLibUrls();
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
      final URL[] urls = loader.getURLs();
      if ((urls != null) && (urls.length > 0)) {
        url = urls[0];
        final InputStream is = url.openStream();
        jarStream = new JarInputStream(is);
        final Manifest manifest = jarStream.getManifest();
        attrs = manifest.getMainAttributes();
      }
    } catch (final IOException e) {
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
      final String bootClassName = attrs.getValue(PluginLoader.PLUGIN_SPRING_CONFIG_CLASS);
      if (!StringUtils.isEmpty(bootClassName)) {
        try {
          final Class<?> bootClass = Class.forName(bootClassName, true, loader);
          if (bootClass != null) {
            final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.setParent(context);
            ctx.setClassLoader(loader);
            ctx.register(bootClass);
            ctx.refresh();
            return ctx;
          }
        } catch (final ClassNotFoundException e) {
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
    final String str = attrs.getValue(PluginLoader.PLUGIN_BASE_PACKAGES);
    if (StringUtils.isEmpty(str)) {
      throw new RuntimeException("Invalid plugin package: no base packages found in manifest!");
    }
    final String[] packages = str.split(",");
    return packages;
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
            Object obj = (ctx != null) ? ctx.getBean(clazz) : null;
            if (obj == null) {
              if (ctx != null) {
                logger.debug(String.format(
                    "No bean with type \"%s\" was found. Will try to instantiate it directly.",
                    clazz.toString()));
              }
              obj = clazz.newInstance();// 非spring的插件包，通过反射直接构造插件实例
            }
            plugins.put(pluggable.name(), ctx);
            instances.add(obj);
          }
        }
      }
    } catch (final Exception e) {
      throw new RuntimeException(
          "Failed to instantiate plugin in base package \"%s\"" + basePackage, e);
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
  private List<Object> loadPluginInstances(URLClassLoader loader) {
    final List<Object> instances = new ArrayList<>();
    // 解析jar包的 MANIFEST.MF 文件中的属性
    final Attributes attrs = parseJarManifest(loader);
    // 插件基包
    final String[] packages = resolvePluginBasePackages(attrs);
    // 尝试根据插件包属性构建 spring 上下文
    final AnnotationConfigApplicationContext ctx = loadSpringContext(attrs, loader);
    // 遍历基包来扫描插件
    for (final String packageName : packages) {
      final List<Object> objs = instantiatePluginClasses(packageName, ctx);
      instances.addAll(objs);
    }
    return instances;
  }

  @Override
  public List<Object> loadPlugins(URL jarFileUrl) {
    final URLClassLoader loader = new URLClassLoader(new URL[] {jarFileUrl}, sharedClassLoader);
    /*
     * 设置线程的上下文类加载器，这样，由此加载的插件都会使用此类加载器，这样 就能在运行时动态加载需要的依赖包，同时不同插件不共享类加载器，甚至能实现 同一个依赖包的不同版本同时被载入
     */
    Thread.currentThread().setContextClassLoader(loader);
    return loadPluginInstances(loader);
  }

  @Override
  public boolean unloadPlugin(Object instance) {
    final Pluggable pluggable = instance.getClass().getAnnotation(Pluggable.class);
    final GenericApplicationContext ctx = plugins.get(pluggable.name());
    if (ctx == null) {
      return true;
    }
    try {
      plugins.remove(pluggable.name());
      final BeanDefinitionRegistry registry = ctx;
      final String[] names = ctx.getBeanNamesForType(instance.getClass());
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
}
