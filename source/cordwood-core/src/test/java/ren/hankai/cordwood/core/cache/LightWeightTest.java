
package ren.hankai.cordwood.core.cache;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.core.test.CoreTestSupport;

public class LightWeightTest extends CoreTestSupport {

  @Autowired
  private CacheTestFixure fixure;

  @Test
  public void testLightWeightCache() throws Exception {
    /*
     * 测试缓存时间间隔思路：调用方法3次，入参相同。由于缓存有效期已设置为1s，因此1s内发出前2次调用，方法调用次数应该为1，
     * 线程休眠1001ms后，发起第3次调用，方法调用计数应该为2.
     */
    CacheTestFixure.m1InvokeTimes = 0; // 重置
    fixure.m1("A");
    Assert.assertEquals(1, CacheTestFixure.m1InvokeTimes);
    fixure.m1("A");
    Assert.assertEquals(1, CacheTestFixure.m1InvokeTimes);
    Thread.sleep(1001);
    fixure.m1("A");
    Assert.assertEquals(2, CacheTestFixure.m1InvokeTimes);
  }

}
