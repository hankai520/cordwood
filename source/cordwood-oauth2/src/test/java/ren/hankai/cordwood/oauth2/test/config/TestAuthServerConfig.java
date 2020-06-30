
package ren.hankai.cordwood.oauth2.test.config;

import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.oauth2.config.AuthorizationServerBaseConfig;
import ren.hankai.cordwood.oauth2.core.Oauth2Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 用于测试的授权服务器，主要用于支持测试不同授权方式是否正常工作。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 22, 2020 9:46:24 AM
 */
@Configuration
@Profile(Preferences.PROFILE_TEST)
public class TestAuthServerConfig extends AuthorizationServerBaseConfig {

  @Qualifier("testClient")
  @Autowired
  private Oauth2Client testClient;

  @Override
  protected DataSource getDataSource() {
    final Properties props = new Properties();
    final String dbfile = Preferences.getDbDir() + "/testdb";
    props.put("url", "jdbc:hsqldb:file:" + dbfile);
    props.put("username", "sa");
    props.put("password", "");
    props.put("loginTimeout", "3");
    try {
      return JDBCDataSourceFactory.createDataSource(props);
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  @Override
  protected List<Oauth2Client> getInitialClients() {
    final List<Oauth2Client> clients = new ArrayList<>();
    clients.add(testClient);
    return clients;
  }

  @Override
  protected String getOauth2ClientsTableName() {
    return "oauth2_clients";
  }

  @Override
  protected String getOauth2ClientsTableDdl() {
    return " CREATE TABLE oauth2_clients ("
        + "    CLIENT_ID VARCHAR(256) NOT NULL PRIMARY KEY,"
        + "    RESOURCE_IDS VARCHAR(256),"
        + "    CLIENT_SECRET VARCHAR(256),"
        + "    SCOPE VARCHAR(256),"
        + "    AUTHORIZED_GRANT_TYPES VARCHAR(256),"
        + "    WEB_SERVER_REDIRECT_URI VARCHAR(256),"
        + "    AUTHORITIES VARCHAR(256),"
        + "    ACCESS_TOKEN_VALIDITY INT,"
        + "    REFRESH_TOKEN_VALIDITY INT,"
        + "    ADDITIONAL_INFORMATION VARCHAR(4096),"
        + "    AUTOAPPROVE VARCHAR(256)"
        + " )";
  }

}
