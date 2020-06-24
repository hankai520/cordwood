
package ren.hankai.cordwood.web.api.support;

import java.io.Serializable;

/**
 * API 响应的内容部分。
 *
 * @author hankai
 * @version 1.0
 * @since Jan 7, 2016 4:32:46 PM
 */
public class ApiResponseBody implements Serializable {

  private static final long serialVersionUID = 1L;

  private boolean success = false;
  private String error;
  private String message;
  private Object data;

  public String getError() {
    return error;
  }

  public void setError(final String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(final Object data) {
    this.data = data;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }
}
