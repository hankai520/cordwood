
package ren.hankai.cordwood.console.config.tomcat;

import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Component;
import ren.hankai.cordwood.console.config.Route;

/**
 * 自定义servlet容器配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jul 14, 2015 10:08:48 AM
 */
@Component
public class ContainerConfig implements EmbeddedServletContainerCustomizer {

  @Autowired
  private ConnectorConfig connectorConfig;

  /*
   * 自定义 tomcat container 配置，例如自定义错误页面，自定义转发的头消息。
   *
   * @see
   * org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer#customize(org.
   * springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer)
   */
  @Override
  public void customize(ConfigurableEmbeddedServletContainer container) {
    container.addErrorPages(
        // Http 错误
        new ErrorPage(HttpStatus.BAD_REQUEST, Route.ERROR_PREFIX + "/400"),
        new ErrorPage(HttpStatus.FORBIDDEN, Route.ERROR_PREFIX + "/403"),
        new ErrorPage(HttpStatus.NOT_FOUND, Route.ERROR_PREFIX + "/404"),
        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, Route.ERROR_PREFIX + "/500"),
        /*
         * cookie被盗用。 或者登陆后cookie中的信息未被持久化到数据库。在使用HSQLDB时，由于HSQLDB不会
         * 在每次写操作之后都立即将数据写入文件，此时如果断电或服务重启，会导致发回给客户端的cookie中的登录凭证实际上未被持久化，于是刷新页面后可能碰到此异常。
         */
        new ErrorPage(CookieTheftException.class, Route.BG_LOGIN),
        // 异常
        new ErrorPage(Exception.class, Route.ERROR_PREFIX),
        new ErrorPage(Error.class, Route.ERROR_PREFIX));
    if (container instanceof TomcatEmbeddedServletContainerFactory) {
      final TomcatEmbeddedServletContainerFactory cf =
          (TomcatEmbeddedServletContainerFactory) container;
      cf.addConnectorCustomizers(connectorConfig);
      final RemoteIpValve riv = new RemoteIpValve();
      riv.setRemoteIpHeader("x-forwarded-for");
      riv.setProxiesHeader("x-forwarded-by");
      riv.setProtocolHeader("x-forwarded-proto");
      cf.addContextValves(riv);
    }
  }
}
