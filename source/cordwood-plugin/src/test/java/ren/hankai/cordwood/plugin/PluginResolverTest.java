package ren.hankai.cordwood.plugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.plugin.api.PluginRegistry;
import ren.hankai.cordwood.plugin.api.PluginResolver;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

import java.lang.reflect.Parameter;

/**
 * 插件解析器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 11:28:46 AM
 */
public class PluginResolverTest extends PluginTestSupport {

  @Autowired
  private PluginResolver pluginResolver;
  @Autowired
  private PluginRegistry pluginRegistry;

  @Test
  public void testResolvePackage() throws Exception {
    final String checksum = DigestUtils.sha1Hex(testPluginPackageUrl.openStream());
    final PluginPackage pp = pluginResolver.resolvePackage(testPluginPackageUrl);
    final String expName = FilenameUtils.getName(testPluginPackageUrl.getFile());
    Assert.assertEquals(expName, pp.getFileName());
    Assert.assertEquals(checksum, pp.getIdentifier());
    Assert.assertEquals(testPluginPackageUrl, pp.getInstallUrl());
  }

  @Test
  public void testResolvePlugin() throws Exception {
    final PluginPackage pp = pluginRegistry.registerPackage(testPluginPackageUrl);
    final Plugin plugin = pp.getPlugins().get(0);
    final Plugin resolvedPlugin = pluginResolver.resolvePlugin(plugin.getInstance());
    Assert.assertEquals(plugin.getName(), resolvedPlugin.getName());
    Assert.assertEquals(plugin.getVersion(), resolvedPlugin.getVersion());
    Assert.assertEquals(plugin.getDescription(), resolvedPlugin.getDescription());
    Assert.assertNotNull(resolvedPlugin.getFunctions());
    Assert.assertTrue(resolvedPlugin.getFunctions().size() == 2);
    final PluginFunction resolvedFun = resolvedPlugin.getFunctions().get("sum2");
    final PluginFunction fun = plugin.getFunctions().get("sum2");
    Assert.assertNotNull(resolvedFun);
    Assert.assertEquals(fun.getName(), resolvedFun.getName());
    Assert.assertEquals(fun.getResultType(), resolvedFun.getResultType());
    Assert.assertEquals(fun.getMethod().getName(), resolvedFun.getMethod().getName());

    final Parameter[] resolvedParams = resolvedFun.getParameters();
    final Parameter[] params = fun.getParameters();
    Assert.assertTrue(resolvedParams.length == 2);
    for (int i = 0; i < resolvedParams.length; i++) {
      Assert.assertEquals(params[i].getName(), resolvedParams[i].getName());
      Assert.assertEquals(params[i].getType(), resolvedParams[i].getType());
    }
  }

}
