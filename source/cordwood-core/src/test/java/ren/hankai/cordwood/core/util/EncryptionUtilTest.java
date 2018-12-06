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
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 加密工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 11:12:58 PM
 *
 */
public class EncryptionUtilTest {

  @Test
  public void testAes() {
    final String sk = "1234432112344321";// 必须为 16, 24 或 32 字节
    final String encrypted = EncryptionUtil.aes("oh yeah!", sk, true);
    Assert.assertNotEquals("oh yeah!", encrypted);
    final String decrypted = EncryptionUtil.aes(encrypted, sk, false);
    Assert.assertEquals("oh yeah!", decrypted);
  }

}
