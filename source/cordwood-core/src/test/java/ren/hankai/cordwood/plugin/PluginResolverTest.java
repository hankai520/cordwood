package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.lang.reflect.Parameter;
import java.net.URL;

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
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    String checksum = DigestUtils.sha1Hex(url.openStream());
    PluginPackage pp = pluginResolver.resolvePackage(url);
    Assert.assertEquals("pojo-0.0.1.RELEASE.jar", pp.getFileName());
    Assert.assertEquals(checksum, pp.getIdentifier());
    Assert.assertEquals(url, pp.getInstallUrl());
  }

  @Test
  public void testResolvePlugin() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    PluginPackage pp = pluginRegistry.register(url);
    Plugin plugin = pp.getPlugins().get(0);

    Plugin resolvedPlugin = pluginResolver.resolvePlugin(plugin.getInstance());
    Assert.assertEquals(plugin.getName(), resolvedPlugin.getName());
    Assert.assertEquals(plugin.getVersion(), resolvedPlugin.getVersion());
    Assert.assertEquals(plugin.getDescription(), resolvedPlugin.getDescription());
    Assert.assertNotNull(resolvedPlugin.getFunctions());
    Assert.assertTrue(resolvedPlugin.getFunctions().size() == 2);
    PluginFunction resolvedFun = resolvedPlugin.getFunctions().get("hello2");
    PluginFunction fun = plugin.getFunctions().get("hello2");
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
