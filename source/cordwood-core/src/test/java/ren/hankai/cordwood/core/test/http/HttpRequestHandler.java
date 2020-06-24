
package ren.hankai.cordwood.core.test.http;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * HTTP请求处理器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 3, 2018 9:41:16 AM
 */
public interface HttpRequestHandler {

  /**
   * 处理HTTP请求。
   *
   * @param request 请求
   * @param response 响应
   * @throws Exception 异常
   * @author hankai
   * @since Dec 3, 2018 10:04:20 AM
   */
  public void handle(Request request, Response response) throws Exception;
}
