/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package ren.hankai.cordwood.web.breadcrumb;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import ren.hankai.cordwood.core.test.CoreTestSupport;
import ren.hankai.cordwood.core.test.Route;
import ren.hankai.cordwood.core.test.controller.MockWebController;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 面包屑导航拦截器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 27, 2018 5:44:07 PM
 */
public class BreadCrumbInterceptorTest extends CoreTestSupport {

  @Autowired
  private MockWebController wc;

  @SuppressWarnings("unchecked")
  @Test
  public void testPreHandle() throws Exception {
    Method method = MockWebController.class.getMethod("s6_1", new Class<?>[] {});
    HandlerMethod hm = new HandlerMethod(wc, method);

    MockHttpServletRequest request = new MockHttpServletRequest("POST", Route.S6_1);
    MockHttpServletResponse response = new MockHttpServletResponse();
    BreadCrumbInterceptor bci = new BreadCrumbInterceptor();
    boolean shouldProceed = bci.preHandle(request, response, hm);
    Assert.assertTrue(shouldProceed);

    final HttpSession session1 = request.getSession();
    LinkedList<BreadCrumbLink> currentBreadCrumb = (LinkedList<BreadCrumbLink>) session1
        .getAttribute(BreadCrumbInterceptor.CURRENT_BREAD_CRUMB);
    Assert.assertNotNull(currentBreadCrumb);
    Assert.assertTrue(currentBreadCrumb.size() == 1);
    BreadCrumbLink link = currentBreadCrumb.getFirst();
    Assert.assertEquals("test", link.getFamily());
    Assert.assertEquals("s6_1", link.getLabel());

    Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb =
        (Map<String, LinkedHashMap<String, BreadCrumbLink>>) session1
            .getAttribute(BreadCrumbInterceptor.BREAD_CRUMB_LINKS);
    Assert.assertNotNull(breadCrumb);
    Assert.assertTrue(breadCrumb.size() == 1);
    Map<String, BreadCrumbLink> testFamily = breadCrumb.get("test");
    BreadCrumbLink subPage = testFamily.get("s6_1");
    Assert.assertNotNull(subPage);
    Assert.assertEquals("test", subPage.getFamily());
    Assert.assertEquals("s6_1", subPage.getLabel());

    /* 访问子页面，测试导航项目 */
    method = MockWebController.class.getMethod("s6_2", new Class<?>[] {});
    hm = new HandlerMethod(wc, method);

    request = new MockHttpServletRequest("POST", Route.S6_2);
    request.setSession(session1);
    response = new MockHttpServletResponse();
    bci = new BreadCrumbInterceptor();
    shouldProceed = bci.preHandle(request, response, hm);
    Assert.assertTrue(shouldProceed);

    final HttpSession session2 = request.getSession();
    currentBreadCrumb = (LinkedList<BreadCrumbLink>) session2
        .getAttribute(BreadCrumbInterceptor.CURRENT_BREAD_CRUMB);
    Assert.assertNotNull(currentBreadCrumb);
    Assert.assertTrue(currentBreadCrumb.size() == 2);
    link = currentBreadCrumb.getFirst();
    Assert.assertEquals("test", link.getFamily());
    Assert.assertEquals("s6_1", link.getLabel());
    link = currentBreadCrumb.getLast();
    Assert.assertEquals("test", link.getFamily());
    Assert.assertEquals("s6_2", link.getLabel());

    breadCrumb = (Map<String, LinkedHashMap<String, BreadCrumbLink>>) session2
        .getAttribute(BreadCrumbInterceptor.BREAD_CRUMB_LINKS);
    Assert.assertNotNull(breadCrumb);
    Assert.assertTrue(breadCrumb.size() == 1);
    testFamily = breadCrumb.get("test");
    subPage = testFamily.get("s6_1");
    Assert.assertNotNull(subPage);
    Assert.assertEquals("test", subPage.getFamily());
    Assert.assertEquals("s6_1", subPage.getLabel());

    subPage = testFamily.get("s6_2");
    Assert.assertNotNull(subPage);
    Assert.assertEquals("test", subPage.getFamily());
    Assert.assertEquals("s6_2", subPage.getLabel());
  }

}
