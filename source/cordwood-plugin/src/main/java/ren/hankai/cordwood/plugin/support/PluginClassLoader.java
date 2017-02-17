
package ren.hankai.cordwood.plugin.support;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * 插件依赖项类加载器，用于加载插件所依赖的第三方jar包（目前没有扩展任何功能，仅用于调试）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 26, 2016 4:26:05 PM
 */
public class PluginClassLoader extends URLClassLoader {

  public PluginClassLoader(URL[] urls, ClassLoader parent,
      URLStreamHandlerFactory factory) {
    super(urls, parent, factory);
  }

  public PluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public PluginClassLoader(URL[] urls) {
    super(urls);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }

}
