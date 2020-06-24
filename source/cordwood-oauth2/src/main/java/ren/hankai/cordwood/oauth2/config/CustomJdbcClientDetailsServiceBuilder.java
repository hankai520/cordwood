
package ren.hankai.cordwood.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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

  private static final Logger logger = LoggerFactory.getLogger(CustomJdbcClientDetailsServiceBuilder.class);

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
  private final DataSource dataSource;

  /**
   * 当通过 JdbcClientDetailsService 查找密码（secret）为密文的客户端信息时，需要通过编码器对密码进行编码。
   */
  private PasswordEncoder passwordEncoder;

  /**
   * OAUTH2授权信息表名。
   */
  private final String clientDetailsTableName;

  /**
   * 客户端详情表建表语句。
   */
  private final String clientDetailsTableDdl;

  /**
   * 客户端详情服务。
   */
  private final JdbcClientDetailsService actualClientDetailsService;

  @Override
  protected void addClient(final String clientId, final ClientDetails value) {
    clientDetails.add(value);
  }

  @Override
  protected ClientDetailsService performBuild() {
    Assert.state(dataSource != null, "You need to provide a DataSource");
    actualClientDetailsService.setDeleteClientDetailsSql(
        "delete from " + this.clientDetailsTableName + " where client_id = ?");
    actualClientDetailsService.setFindClientDetailsSql("select client_id, " + CLIENT_FIELDS + " from "
        + this.clientDetailsTableName + " order by client_id");
    actualClientDetailsService.setInsertClientDetailsSql(
        "insert into " + this.clientDetailsTableName + " (" + CLIENT_FIELDS
            + ", client_id) values (?,?,?,?,?,?,?,?,?,?,?)");
    actualClientDetailsService.setSelectClientDetailsSql("select client_id, " + CLIENT_FIELDS
        + " from " + this.clientDetailsTableName + " where client_id = ?");
    actualClientDetailsService.setUpdateClientDetailsSql("update " + this.clientDetailsTableName + " set "
        + CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?");
    actualClientDetailsService.setUpdateClientSecretSql("update " + this.clientDetailsTableName
        + " set client_secret = ? where client_id = ?");
    if (passwordEncoder != null) {
      actualClientDetailsService.setPasswordEncoder(passwordEncoder);
    } else {
      logger.warn("PasswordEncoder not specified, using NoOpPasswordEncoder instead.");
      actualClientDetailsService.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
    }
    for (final ClientDetails client : clientDetails) {
      ClientDetails existClient = null;
      try {
        existClient = actualClientDetailsService.loadClientByClientId(client.getClientId());
      } catch (final Exception expected) {
      }
      if (null == existClient) {
        actualClientDetailsService.addClientDetails(client);
      }
    }
    return actualClientDetailsService;
  }

  /**
   * 构造方法。
   *
   * @param dataSource 数据源
   * @param tableDdl 建表语句
   * @param tableName 表名
   */
  public CustomJdbcClientDetailsServiceBuilder(final DataSource dataSource, final String tableDdl,
      final String tableName) {
    this.dataSource = dataSource;
    this.clientDetailsTableDdl = tableDdl;
    this.clientDetailsTableName = tableName;
    this.actualClientDetailsService = new JdbcClientDetailsService(dataSource);
  }

  /**
   * 设置密码器。
   *
   * @param passwordEncoder 密码器
   */
  public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 创建客户端详情表。
   *
   */
  public void createClientDetailsTable() {
    final JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource);
    jdbcTemplate.execute(this.clientDetailsTableDdl);
  }
}
