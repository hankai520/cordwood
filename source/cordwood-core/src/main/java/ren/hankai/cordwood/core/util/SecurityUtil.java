package ren.hankai.cordwood.core.util;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.domain.TokenInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 安全检查工具类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 27, 2016 5:50:53 PM
 */
public class SecurityUtil {

  private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 根据入参计算签名。
   *
   * @param params 参数表
   * @return 签名字串
   * @author hankai
   * @since Jun 28, 2016 4:00:45 PM
   */
  public static String generateSign(Map<String, ?> params) {
    StringBuffer toBeSigned = new StringBuffer();
    List<String> paramNames = new ArrayList<>();
    paramNames.addAll(params.keySet());
    Collections.sort(paramNames);
    for (String param : paramNames) {
      if (param.equalsIgnoreCase(Preferences.API_ACCESS_TOKEN)
          || param.equalsIgnoreCase(Preferences.API_REQUEST_SIGN)) {
        continue;
      }
      Object objValue = params.get(param);
      String value = null;
      if (objValue instanceof String[]) {
        String[] array = (String[]) objValue;
        if (array.length > 0) {
          value = array[0];
        }
      } else {
        value = objValue.toString();
      }
      if (value != null) {
        try {
          value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          logger.warn(String.format("Failed to url encode request parameter: %s = ", param, value));
        }
        toBeSigned.append(param + "=" + value + "&");
      }
    }
    if (toBeSigned.length() > 0) {
      toBeSigned.deleteCharAt(toBeSigned.length() - 1);
    }
    toBeSigned.append(Preferences.getTransferKey());
    String expSign = DigestUtils.sha1Hex(toBeSigned.toString());
    return expSign;
  }

  /**
   * 对入参进行验签。
   *
   * @param params 参数
   * @return 是否合法
   * @author hankai
   * @since Jun 28, 2016 2:28:00 PM
   */
  public static boolean verifyParameters(Map<String, ?> params) {
    boolean hasParams = false;
    Iterator<String> it = params.keySet().iterator();
    // 检查是否有入参，若无，则不会验证
    while (it.hasNext()) {
      String key = it.next();
      // 忽略白名单字段
      if (key.equalsIgnoreCase(Preferences.API_ACCESS_TOKEN)
          || key.equalsIgnoreCase(Preferences.API_REQUEST_SIGN)) {
        continue;
      }
      hasParams = true;
      break;
    }
    if (!hasParams) {
      return true;
    }
    String expSign = generateSign(params);
    Object sign = params.get(Preferences.API_REQUEST_SIGN);
    if ((sign instanceof String) && expSign.equalsIgnoreCase((String) sign)) {
      return true;
    }
    return false;
  }

  /**
   * 生成 API 鉴权码。
   *
   * @param token 鉴权信息
   * @return
   * @author hankai
   * @since Jun 29, 2016 9:13:55 PM
   */
  public static String generateToken(TokenInfo ti) {
    try {
      String token = objectMapper.writeValueAsString(ti);
      token = EncryptionUtil.aes(token, Preferences.getSystemSk(), true);
      return token;
    } catch (Exception e) {
      logger.error("Failed to generate api access token!", e);
    }
    return null;
  }

  /**
   * 验证令牌。
   *
   * @param rawToken 令牌密文字串
   * @return 是否有效（0：有效，<0：无效，参见 TokenInfo 中的错误常量）
   * @author hankai
   * @since Jun 29, 2016 9:17:15 PM
   */
  public static int verifyToken(String rawToken) {
    TokenInfo tokenInfo = parseToken(rawToken);
    if (tokenInfo == null) {
      return TokenInfo.TOKEN_ERROR_INVALID;
    } else if (tokenInfo.getExpiryTime() < System.currentTimeMillis()) {
      return TokenInfo.TOKEN_ERROR_EXPIRED;
    }
    return 0;
  }

  /**
   * 解析API鉴权码。
   *
   * @param token 鉴权码
   * @return 鉴权信息
   * @author hankai
   * @since Jun 29, 2016 9:56:29 PM
   */
  public static TokenInfo parseToken(String token) {
    TokenInfo tokenInfo = null;
    if (!StringUtils.isEmpty(token)) {
      String decrypted = EncryptionUtil.aes(token, Preferences.getSystemSk(), false);
      try {
        tokenInfo = objectMapper.readValue(decrypted, TokenInfo.class);
      } catch (Exception e) {
        logger.error(String.format("Failed to parse token: \"%s\"", token), e);
        logger.error(String.format("Decrypted data is: %s", decrypted));
      }
    }
    return tokenInfo;
  }

}
