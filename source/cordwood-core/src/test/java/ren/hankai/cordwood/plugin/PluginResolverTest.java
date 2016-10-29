package ren.hankai.cordwood.plugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;

import java.lang.reflect.Parameter;

/**
 * 插件解析器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 11:28:46 AM
 */
public class PluginResolverTest extends TestSupport {

  @Autowired
  private PluginResolver pluginResolver;
  @Autowired
  private PluginRegistry pluginRegistry;

  @Test
  public void testResolvePackage() throws Exception {
    String checksum = DigestUtils.sha1Hex(testPluginPackageUrl.openStream());
    PluginPackage pp = pluginResolver.resolvePackage(testPluginPackageUrl);
    String expName = FilenameUtils.getName(testPluginPackageUrl.getFile());
    Assert.assertEquals(expName, pp.getFileName());
    Assert.assertEquals(checksum, pp.getIdentifier());
    Assert.assertEquals(testPluginPackageUrl, pp.getInstallUrl());
  }

  @Test
  public void testResolvePlugin() throws Exception {
    PluginPackage pp = pluginRegistry.register(testPluginPackageUrl);
    Plugin plugin = pp.getPlugins().get(0);
    Plugin resolvedPlugin = pluginResolver.resolvePlugin(plugin.getInstance());
    Assert.assertEquals(plugin.getName(), resolvedPlugin.getName());
    Assert.assertEquals(plugin.getVersion(), resolvedPlugin.getVersion());
    Assert.assertEquals(plugin.getDescription(), resolvedPlugin.getDescription());
    Assert.assertNotNull(resolvedPlugin.getFunctions());
    Assert.assertTrue(resolvedPlugin.getFunctions().size() == 2);
    PluginFunction resolvedFun = resolvedPlugin.getFunctions().get("sum2");
    PluginFunction fun = plugin.getFunctions().get("sum2");
    Assert.assertNotNull(resolvedFun);
    Assert.assertEquals(fun.getName(), resolvedFun.getName());
    Assert.assertEquals(fun.getResultType(), resolvedFun.getResultType());
    Assert.assertEquals(fun.getMethod().getName(), resolvedFun.getMethod().getName());

    Parameter[] resolvedParams = resolvedFun.getParameters();
    Parameter[] params = fun.getParameters();
    Assert.assertTrue(resolvedParams.length == 2);
    for (int i = 0; i < resolvedParams.length; i++) {
      Assert.assertEquals(params[i].getName(), resolvedParams[i].getName());
      Assert.assertEquals(params[i].getType(), resolvedParams[i].getType());
    }
  }

}
