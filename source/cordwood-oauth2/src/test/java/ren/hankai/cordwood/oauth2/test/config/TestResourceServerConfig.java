
package ren.hankai.cordwood.oauth2.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.oauth2.config.ResourceServerBaseConfig;

/**
 * OAuth2资源服务配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2020 10:23:25 AM
 */
@Configuration
@Profile(Preferences.PROFILE_TEST)
public class TestResourceServerConfig extends ResourceServerBaseConfig {

  @Override
  protected String getResourceVersion() {
    return "v1";
  }

  @Override
  protected String getApiBaseUrl() {
    return "/api/*";
  }

}
