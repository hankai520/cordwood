
package ren.hankai.cordwood.web.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 测试用于将请求包装为支持多次读取的请求。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 1:53:05 PM
 */
public class MultiReadFilterTest {

  @Test
  public void testDoFilter() throws Exception {
    final MockFilterChain chain = new MockFilterChain();
    final MockHttpServletRequest request = new MockHttpServletRequest();
    final MockHttpServletResponse response = new MockHttpServletResponse();
    final MultiReadFilter filter = new MultiReadFilter();
    filter.doFilter(request, response, chain);
    Assert.assertTrue(chain.getRequest() instanceof MultiReadHttpServletRequest);
    filter.destroy();
  }

}
