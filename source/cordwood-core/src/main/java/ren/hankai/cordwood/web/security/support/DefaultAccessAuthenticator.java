package ren.hankai.cordwood.web.security.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.EncryptionUtil;
import ren.hankai.cordwood.web.security.AccessAuthenticator;

/**
 * 访问认证接口默认实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:26:34 PM
 *
 */
@Component
public class DefaultAccessAuthenticator implements AccessAuthenticator {

  private static final Logger logger = LoggerFactory.getLogger(DefaultAccessAuthenticator.class);

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public String generateAccessToken(TokenInfo tokenInfo) {
    try {
      String token = objectMapper.writeValueAsString(tokenInfo);
      token = EncryptionUtil.aes(token, Preferences.getSystemSk(), true);
      return token;
    } catch (final Exception ex) {
      logger.error("Failed to generate api access token!", ex);
    }
    return null;
  }

  @Override
  public TokenInfo parseAccessToken(String tokenString) {
    TokenInfo tokenInfo = null;
    if (!StringUtils.isEmpty(tokenString)) {
      final String decrypted = EncryptionUtil.aes(tokenString, Preferences.getSystemSk(), false);
      try {
        tokenInfo = objectMapper.readValue(decrypted, TokenInfo.class);
      } catch (final Exception ex) {
        logger.error(String.format("Failed to parse token: \"%s\"", tokenString), ex);
        logger.error(String.format("Decrypted data is: %s", decrypted));
      }
    }
    return tokenInfo;
  }

  @Override
  public int verifyAccessToken(String tokenString) {
    final TokenInfo tokenInfo = parseAccessToken(tokenString);
    if (tokenInfo == null) {
      return TokenInfo.TOKEN_ERROR_INVALID;
    } else if ((tokenInfo.getExpiryTime() >= 0)
        && (tokenInfo.getExpiryTime() < System.currentTimeMillis())) {
      return TokenInfo.TOKEN_ERROR_EXPIRED;
    }
    return 0;
  }

}
