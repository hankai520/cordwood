
package ren.hankai.cordwood.oauth2.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.oauth2.config.AuthorizationServerBaseConfig;
import ren.hankai.cordwood.oauth2.core.Oauth2Client;

import java.util.List;

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

  @Override
  protected DataSource getDataSource() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected List<Oauth2Client> getInitialClients() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getOauth2ClientsTableName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getOauth2ClientsTableDdl() {
    // TODO Auto-generated method stub
    return null;
  }

}
