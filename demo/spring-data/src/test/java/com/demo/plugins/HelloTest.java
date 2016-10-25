
package com.demo.plugins;

import com.demo.ApplicationTests;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 插件测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 4:49:18 PM
 */
public class HelloTest extends ApplicationTests {

  @Autowired
  private Hello hello;

  @Test
  public void testAdd() throws Exception {
    HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
    EasyMock.expect(request.getParameter("op1")).andReturn("12").anyTimes();
    EasyMock.expect(request.getParameter("op2")).andReturn("11").anyTimes();
    EasyMock.replay(request);
    HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
    EasyMock.replay(response);
    String result = hello.add(request, response);
    Assert.assertTrue("23".equals(result));
    EasyMock.verify(request);
  }
}
