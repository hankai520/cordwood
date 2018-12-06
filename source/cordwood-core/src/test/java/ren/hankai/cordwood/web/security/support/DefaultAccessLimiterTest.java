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
package ren.hankai.cordwood.web.security.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import ren.hankai.cordwood.core.test.CoreTestSupport;
import ren.hankai.cordwood.core.test.controller.MockWebService;
import ren.hankai.cordwood.web.security.AccessLimiter;

import java.lang.reflect.Method;

public class DefaultAccessLimiterTest extends CoreTestSupport {

  @Autowired
  private MockWebService ws;

  @Test
  public void testHandleAccess() throws Exception {
    final HandlerExecutionChain[] chains = new HandlerExecutionChain[2];
    Method method = MockWebService.class.getMethod("s5_1", new Class<?>[] {});
    HandlerMethod hm = new HandlerMethod(ws, method);
    chains[0] = new HandlerExecutionChain(hm);

    method = MockWebService.class.getMethod("s5_2", new Class<?>[] {});
    hm = new HandlerMethod(ws, method);
    chains[1] = new HandlerExecutionChain(hm);

    final AccessLimiter limiter = new DefaultAccessLimiter();
    for (final HandlerExecutionChain chain : chains) {
      // 第一次调用，应该成功
      MockHttpServletResponse response = new MockHttpServletResponse();
      boolean result = limiter.handleAccess(chain, response);
      Assert.assertTrue(result);

      // 第二、三次调用，由于QPS只有1，显然超过QPS，应该失败。由于熔断阈值为1，因此失败次数大于等于2时触发熔断
      response = new MockHttpServletResponse();
      result = limiter.handleAccess(chain, response);
      Assert.assertFalse(result);
      Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatus());

      response = new MockHttpServletResponse();
      result = limiter.handleAccess(chain, response);
      Assert.assertFalse(result);
      Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatus());

      // 第四次调用，测试是否已熔断
      response = new MockHttpServletResponse();
      result = limiter.handleAccess(chain, response);
      Assert.assertFalse(result);
      Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatus());

      // 第五次调用， 在独立线程中调用，验证熔断是否已恢复。
      final AsyncExecut ae = new AsyncExecut(limiter, chain);
      new Thread(ae).start();

      // JUnit 为单线程，上面启动了独立线程测试熔断，此处需等待测试结束
      Thread.sleep(3500);
      Assert.assertTrue(ae.getResult());
    }
  }

  public class AsyncExecut implements Runnable {

    private boolean result;
    private final AccessLimiter limiter;
    private final HandlerExecutionChain chain;

    public AsyncExecut(AccessLimiter limiter, HandlerExecutionChain chain) {
      this.limiter = limiter;
      this.chain = chain;
    }

    public boolean getResult() {
      return this.result;
    }

    @Override
    public void run() {
      // JUnit 为单线程执行，因此限流器运行于主线程，让当前线程休眠，等待主线程限流器熔断恢复，恢复间隔为50ms
      try {
        Thread.sleep(3000);
      } catch (final InterruptedException ex) {
        throw new RuntimeException(ex);
      }
      final MockHttpServletResponse response = new MockHttpServletResponse();
      this.result = limiter.handleAccess(chain, response);
    }
  }
}
