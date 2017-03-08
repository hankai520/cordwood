package ren.hankai.cordwood.plugin.api;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.plugin.FunctionParameter;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

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
  public void testResolvePlugin() throws Exception {
    final PluginPackage pp = pluginRegistry.registerPackage(testPluginPackageUrl, true);
    final Plugin plugin = pp.getPlugins().get(0);
    final Plugin resolvedPlugin = pluginResolver.resolvePlugin(plugin.getInstance());
    Assert.assertEquals(plugin.getName(), resolvedPlugin.getName());
    Assert.assertEquals(plugin.getDisplayName(), resolvedPlugin.getDisplayName());
    Assert.assertEquals(plugin.getVersion(), resolvedPlugin.getVersion());
    Assert.assertEquals(plugin.getDescription(), resolvedPlugin.getDescription());
    Assert.assertNotNull(resolvedPlugin.getFunctions());
    Assert.assertTrue(resolvedPlugin.getFunctions().size() == 2);
    final PluginFunction resolvedFun = resolvedPlugin.getFunctions().get("sum2");
    final PluginFunction fun = plugin.getFunctions().get("sum2");
    Assert.assertNotNull(resolvedFun);
    Assert.assertEquals(fun.getName(), resolvedFun.getName());
    Assert.assertEquals(fun.getDescription(), resolvedFun.getDescription());
    Assert.assertEquals(fun.getResultType(), resolvedFun.getResultType());
    Assert.assertEquals(fun.getMethod().getName(), resolvedFun.getMethod().getName());

    final FunctionParameter[] resolvedParams = resolvedFun.getParameters();
    final FunctionParameter[] params = fun.getParameters();
    Assert.assertTrue(resolvedParams.length == 2);
    for (int i = 0; i < resolvedParams.length; i++) {
      Assert.assertEquals(params[i].getName(), resolvedParams[i].getName());
      Assert.assertEquals(params[i].getType(), resolvedParams[i].getType());
      Assert.assertEquals(params[i].getDescription(), resolvedParams[i].getDescription());
    }
    pluginRegistry.unregisterPackage(pp.getIdentifier());
  }

}
