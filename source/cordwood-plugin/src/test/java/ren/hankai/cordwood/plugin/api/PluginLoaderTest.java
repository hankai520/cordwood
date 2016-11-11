
package ren.hankai.cordwood.plugin.api;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

import java.util.List;

/**
 * 插件加载器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:25:46 PM
 */
public class PluginLoaderTest extends PluginTestSupport {

  @Autowired
  protected PluginLoader pluginLoader;

  @Test
  public void testLoadPlugins() throws Exception {
    final PluginPackage pp = new PluginPackage(testPluginPackageUrl);
    final List<Object> objs = pluginLoader.loadPlugins(pp);
    Assert.assertNotNull(objs);
    Assert.assertTrue(objs.size() == 1);
    for (final Object instance : objs) {
      Assert.assertTrue(pluginLoader.unloadPlugin(instance));
    }
  }
}
