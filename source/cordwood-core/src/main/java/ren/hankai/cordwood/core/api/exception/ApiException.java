
package ren.hankai.cordwood.core.api.exception;

/**
 * API异常基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 2:13:24 PM
 */
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected final String code;

  public ApiException(String code) {
    super();
    this.code = code;
  }

  public ApiException(String code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * 获取 code 字段的值。
   *
   * @return code 字段值
   */
  public String getCode() {
    return code;
  }

}
