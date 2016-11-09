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
