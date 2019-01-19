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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

/**
 * 自定义的 OAuth2 客户端详情服务构建器。需要在目标数据库中新建以下表：
 *
 * <pre>
 * CREATE TABLE this.clientDetailsTableName (
 *     CLIENT_ID VARCHAR2(256) NOT NULL PRIMARY KEY,
 *     RESOURCE_IDS VARCHAR2(256),
 *     CLIENT_SECRET VARCHAR2(256),
 *     SCOPE VARCHAR2(256),
 *     AUTHORIZED_GRANT_TYPES VARCHAR2(256),
 *     WEB_SERVER_REDIRECT_URI VARCHAR2(256),
 *     AUTHORITIES VARCHAR2(256),
 *     ACCESS_TOKEN_VALIDITY NUMBER(8),
 *     REFRESH_TOKEN_VALIDITY NUMBER(8),
 *     ADDITIONAL_INFORMATION VARCHAR2(4096),
 *     AUTOAPPROVE VARCHAR2(256)
 * );
 * </pre>
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 15, 2018 2:58:17 PM
 */
public class CustomJdbcClientDetailsServiceBuilder
    extends ClientDetailsServiceBuilder<JdbcClientDetailsServiceBuilder> {

  /**
   * 可更新的客户端表字段。
   */
  private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, "
      + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
      + "refresh_token_validity, additional_information, autoapprove";

  /**
   * 客户端表字段。
   */
  private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

  /**
   * 需要初始化的客户端集合。
   */
  private final Set<ClientDetails> clientDetails = new HashSet<>();

  /**
   * 数据库数据源。
   */
  private DataSource dataSource;

  /**
   * 当通过 JdbcClientDetailsService 查找密码（secret）为密文的客户端信息时，需要通过编码器对密码进行编码。
   */
  private PasswordEncoder passwordEncoder;

  /**
   * OAUTH2授权信息表名。
   */
  private String clientDetailsTableName;

  public CustomJdbcClientDetailsServiceBuilder clientDetailsTableName(final String tableName) {
    this.clientDetailsTableName = tableName;
    return this;
  }

  public CustomJdbcClientDetailsServiceBuilder dataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  public CustomJdbcClientDetailsServiceBuilder passwordEncoder(
      final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    return this;
  }

  @Override
  protected void addClient(final String clientId, final ClientDetails value) {
    clientDetails.add(value);
  }

  @Override
  protected ClientDetailsService performBuild() {
    Assert.state(dataSource != null, "You need to provide a DataSource");
    final JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
    clientDetailsService.setDeleteClientDetailsSql(
        "delete from " + this.clientDetailsTableName + " where client_id = ?");
    clientDetailsService.setFindClientDetailsSql("select client_id, " + CLIENT_FIELDS + " from "
        + this.clientDetailsTableName + " order by client_id");
    clientDetailsService.setInsertClientDetailsSql(
        "insert into " + this.clientDetailsTableName + " (" + CLIENT_FIELDS
            + ", client_id) values (?,?,?,?,?,?,?,?,?,?,?)");
    clientDetailsService.setSelectClientDetailsSql("select client_id, " + CLIENT_FIELDS
        + " from " + this.clientDetailsTableName + " where client_id = ?");
    clientDetailsService.setUpdateClientDetailsSql("update " + this.clientDetailsTableName + " set "
        + CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?");
    clientDetailsService.setUpdateClientSecretSql("update " + this.clientDetailsTableName
        + " set client_secret = ? where client_id = ?");
    if (passwordEncoder != null) {
      clientDetailsService.setPasswordEncoder(passwordEncoder);
    }
    for (final ClientDetails client : clientDetails) {
      ClientDetails existClient = null;
      try {
        existClient = clientDetailsService.loadClientByClientId(client.getClientId());
      } catch (final Exception expected) {
      }
      if (null == existClient) {
        clientDetailsService.addClientDetails(client);
      }
    }
    return clientDetailsService;
  }
}
