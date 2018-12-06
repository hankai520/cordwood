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
