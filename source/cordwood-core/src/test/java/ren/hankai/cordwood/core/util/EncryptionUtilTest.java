/**
 *
 */
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
    String sk = "123321";
    String encrypted = EncryptionUtil.aes("oh yeah!", sk, true);
    Assert.assertNotEquals("oh yeah!", encrypted);
    Assert.assertNotEquals("oh yeah!", EncryptionUtil.aes(encrypted, sk, false));
  }

}
