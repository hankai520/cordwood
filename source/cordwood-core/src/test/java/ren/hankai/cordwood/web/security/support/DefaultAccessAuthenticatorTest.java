
package ren.hankai.cordwood.web.security.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.core.test.CoreTestSupport;
import ren.hankai.cordwood.web.security.AccessAuthenticator.TokenInfo;

/**
 * 默认访问认证器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Apr 5, 2017 10:36:29 AM
 */
public class DefaultAccessAuthenticatorTest extends CoreTestSupport {

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

  }

  @Test
  public void testVerifyAccessToken() {

  }

}
