
package ren.hankai.cordwood.core.test.http;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.core.util.HttpSslUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 微型HTTP服务器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 3, 2018 9:42:05 AM
 */
public class TinyHttpServer implements Container {

  private static final Logger logger = LoggerFactory.getLogger(TinyHttpServer.class);

  private final HttpRequestHandler handler;
  private final Connection connection;
  private final Server server;

  /**
   * 构建 Web 容器。
   * @param handler 请求处理器
   * @throws Exception 异常
   */
  public TinyHttpServer(HttpRequestHandler handler) throws Exception {
    this.server = new ContainerServer(this, 10);
    this.connection = new SocketConnection(server);
    this.handler = handler;
  }

  /**
   * 启动web容器。
   *
   * @author hankai
   * @since Dec 3, 2018 9:12:10 AM
   */
  public void start(int port, boolean secure) {
    try {
      final SocketAddress address = new InetSocketAddress(port);
      if (secure) {
        HttpSslUtil.trustAllHostnames();
        connection.connect(address, HttpSslUtil.trustAllHttpsCertificates());
      } else {
        connection.connect(address);
      }
    } catch (final Exception ex) {
      logger.error("Failed to start web container.", ex);
    }
  }

  /**
   * 停止web容器。
   *
   * @author hankai
   * @since Dec 3, 2018 9:10:36 AM
   */
  public void stop() {
    try {
      connection.close();
      server.stop();
    } catch (final Exception ex) {
      logger.error("Failed to stop web container.", ex);
    }
  }

  @Override
  public void handle(Request req, Response resp) {
    try {
      handler.handle(req, resp);
    } catch (final Throwable ex) {
      logger.error("Failed to handle web request.", ex);
    } finally {
      try {
        resp.close();
      } catch (final Exception expected) {
      }
    }
  }

}
