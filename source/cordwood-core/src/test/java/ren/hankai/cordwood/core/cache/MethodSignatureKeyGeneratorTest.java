/*******************************************************************************
 * Copyright (C) 2019 hankai
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

import java.lang.reflect.Method;

public class MethodSignatureKeyGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    final MethodSignatureKeyGeneratorTest target = new MethodSignatureKeyGeneratorTest();
    final Method method = target.getClass().getMethod("testGenerate");
    final MethodSignatureKeyGenerator gen = new MethodSignatureKeyGenerator();
    final String p1 = "abc";
    final Object key = gen.generate(target, method, new Object[] {p1});
    Assert.assertTrue(key instanceof String);
    final String exp =
        target.getClass().toString() + method.getName() + String.class.toString() + p1.hashCode();
    Assert.assertEquals(exp, key);
  }

}
