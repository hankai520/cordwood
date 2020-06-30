
package ren.hankai.cordwood.oauth2.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.oauth2.core.GrantType;
import ren.hankai.cordwood.oauth2.core.Oauth2Client;

/**
 * 用于测试的 Spring 核心配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:31:52 PM
 */
@Configuration
@Profile(Preferences.PROFILE_TEST)
public class TestBeanConfig extends CoreSpringConfig {

  /**
   * 用于在所有测试用例中共享的oauth2 client信息，在配置auth server时会将此客户端初始化。
   *
   * @return 客户端信息
   */
  @Bean(name = "testClient")
  public Oauth2Client testClient() {
    final Oauth2Client client = new Oauth2Client();
    client.setId("test");
    client.setResourceIds("v1");
    client.setSecret("test");
    client.setScopes("read,write");
    client.addGrantType(GrantType.ClientCredentials);
    client.addGrantType(GrantType.Password);
    return client;
  }

}
