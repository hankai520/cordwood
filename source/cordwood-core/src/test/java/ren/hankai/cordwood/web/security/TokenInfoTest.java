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
package ren.hankai.cordwood.web.security;

import org.junit.Assert;
import org.junit.Test;
import ren.hankai.cordwood.web.security.AccessAuthenticator.TokenInfo;

public class TokenInfoTest {

  @Test
  public void testWithinMinutes() {
    final long expectExpire = System.currentTimeMillis() + (10 * 60 * 1000);
    final TokenInfo tokenInfo = TokenInfo.withinMinutes("123", "321", 10);
    final long timespan = tokenInfo.getExpiryTime() - expectExpire;
    Assert.assertTrue(timespan >= 0);
    Assert.assertTrue(timespan <= 10);
    Assert.assertEquals("123", tokenInfo.getUserKey());
    Assert.assertEquals("321", tokenInfo.getUserSk());
  }

  @Test
  public void testWithinHours() {
    final long expectExpire = System.currentTimeMillis() + (1 * 60 * 60 * 1000);
    final TokenInfo tokenInfo = TokenInfo.withinHours("123", "321", 1);
    final long timespan = tokenInfo.getExpiryTime() - expectExpire;
    Assert.assertTrue(timespan >= 0);
    Assert.assertTrue(timespan <= 10);
    Assert.assertEquals("123", tokenInfo.getUserKey());
    Assert.assertEquals("321", tokenInfo.getUserSk());
  }

  @Test
  public void testWithinDays() {
    final long expectExpire = System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000);
    final TokenInfo tokenInfo = TokenInfo.withinDays("123", "321", 1);
    final long timespan = tokenInfo.getExpiryTime() - expectExpire;
    Assert.assertTrue(timespan >= 0);
    Assert.assertTrue(timespan <= 10);
    Assert.assertEquals("123", tokenInfo.getUserKey());
    Assert.assertEquals("321", tokenInfo.getUserSk());
  }

  @Test
  public void testNeverExpire() {
    final TokenInfo tokenInfo = TokenInfo.neverExpire("123", "321");
    Assert.assertEquals(-1, tokenInfo.getExpiryTime());
    Assert.assertEquals("123", tokenInfo.getUserKey());
    Assert.assertEquals("321", tokenInfo.getUserSk());
  }
}
