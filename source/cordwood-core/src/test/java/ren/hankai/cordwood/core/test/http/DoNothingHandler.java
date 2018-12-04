
package ren.hankai.cordwood.core.test.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * 用于测试的http请求处理器，内部不含逻辑，仅做固定响应。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 3, 2018 10:03:37 AM
 */
public class DoNothingHandler implements HttpRequestHandler {

  private String rawResponse;

  /**
   * 构造处理器。
   *
   * @param rawResponse 指定的响应内容，传入此参数，则所有请求的响应体均为此字符串
   */
  public DoNothingHandler(String rawResponse) {
    if (StringUtils.isNotEmpty(rawResponse)) {
      this.rawResponse = rawResponse;
    } else {
      this.rawResponse = "";
    }
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    response.setValue("Server", "TinyServer/1.0");
    response.setDate("Date", new Date().getTime());
    response.setDate("Last-Modified", new Date().getTime());

    final PrintStream output = response.getPrintStream();
    final ByteArrayInputStream input = new ByteArrayInputStream(this.rawResponse.getBytes("UTF-8"));
    IOUtils.copy(input, output);
    output.flush();
    response.close();
  }

}
