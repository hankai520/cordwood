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

package ren.hankai.cordwood.oauth2.token.store;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Jwt (Json Web Token) 令牌验证器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 9, 2018 4:09:40 PM
 */
@Component
public class JwtTokenVerifier {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenVerifier.class);

  @Autowired
  private final TokenStore tokenStore = null;

  /**
   * 检查访问令牌是否有效。
   *
   * @param accessToken 访问令牌
   * @param requiredScopes 必须具备的访问权限（逗号或空格分隔多个权限）
   * @return 验证结果
   * @author hankai
   * @since Aug 9, 2018 6:21:30 PM
   */
  public VerifyResult verifyAccessToken(final String accessToken, final String requiredScopes) {
    OAuth2AccessToken token = null;
    try {
      if (StringUtils.isNotEmpty(accessToken)) {
        token = tokenStore.readAccessToken(accessToken);
      }
    } catch (final Exception ex) {
      logger.debug("Error occurred while verifying access token.", ex);
    }
    return verifyAccessToken(token, requiredScopes);
  }

  /**
   * 检查访问令牌是否有效。
   *
   * @param accessToken 访问令牌
   * @param requiredScopes 必须具备的访问权限（逗号或空格分隔多个权限）
   * @return 验证结果
   */
  public VerifyResult verifyAccessToken(final OAuth2AccessToken accessToken,
      final String requiredScopes) {
    final VerifyResult result = new VerifyResult(accessToken, requiredScopes);
    return result;
  }

  /**
   * 令牌验证结果代码。
   *
   * @author hankai
   * @version 1.0.0
   * @since Aug 12, 2018 11:11:13 AM
   */
  public enum TokenError {
    Invalid, Expired, NotPermitted
  }

  /**
   * 令牌验证结果。
   *
   * @author hankai
   * @version 1.0.0
   * @since Aug 12, 2018 10:48:14 AM
   */
  public class VerifyResult {

    private boolean isValid;
    private OAuth2AccessToken token;
    private TokenError tokenError;
    private String userName;

    private final List<String> authorities = new ArrayList<>();

    /**
     * 用 OAuth2 的访问令牌构造验证结果。
     *
     * @param token 访问令牌
     * @param requiredScopes 必须具备的访问权限（逗号或空格分隔多个权限）
     */
    public VerifyResult(final OAuth2AccessToken token, final String requiredScopes) {
      this.token = token;
      if (null == token) {
        this.tokenError = TokenError.Invalid;
      } else if (token.isExpired()) {
        this.tokenError = TokenError.Expired;
      } else {
        if (StringUtils.isNotEmpty(requiredScopes)) {
          final String[] scopes = requiredScopes.split(",|\\s");
          for (final String scope : scopes) {
            if (!token.getScope().contains(scope)) {
              this.tokenError = TokenError.NotPermitted;
              return;
            }
          }
        }
        this.isValid = true;
        final Map<String, Object> additionalInfo = token.getAdditionalInformation();
        if (null != additionalInfo.get("user_name")) {
          this.userName = (String) additionalInfo.get("user_name");
        }
        if (null != additionalInfo.get("authorities")) {
          final Collection<?> auths = (Collection<?>) additionalInfo.get("authorities");
          for (final Object object : auths) {
            this.authorities.add((String) object);
          }
        }
      }
    }

    /**
     * Token 是否有效。
     *
     * @return Token 是否有效
     * @author hankai
     * @since Aug 12, 2018 5:19:26 PM
     */
    public boolean isValid() {
      return isValid;
    }

    /**
     * 获取 Token 验证的错误。
     *
     * @return Token错误
     * @author hankai
     * @since Aug 12, 2018 5:19:11 PM
     */
    public TokenError getTokenError() {
      return tokenError;
    }

    /**
     * 获取用户名。
     *
     * @return 用户名
     * @author hankai
     * @since Aug 12, 2018 5:18:52 PM
     */
    public String getUserName() {
      return userName;
    }

    /**
     * 获取授权信息集合。
     *
     * @return 授权
     * @author hankai
     * @since Jan 19, 2019 9:02:20 PM
     */
    public String[] getAuthorities() {
      final String[] auths = new String[authorities.size()];
      authorities.toArray(auths);
      return auths;
    }

    /**
     * 获取到期时间。
     *
     * @return 到期时间日期
     * @author hankai
     * @since Jan 19, 2019 9:02:34 PM
     */
    public Date getExpiration() {
      if (null != token) {
        return token.getExpiration();
      }
      return null;
    }

    /**
     * 获取有效期（秒）。
     *
     * @return 有效期（秒）
     * @author hankai
     * @since Jan 19, 2019 9:02:42 PM
     */
    public int getExpiresIn() {
      if (null != token) {
        return token.getExpiresIn();
      }
      return 0;
    }

  }

}
