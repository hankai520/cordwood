package ren.hankai.cordwood.plugin;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.core.domain.PluginPackage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件参数映射器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 11:27:47 AM
 */
public class ParameterMapperTest extends TestSupport {

  @Autowired
  private ParameterMapper parameterMapper;

  @Autowired
  private PluginRegistry pluginRegistry;

  @Test
  public void testMapParameters() throws Exception {
    HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(request.getParameter("op1")).andReturn("10").anyTimes();
    EasyMock.expect(request.getParameter("op2")).andReturn("15").anyTimes();
    EasyMock.replay(request);
    HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
    EasyMock.replay(response);
    PluginPackage pp = pluginRegistry.register(testPluginPackageUrl);
    Plugin plugin = pp.getPlugins().get(0);
    PluginFunction fun = plugin.getFunctions().get("sum2");
    Object[] params = parameterMapper.mapParameters(fun, request, response);
    Assert.assertTrue(params.length == 2);
    Assert.assertEquals(10, params[0]);
    Assert.assertEquals(15, params[1]);
  }

}
