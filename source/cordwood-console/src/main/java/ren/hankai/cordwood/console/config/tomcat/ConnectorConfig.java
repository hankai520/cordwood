
package ren.hankai.cordwood.console.config.tomcat;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ren.hankai.cordwood.core.Preferences;

/**
 * Tomcat Connector 配置。
 *
 * @author hankai
 * @version 1.0
 * @since Apr 5, 2016 4:11:36 PM
 */
@Component
public class ConnectorConfig implements TomcatConnectorCustomizer {

  /*
   * 用于自定义 tomcat connector 配置。默认提供 proxy 配置，这将用于将 tomcat 作为反向代理架构下 的后端服务器的场景。
   *
   * @see org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer#customize(org.
   * apache.catalina.connector.Connector)
   */
  @Override
  public void customize(Connector connector) {
    final String proxyScheme = Preferences.getProxyScheme();
    final String proxyName = Preferences.getProxyName();
    final Integer proxyPort = Preferences.getProxyPort();
    if (!StringUtils.isEmpty(proxyScheme) && !StringUtils.isEmpty(proxyName) && (proxyPort != null)
        && (proxyPort > 0)) {
      connector.setProxyName(proxyName);
      connector.setProxyPort(proxyPort);
      connector.setRedirectPort(proxyPort);
      connector.setScheme(proxyScheme);
    }
  }
}
