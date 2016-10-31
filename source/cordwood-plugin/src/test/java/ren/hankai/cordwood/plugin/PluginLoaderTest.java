
package ren.hankai.cordwood.plugin;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.plugin.api.PluginLoader;

import java.util.List;

/**
 * 插件加载器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:25:46 PM
 */
public class PluginLoaderTest extends TestSupport {

  @Autowired
  protected PluginLoader pluginLoader;

  @Test
  public void testLoadPlugins() throws Exception {
    final List<Object> objs = pluginLoader.loadPlugins(testPluginPackageUrl);
    Assert.assertNotNull(objs);
    Assert.assertTrue(objs.size() == 1);
    for (final Object instance : objs) {
      Assert.assertTrue(pluginLoader.unloadPlugin(instance));
    }
  }
}
