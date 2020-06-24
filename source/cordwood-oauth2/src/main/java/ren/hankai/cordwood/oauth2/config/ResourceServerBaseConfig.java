
package ren.hankai.cordwood.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 资源服务器基础配置（如：RestfulApi）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 24, 2020 1:49:02 PM
 */
@Configuration
@EnableResourceServer
public abstract class ResourceServerBaseConfig extends ResourceServerConfigurerAdapter {

  /**
   * 获取资源版本号，主要用于资源升级。资源服务升级后，可以通过升级版本号，提供新服务给新的客户端。
   * 例如，微博开放平台在升级接口后，可以提供v2版本的接口，开发者添加新应用后，可以选择接入新版本接口。
   *
   * @return 资源版本号，典型值为 v1, v2...
   */
  protected abstract String getResourceVersion();

  /**
   * 接口类资源的根访问地址，例如通过 http://example.org:3000/api/hello 访问某接口，则根地址为 /api/*，表示
   * 通配api下的所有接口。
   *
   * @return 根地址，必须以 "开头"，支持ant语法的通配
   */
  protected abstract String getApiBaseUrl();

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(getResourceVersion()).stateless(true);
  }

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    http.antMatcher(getApiBaseUrl())
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
  }
}
