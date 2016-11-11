
package ren.hankai.cordwood.plugin.api;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 插件管理器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 2:46:01 PM
 */
public class PluginManagerTest extends PluginTestSupport {

  private Plugin plugin;

  @Autowired
  protected PluginRegistry pluginRegistry;
  @Autowired
  protected PluginManager pluginManager;

  @Test
  public void testActivatePlugin() throws Exception {
    final PluginPackage pp = pluginRegistry.registerPackage(testPluginPackageUrl, true);
    Assert.assertNotNull(pp);
    final String expName = FilenameUtils.getName(testPluginPackageUrl.getFile());
    Assert.assertEquals(expName, pp.getFileName());
    final URL expUrl =
        new File(Preferences.getPluginsDir() + File.separator + expName).toURI().toURL();
    Assert.assertEquals(expUrl, pp.getInstallationUrl());
    Assert.assertEquals("ren.hankai.cordwood", pp.getGroupId());
    Assert.assertEquals("plugin.pojo", pp.getArtifactId());
    Assert.assertEquals("0.0.1.RELEASE", pp.getVersion());
    Assert.assertArrayEquals(new String[] {"ren.hankai.cordwood.plugin"}, pp.getBasePackages());
    Assert.assertTrue(pp.getPlugins().size() == 1);
    plugin = pp.getPlugins().get(0);
    Assert.assertEquals("pojo", plugin.getName());
    Assert.assertEquals("示例POJO插件", plugin.getDisplayName());
    Assert.assertEquals("1.0.5", plugin.getVersion());
    Assert.assertEquals("POJO 插件，适用于不依赖于第三方框架的纯业务逻辑。", plugin.getDescription());
    Assert.assertNotNull(plugin.getInstance());
    Assert.assertNotNull(plugin.getFunctions());
    final PluginFunction pf = plugin.getFunctions().get("sum");
    Assert.assertEquals("sum", pf.getName());
    Assert.assertEquals("text/plain", pf.getResultType());
    pluginManager.deactivatePlugin(plugin.getName());
    Plugin plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertFalse(plugin2.isActive());
    pluginManager.activatePlugin(plugin.getName());
    plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertTrue(plugin2.isActive());
    pluginRegistry.unregisterPackage(pp.getIdentifier());
  }

  @Test
  public void testGetPlugin() throws Exception {
    final PluginPackage pp = pluginRegistry.registerPackage(testPluginPackageUrl, true);
    Assert.assertNotNull(pp);
    final String expName = FilenameUtils.getName(testPluginPackageUrl.getFile());
    Assert.assertEquals(expName, pp.getFileName());
    final URL expUrl =
        new File(Preferences.getPluginsDir() + File.separator + expName).toURI().toURL();
    Assert.assertEquals(expUrl, pp.getInstallationUrl());
    Assert.assertEquals("ren.hankai.cordwood", pp.getGroupId());
    Assert.assertEquals("plugin.pojo", pp.getArtifactId());
    Assert.assertEquals("0.0.1.RELEASE", pp.getVersion());
    Assert.assertArrayEquals(new String[] {"ren.hankai.cordwood.plugin"}, pp.getBasePackages());
    Assert.assertTrue(pp.getPlugins().size() == 1);
    plugin = pp.getPlugins().get(0);
    Assert.assertEquals("pojo", plugin.getName());
    Assert.assertEquals("示例POJO插件", plugin.getDisplayName());
    Assert.assertEquals("1.0.5", plugin.getVersion());
    Assert.assertEquals("POJO 插件，适用于不依赖于第三方框架的纯业务逻辑。", plugin.getDescription());
    Assert.assertNotNull(plugin.getInstance());
    Assert.assertNotNull(plugin.getFunctions());
    final PluginFunction pf = plugin.getFunctions().get("sum");
    Assert.assertEquals("sum", pf.getName());
    Assert.assertEquals("text/plain", pf.getResultType());
    final Plugin plugin2 = pluginManager.getPlugin(plugin.getName());
    Assert.assertEquals(plugin2.getName(), plugin.getName());
    Assert.assertEquals(plugin2.getVersion(), plugin.getVersion());
    Assert.assertEquals(plugin2.getDescription(), plugin.getDescription());
    Assert.assertEquals(plugin2.getInstance(), plugin.getInstance());
    pluginRegistry.unregisterPackage(pp.getIdentifier());
  }

  @Test
  public void testGetPluginPackage() throws Exception {
    final PluginPackage pp = pluginRegistry.registerPackage(testPluginPackageUrl, true);
    final PluginPackage loadedPP = pluginManager.getPluginPackage(pp.getIdentifier());
    Assert.assertEquals(loadedPP.getGroupId(), pp.getGroupId());
    Assert.assertEquals(loadedPP.getArtifactId(), pp.getArtifactId());
    Assert.assertEquals(loadedPP.getVersion(), pp.getVersion());
    Assert.assertArrayEquals(loadedPP.getBasePackages(), pp.getBasePackages());
    Assert.assertEquals(loadedPP.getIdentifier(), pp.getIdentifier());
  }

  @Test
  public void testInitializePlugins() throws Exception {
    final List<PluginPackage> packages = new ArrayList<>();
    packages.add(new PluginPackage(testPluginPackageUrl));
    pluginManager.initializePlugins(packages);
    final Plugin plugin2 = pluginManager.getPlugin("pojo");
    Assert.assertEquals("pojo", plugin2.getName());
    Assert.assertEquals("示例POJO插件", plugin2.getDisplayName());
    Assert.assertEquals("1.0.5", plugin2.getVersion());
    Assert.assertEquals("POJO 插件，适用于不依赖于第三方框架的纯业务逻辑。", plugin2.getDescription());
    Assert.assertNotNull(plugin2.getInstance());
    pluginRegistry.unregisterPackage(DigestUtils.sha1Hex(testPluginPackageUrl.openStream()));
  }

  @Test
  public void testRegisterTransientPlugin() throws Exception {
    final Plugin plugin = pluginRegistry.registerTransientPlugin(new TestPlugin(), false);
    Assert.assertNotNull(plugin);
    Assert.assertEquals("TestPlugin", plugin.getName());
    Assert.assertEquals("1.2.1", plugin.getVersion());
    Assert.assertTrue(pluginRegistry.isPluginRegistered(plugin.getName()));
  }

  @Test
  public void testUnregisterTransientPlugin() throws Exception {
    final Plugin plugin = pluginRegistry.registerTransientPlugin(new TestPlugin(), true);
    Assert.assertNotNull(plugin);
    Assert.assertTrue(pluginRegistry.isPluginRegistered(plugin.getName()));
    Assert.assertTrue(pluginRegistry.unregisterTransientPlugin(plugin.getName()));
    Assert.assertFalse(pluginRegistry.isPluginRegistered(plugin.getName()));
  }

  @Test
  public void testGetPluginIterator() throws Exception {
    final Plugin plugin = pluginRegistry.registerTransientPlugin(new TestPlugin(), true);
    final Iterator<Plugin> it = pluginManager.getPluginIterator();
    Assert.assertTrue(it.hasNext());
    it.next();
    Assert.assertFalse(it.hasNext());
    Assert.assertTrue(pluginRegistry.unregisterTransientPlugin(plugin.getName()));
  }

  @Pluggable(name = "TestPlugin", version = "1.2.1")
  private static final class TestPlugin {

  }
}
