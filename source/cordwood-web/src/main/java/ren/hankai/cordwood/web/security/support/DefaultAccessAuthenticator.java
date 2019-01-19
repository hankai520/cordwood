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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
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
  @Lazy
  private ObjectMapper objectMapper;

  @Override
  public String generateAccessToken(TokenInfo tokenInfo) {
    return generateAccessToken(tokenInfo, Preferences.getSystemSk());
  }

  @Override
  public String generateAccessToken(TokenInfo tokenInfo, String secretKey) {
    try {
      String token = objectMapper.writeValueAsString(tokenInfo);
      token = EncryptionUtil.aes(token, secretKey, true);
      return token;
    } catch (final Exception ex) {
      logger.error("Failed to generate api access token!", ex);
    }
    return null;
  }

  @Override
  public TokenInfo parseAccessToken(String tokenString) {
    return parseAccessToken(tokenString, Preferences.getSystemSk());
  }

  @Override
  public TokenInfo parseAccessToken(String tokenString, String secretKey) {
    TokenInfo tokenInfo = null;
    if (!StringUtils.isEmpty(tokenString)) {
      final String decrypted = EncryptionUtil.aes(tokenString, secretKey, false);
      if (StringUtils.isNotEmpty(decrypted)) {
        try {
          tokenInfo = objectMapper.readValue(decrypted, TokenInfo.class);
        } catch (final Exception ex) {
          logger.error(String.format("Failed to parse token: \"%s\"", tokenString), ex);
          logger.error(String.format("Decrypted data is: %s", decrypted));
        }
      }
    }
    return tokenInfo;
  }

  @Override
  public int verifyAccessToken(String tokenString) {
    return verifyAccessToken(tokenString, Preferences.getSystemSk());
  }

  @Override
  public int verifyAccessToken(String tokenString, String secretKey) {
    final TokenInfo tokenInfo = parseAccessToken(tokenString, secretKey);
    if (tokenInfo == null) {
      return TokenInfo.TOKEN_ERROR_INVALID;
    } else if ((tokenInfo.getExpiryTime() >= 0)
        && (tokenInfo.getExpiryTime() < System.currentTimeMillis())) {
      return TokenInfo.TOKEN_ERROR_EXPIRED;
    }
    return 0;
  }

}
