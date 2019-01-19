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

package ren.hankai.cordwood.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.oauth2.client.ClientInfo;

import java.util.List;

import javax.sql.DataSource;

@EnableAuthorizationServer
public abstract class BaseOauth2Config extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired(required = false)
  private DataSource dataSource;

  /**
   * 获取需要初始化的OAuth2客户端。
   *
   * @return 客户端集合
   * @author hankai
   * @since Jan 19, 2019 11:30:59 AM
   */
  protected abstract List<ClientInfo> getInitialClients();

  /**
   * 用于存储OAuth2客户端信息的表名。
   *
   * @return 表名
   * @author hankai
   * @since Jan 19, 2019 11:31:32 AM
   * @see CustomJdbcClientDetailsServiceBuilder
   */
  protected abstract String getOauth2ClientsTableName();

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    if (null != dataSource) {
      final CustomJdbcClientDetailsServiceBuilder builder =
          new CustomJdbcClientDetailsServiceBuilder();
      builder.dataSource(dataSource);

      final List<ClientInfo> clientInfos = getInitialClients();
      if ((null != clientInfos) && !clientInfos.isEmpty()) {
        for (final ClientInfo clientInfo : clientInfos) {
          builder.withClient(clientInfo.getId())
              .secret(clientInfo.getSecret())
              .accessTokenValiditySeconds(Preferences.getApiAccessTokenExpiry() * 60 * 60 * 24)
              .refreshTokenValiditySeconds(-1)
              .scopes(clientInfo.getScopes())
              // 支持"password", "client_credentials", "authorization_code","refresh_token"
              .authorizedGrantTypes(clientInfo.getGrantTypes())
              .autoApprove(true);
        }
        builder.build();
        clients.setBuilder(builder);
      }
    }
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(jwtTokenStore())
        .accessTokenConverter(jwtAccessTokenConverter())
        .authenticationManager(authenticationManager);
  }

  @Override
  public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
    // 要访问认证服务器tokenKey的时候需要经过身份认证
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }

  /**
   * 构建JWT鉴权码转换器。
   *
   * @return 转换器
   * @author hankai
   * @since Jan 19, 2019 9:01:26 PM
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    // 秘钥库中公钥用于验签，私钥用于签名
    final FileSystemResource keyResource =
        new FileSystemResource(Preferences.getConfigFilePath("oauth.jks"));
    final KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(keyResource, Preferences.getSystemSk().toCharArray());
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth"));
    return converter;
  }

  @Bean
  public TokenStore jwtTokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }

}
