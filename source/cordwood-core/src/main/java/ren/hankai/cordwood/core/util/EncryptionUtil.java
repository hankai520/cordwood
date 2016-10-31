package ren.hankai.cordwood.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2016 10:57:29 AM
 */
public class EncryptionUtil {

  private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

  /**
   * AES 加密/解密（支持128位或256位秘钥，其中256位秘钥需要解除JRE算法出口限制）。
   *
   * @param value 待加密的字符串
   * @param sk 秘钥
   * @param encrypt true表示加密，false表示解密
   * @return 密文字符串（base64 编码，为了便于传输，'+'和'/'已被替换为'-'和'_'）
   * @author hankai
   * @since Jun 28, 2016 1:21:55 PM
   */
  public static String aes(String value, String sk, boolean encrypt) {
    try {
      final IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes("UTF-8"));
      final SecretKeySpec skeySpec = new SecretKeySpec(sk.getBytes("UTF-8"), "AES");
      final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      if (encrypt) {
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        final byte[] encrypted = cipher.doFinal(value.getBytes());
        String result = Base64.encodeBase64String(encrypted);
        result = result.replaceAll("\\+", "-");
        result = result.replaceAll("/", "_");
        return result;
      } else {
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        value = value.replaceAll("-", "+");
        value = value.replaceAll("_", "/");
        final byte[] original = cipher.doFinal(Base64.decodeBase64(value));
        return new String(original);
      }
    } catch (final Exception ex) {
      logger.error("Error occurred while encrypting/decrypting data.", ex);
    }
    return null;
  }
}