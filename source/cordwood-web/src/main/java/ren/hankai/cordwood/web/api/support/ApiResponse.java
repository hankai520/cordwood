
package ren.hankai.cordwood.web.api.support;

import java.io.Serializable;

/**
 * API 返回的响应信息包装类。
 *
 * @author hankai
 * @version 1.0
 * @since Jan 7, 2016 3:09:54 PM
 */
public class ApiResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  private ApiCode code = ApiCode.InternalError;
  private String message;
  private ApiResponseBody body = new ApiResponseBody();

  public ApiCode getCode() {
    return code;
  }

  public void setCode(final ApiCode code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public ApiResponseBody getBody() {
    return body;
  }

  public void setBody(final ApiResponseBody body) {
    this.body = body;
  }
}
