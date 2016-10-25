
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.TestSupport;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;
import java.util.List;

/**
 * 插件加载器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:25:46 PM
 */
public class PluginLoaderTest extends TestSupport {

  @Test
  public void testLoadPlugins() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    List<Object> objs = pluginLoader.loadPlugins(url);
    Assert.assertNotNull(objs);
    Assert.assertTrue(objs.size() == 1);
    for (Object instance : objs) {
      Assert.assertTrue(pluginLoader.unloadPlugin(instance));
    }
  }
}
