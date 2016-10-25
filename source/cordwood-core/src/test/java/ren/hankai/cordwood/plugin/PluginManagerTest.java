
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 插件管理器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 2:46:01 PM
 */
public class PluginManagerTest extends TestSupport {

  @Test
  public void testActivatePlugin() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    PluginPackage pp = pluginRegistry.register(url);
    pluginPackage = pp;
    Assert.assertNotNull(pp);
    Assert.assertEquals("pojo-0.0.1.RELEASE.jar", pp.getFileName());
    Assert.assertEquals("73ed386ba57f41e908f1757970df071187aa7a28", pp.getIdentifier());
    URL expUrl = new File(Preferences.getPluginsDir() + File.separator + "pojo-0.0.1.RELEASE.jar")
        .toURI().toURL();
    Assert.assertEquals(expUrl, pp.getInstallUrl());
    Assert.assertTrue(pp.getPlugins().size() == 1);
    plugin = pp.getPlugins().get(0);
    Assert.assertEquals("demo", plugin.getName());
    Assert.assertEquals("1.0.0", plugin.getVersion());
    Assert.assertEquals("test only", plugin.getDescription());
    Assert.assertNotNull(plugin.getInstance());
    Assert.assertNotNull(plugin.getFunctions());
    PluginFunction pf = plugin.getFunctions().get("hello");
    Assert.assertEquals("hello", pf.getName());
    Assert.assertEquals("text/plain", pf.getResultType());
    pluginManager.deactivatePlugin(plugin.getName());
    Plugin plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertFalse(plugin2.isActive());
    pluginManager.activatePlugin(plugin.getName());
    plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertTrue(plugin2.isActive());
    pluginRegistry.unregister("73ed386ba57f41e908f1757970df071187aa7a28");
  }

  @Test
  public void testGetPlugin() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    PluginPackage pp = pluginRegistry.register(url);
    pluginPackage = pp;
    Assert.assertNotNull(pp);
    Assert.assertEquals("pojo-0.0.1.RELEASE.jar", pp.getFileName());
    Assert.assertEquals("73ed386ba57f41e908f1757970df071187aa7a28", pp.getIdentifier());
    URL expUrl = new File(Preferences.getPluginsDir() + File.separator + "pojo-0.0.1.RELEASE.jar")
        .toURI().toURL();
    Assert.assertEquals(expUrl, pp.getInstallUrl());
    Assert.assertTrue(pp.getPlugins().size() == 1);
    plugin = pp.getPlugins().get(0);
    Assert.assertEquals("demo", plugin.getName());
    Assert.assertEquals("1.0.0", plugin.getVersion());
    Assert.assertEquals("test only", plugin.getDescription());
    Assert.assertNotNull(plugin.getInstance());
    Assert.assertNotNull(plugin.getFunctions());
    PluginFunction pf = plugin.getFunctions().get("hello");
    Assert.assertEquals("hello", pf.getName());
    Assert.assertEquals("text/plain", pf.getResultType());
    Plugin plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertEquals(plugin2.getName(), plugin.getName());
    Assert.assertEquals(plugin2.getVersion(), plugin.getVersion());
    Assert.assertEquals(plugin2.getDescription(), plugin.getDescription());
    Assert.assertEquals(plugin2.getInstance(), plugin.getInstance());
    pluginRegistry.unregister("73ed386ba57f41e908f1757970df071187aa7a28");
  }

  @Test
  public void testInitializePlugins() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    String fileName = FilenameUtils.getName(url.getPath());
    FileOutputStream fos =
        new FileOutputStream(Preferences.getPluginsDir() + File.separator + fileName);
    FileCopyUtils.copy(url.openStream(), fos);
    List<String> names = new ArrayList<>();
    names.add("pojo-0.0.1.RELEASE.jar");
    pluginManager.initializePlugins(names);
    Plugin plugin2 = pluginManager.getPlugin("demo");
    Assert.assertEquals(plugin2.getName(), "demo");
    Assert.assertEquals(plugin2.getVersion(), "1.0.0");
    Assert.assertEquals(plugin2.getDescription(), "test only");
    Assert.assertNotNull(plugin2.getInstance());
    pluginRegistry.unregister("73ed386ba57f41e908f1757970df071187aa7a28");
  }
}
