
package ren.hankai.cordwood.web.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import ren.hankai.cordwood.core.test.CoreTestSupport;
import ren.hankai.cordwood.core.test.controller.MockWebService;
import ren.hankai.cordwood.web.security.support.DefaultAccessLimiter;

import java.lang.reflect.Method;

public class AccessLimiterTest extends CoreTestSupport {

  @Autowired
  private MockWebService ws;

  @Test
  public void testHandleAccess() throws Exception {
    final AccessLimiter limiter = new DefaultAccessLimiter();

    final Method method = MockWebService.class.getMethod("s5", new Class<?>[] {});
    final HandlerMethod hm = new HandlerMethod(ws, method);
    final HandlerExecutionChain chain = new HandlerExecutionChain(hm);

    // 第一次调用，应该成功
    MockHttpServletResponse response = new MockHttpServletResponse();
    boolean result = limiter.handleAccess(chain, response);
    Assert.assertTrue(result);

    // 第二、三次调用，由于QPS只有1，显然超过QPS，应该失败。由于熔断阈值为1，因此失败次数大于等于2时触发熔断
    response = new MockHttpServletResponse();
    result = limiter.handleAccess(chain, response);
    Assert.assertFalse(result);

    response = new MockHttpServletResponse();
    result = limiter.handleAccess(chain, response);
    Assert.assertFalse(result);

    // 第四次调用，测试是否已熔断
    response = new MockHttpServletResponse();
    result = limiter.handleAccess(chain, response);
    Assert.assertFalse(result);

    // 第五次调用， 在独立线程中调用，验证熔断是否已恢复。
    final AsyncExecut ae = new AsyncExecut(limiter, chain);
    new Thread(ae).start();

    // JUnit 为单线程，上面启动了独立线程测试熔断，此处需等待测试结束
    Thread.sleep(3500);
    Assert.assertTrue(ae.getResult());
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
