
package com.demo.web;

import com.demo.ApplicationTests;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 示例插件测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 18, 2016 9:46:24 AM
 */
public class DemoWebTest extends ApplicationTests {

  @Test
  public void testSayHello() throws Exception {
    HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(request.getParameter("name")).andReturn("Eola").anyTimes();
    EasyMock.replay(request);
    HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
    EasyMock.replay(response);
    DemoWeb dw = new DemoWeb();
    String result = dw.sayHello(request, response);
    Assert.assertTrue(!StringUtils.isEmpty(result));
    EasyMock.verify(request);
  }
}
