
package ren.hankai.cordwood.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.oauth2.core.Oauth2Client;

import java.util.List;

import javax.sql.DataSource;

/**
 * Authroization Server 基础配置，基于数据库（JDBC）存储客户端信息，基于RSA密钥对实现token签名和验签，
 * token授权信息存储于内存中。通过继承此类，可实现创建客户端信息表，初始化客户端。
 * 必须将oauth.jks文件（通过openssl等工具自行创建）置于数据目录中的配置文件目录下以支持RSA签名和验签，其中的
 * 密钥对名称必须是oauth。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:22:23 PM
 */
@EnableAuthorizationServer
public abstract class AuthorizationServerBaseConfig extends AuthorizationServerConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerBaseConfig.class);

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired(required = false)
  private PasswordEncoder passwordEncoder;

  /**
   * 获取数据源，用于建表并初始化客户端数据。
   *
   * @return 数据源
   */
  protected abstract DataSource getDataSource();

  /**
   * 获取需要初始化的OAuth2客户端。
   *
   * @return 客户端集合
   * @author hankai
   * @since Jan 19, 2019 11:30:59 AM
   */
  protected abstract List<Oauth2Client> getInitialClients();

  /**
   * 用于存储OAuth2客户端信息的表名。
   *
   * @return 表名
   * @author hankai
   * @since Jan 19, 2019 11:31:32 AM
   * @see CustomJdbcClientDetailsServiceBuilder
   */
  protected abstract String getOauth2ClientsTableName();

  /**
   * 获取用于建表的SQL语句。
   *
   * @return 建表SQL
   */
  protected abstract String getOauth2ClientsTableDdl();

  /**
   * 获取授权信息存储服务，子类覆盖此方法可提供自定义实现，默认在内存中存储。
   *
   * @return 授权存储服务
   */
  protected ApprovalStore getApprovalStore() {
    return new InMemoryApprovalStore();
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    final DataSource dataSource = getDataSource();
    ClientDetailsServiceBuilder<?> builder = null;
    if (null != dataSource) {
      final CustomJdbcClientDetailsServiceBuilder jdbcClientBuilder =
          new CustomJdbcClientDetailsServiceBuilder(dataSource, getOauth2ClientsTableDdl(),
              getOauth2ClientsTableName());
      if (null != passwordEncoder) {
        jdbcClientBuilder.setPasswordEncoder(passwordEncoder);
      }
      jdbcClientBuilder.createClientDetailsTable();
      builder = jdbcClientBuilder;
    } else {
      logger.warn("DataSource is not specified, using InMemoryClientDetailsServiceBuilder instead.");
      builder = new InMemoryClientDetailsServiceBuilder();
    }
    final List<Oauth2Client> clientInfos = getInitialClients();
    if ((null != clientInfos) && !clientInfos.isEmpty()) {
      for (final Oauth2Client clientInfo : clientInfos) {
        builder.withClient(clientInfo.getId())
            .secret(clientInfo.getSecret())
            .accessTokenValiditySeconds(Preferences.getApiAccessTokenExpiry() * 60 * 60 * 24)
            .refreshTokenValiditySeconds(2 * Preferences.getApiAccessTokenExpiry() * 60 * 60 * 24)
            .scopes(clientInfo.getScopes())
            .authorizedGrantTypes(clientInfo.getGrantTypesString())
            .autoApprove(true);
      }
    } else {
      logger.debug("No initial clients specified, skipped client details initializaiton.");
    }
    builder.build();
    clients.setBuilder(builder);
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
    security
        .allowFormAuthenticationForClients() // 允许resource server远程坚定客户端
        .tokenKeyAccess("isAuthenticated()") // 允许已鉴定的用户获取令牌公钥
        .checkTokenAccess("permitAll()") // 允许所有请求检查令牌
        .allowFormAuthenticationForClients(); // 允许客户端通过表单鉴定身份
  }

  /**
   * 构建JWT令牌转换器，内部通过RSA密钥来实现令牌的签名与验签。
   *
   * @return JSON Web Token转换器
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    /*
     * 秘钥库中公钥用于验签，私钥用于签名。
     * 1. 生成JKS与RSA密钥对：
     * keytool -genkey -keystore oauth.jks -alias oauth \
     * -keypass 654321 -storepass 123456 -keyalg RSA \
     * -dname "CN=Cordwood, OU=Cordwood, O=Cordwood L=Nanjing, S=Jiangsu, C=CN"
     * 2. 查看生成的密钥对：keytool -list -v -keystore oauth.jks -alias oauth
     */
    final FileSystemResource keyResource =
        new FileSystemResource(Preferences.getConfigFilePath("oauth.jks"));
    if (!keyResource.exists()) {
      throw new RuntimeException(
          String.format("OAuth java keystore file not found in path: %s", keyResource.getPath()));
    }
    final String storePwd = Preferences.getCustomConfig("oauth2.keystore.password");
    final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyResource, storePwd.toCharArray());
    final String keyPwd = Preferences.getCustomConfig("oauth2.keypair.password");
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth", keyPwd.toCharArray()));
    return converter;
  }

  /**
   * 构造令牌存储服务。
   *
   * @return 令牌存储服务
   */
  @Bean
  public TokenStore jwtTokenStore() {
    final ApprovalStore as = getApprovalStore();
    if (null == as) {
      throw new RuntimeException(
          "ApprovalStore must not be null, call super.getApprovalStore() if the method is overwritten.");
    }
    final JwtTokenStore ts = new JwtTokenStore(jwtAccessTokenConverter());
    ts.setApprovalStore(as);
    return ts;
  }

}
