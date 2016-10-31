package ren.hankai.cordwood.plugin;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.plugin.api.PluginDriver;
import ren.hankai.cordwood.plugin.api.PluginRegistry;

import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件驱动器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 11:27:12 AM
 */
public class PluginDriverTest extends TestSupport {

  @Autowired
  private PluginDriver pluginDriver;

  @Autowired
  private PluginRegistry pluginRegistry;

  @Test
  public void testHandleRequest() throws Exception {
    final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(request.getParameter("op1")).andReturn("10").anyTimes();
    EasyMock.expect(request.getParameter("op2")).andReturn("21").anyTimes();
    EasyMock.replay(request);
    final HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
    EasyMock.replay(response);
    pluginRegistry.registerPackage(testPluginPackageUrl);
    final Object obj = pluginDriver.handleRequest("pojo", "sum", request, response);
    Assert.assertTrue("Hi, the result is: 31".equals(obj));
    EasyMock.verify(request);
    EasyMock.verify(response);
  }

  @Test
  public void testGetResource() throws Exception {
    final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(request.getRequestURI()).andReturn("/resources/pojo/testonly.txt").anyTimes();
    EasyMock.replay(request);
    pluginRegistry.registerPackage(testPluginPackageUrl);
    final InputStream is = pluginDriver.getResource("pojo", request);
    Assert.assertNotNull(is);
    final StringWriter writer = new StringWriter();
    IOUtils.copy(is, writer, "UTF-8");
    final String expString = writer.toString();
    Assert.assertTrue(
        "this file is used as plugin resource, just for test and demonstration.".equals(expString));
    EasyMock.verify(request);
  }

}
