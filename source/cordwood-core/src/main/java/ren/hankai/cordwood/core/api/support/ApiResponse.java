
package ren.hankai.cordwood.core.api.support;

/**
 * API 返回的响应信息包装类。
 *
 * @author hankai
 * @version 1.0
 * @since Jan 7, 2016 3:09:54 PM
 */
public class ApiResponse {

  private ApiCode code = ApiCode.InternalError;
  private String message;
  private ApiResponseBody body = new ApiResponseBody();

  public ApiCode getCode() {
    return code;
  }

  public void setCode(ApiCode code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ApiResponseBody getBody() {
    return body;
  }

  public void setBody(ApiResponseBody body) {
    this.body = body;
  }
}
