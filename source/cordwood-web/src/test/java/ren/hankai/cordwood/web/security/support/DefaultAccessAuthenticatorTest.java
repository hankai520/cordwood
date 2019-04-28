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

package ren.hankai.cordwood.web.security.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.web.security.TokenInfo;
import ren.hankai.cordwood.web.test.WebTestSupport;

/**
 * 默认访问认证器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Apr 5, 2017 10:36:29 AM
 */
public class DefaultAccessAuthenticatorTest extends WebTestSupport {

  @Autowired
  private DefaultAccessAuthenticator authenticator;

  @Test
  public void testGenerateAccessToken() {
    final TokenInfo tokenInfo = TokenInfo.withinMinutes("a1e30f", "abc", 10);
    final String token = authenticator.generateAccessToken(tokenInfo);
    final TokenInfo parsedToken = authenticator.parseAccessToken(token);
    Assert.assertEquals(tokenInfo.getUserKey(), parsedToken.getUserKey());
    Assert.assertEquals(tokenInfo.getUserSk(), parsedToken.getUserSk());
    Assert.assertEquals(tokenInfo.getExpiryTime(), parsedToken.getExpiryTime());
  }

  @Test
  public void testParseAccessToken() {
    final TokenInfo tokenInfo = TokenInfo.withinMinutes("a1e30f", "abc", 10);
    final String token = authenticator.generateAccessToken(tokenInfo);
    final int result = authenticator.verifyAccessToken(token);
    Assert.assertEquals(0, result);
  }

  @Test
  public void testVerifyAccessToken() {
    final String sk = "123456789abcdefg";
    final TokenInfo tokenInfo = TokenInfo.withinMinutes("a1e30f", "abc", 10);
    String token = authenticator.generateAccessToken(tokenInfo, sk);
    int result = authenticator.verifyAccessToken(token, sk);
    Assert.assertEquals(0, result);

    // 错误的token
    result = authenticator.verifyAccessToken("invalid token", sk);
    Assert.assertEquals(TokenInfo.TOKEN_ERROR_INVALID, result);

    // 过期的token
    tokenInfo.setExpiryTime(System.currentTimeMillis() - 1000);
    token = authenticator.generateAccessToken(tokenInfo, sk);
    result = authenticator.verifyAccessToken(token, sk);
    Assert.assertEquals(TokenInfo.TOKEN_ERROR_EXPIRED, result);

    // 错误的秘钥
    tokenInfo.setExpiryTime(System.currentTimeMillis() - 1000);
    token = authenticator.generateAccessToken(tokenInfo, sk);
    result = authenticator.verifyAccessToken(token);
    Assert.assertEquals(TokenInfo.TOKEN_ERROR_INVALID, result);
  }

}
