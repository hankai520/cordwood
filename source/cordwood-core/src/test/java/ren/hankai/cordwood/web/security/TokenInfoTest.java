
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
