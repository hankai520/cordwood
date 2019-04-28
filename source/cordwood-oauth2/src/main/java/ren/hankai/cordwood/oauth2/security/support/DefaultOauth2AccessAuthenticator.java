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

package ren.hankai.cordwood.oauth2.security.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import ren.hankai.cordwood.oauth2.security.Oauth2AccessAuthenticator;
import ren.hankai.cordwood.oauth2.token.store.JwtTokenVerifier;
import ren.hankai.cordwood.oauth2.token.store.JwtTokenVerifier.VerifyResult;
import ren.hankai.cordwood.web.security.TokenInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * 基于 OAuth 2.0 协议的访问认证器实现。参考 org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 * 类实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 22, 2019 3:23:21 PM
 */
@Component
public class DefaultOauth2AccessAuthenticator implements Oauth2AccessAuthenticator {

  private static final Logger logger =
      LoggerFactory.getLogger(DefaultOauth2AccessAuthenticator.class);

  private OAuth2RequestFactory oauth2RequestFactory;
  private final OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

  @Autowired
  private ClientDetailsService clientDetailsService;
  @Autowired
  private TokenGranter tokenGranter;
  @Autowired
  private final TokenStore tokenStore = null;
  @Autowired
  private JwtTokenVerifier tokenVerifier;

  @PostConstruct
  private void init() {
    oauth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
  }

  private boolean isRefreshTokenRequest(final Map<String, String> parameters) {
    return "refresh_token".equals(parameters.get("grant_type"))
        && (parameters.get("refresh_token") != null);
  }

  private boolean isAuthCodeRequest(final Map<String, String> parameters) {
    return "authorization_code".equals(parameters.get("grant_type"))
        && (parameters.get("code") != null);
  }

  @Override
  public String generateAccessToken(final TokenInfo tokenInfo) {
    if (!tokenInfo.isOauth2()) {
      return null;
    }
    final ClientDetails client = clientDetailsService.loadClientByClientId(tokenInfo.getClientId());
    final Map<String, String> parameters = new HashMap<>();
    parameters.put(OAuth2Utils.CLIENT_ID, tokenInfo.getClientId());
    parameters.put(OAuth2Utils.GRANT_TYPE, tokenInfo.getGrantType());
    parameters.put(OAuth2Utils.SCOPE, tokenInfo.getScope());
    if (null != tokenInfo.getExtraInfo()) {
      parameters.putAll(tokenInfo.getExtraInfo());
    }
    final TokenRequest tokenRequest =
        oauth2RequestFactory.createTokenRequest(parameters, client);

    if (!client.getClientId().equals(tokenRequest.getClientId())) {
      return null;
    }
    if (client != null) {
      oauth2RequestValidator.validateScope(tokenRequest, client);
    }
    if (tokenRequest.getGrantType().equals("implicit")) {
      logger.warn("Implicit grant type not supported from token endpoint");
      return null;
    }
    if (isAuthCodeRequest(parameters)) {
      if (!tokenRequest.getScope().isEmpty()) {
        tokenRequest.setScope(Collections.<String>emptySet());
      }
    }
    if (isRefreshTokenRequest(parameters)) {
      tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
    }
    final OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
    if (token == null) {
      logger.warn("Unsupported grant type: " + tokenRequest.getGrantType());
      return null;
    }
    return token.getValue();
  }

  @Override
  public OAuth2AccessToken parseAccessToken(final String tokenString) {
    OAuth2AccessToken token = null;
    if (!StringUtils.isEmpty(tokenString)) {
      token = tokenStore.readAccessToken(tokenString);
      if (null == token) {
        logger.error(String.format("Failed to parse token: \"%s\"", tokenString));
      }
    }
    return token;
  }

  @Override
  public VerifyResult verifyAccessToken(final String tokenString, final String requiredScopes) {
    parseAccessToken(tokenString);
    return tokenVerifier.verifyAccessToken(tokenString, requiredScopes);
  }

  @Override
  public VerifyResult verifyAccessToken(final OAuth2AccessToken token,
      final String requiredScopes) {
    return tokenVerifier.verifyAccessToken(token, requiredScopes);
  }

}
