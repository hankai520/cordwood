
package ren.hankai.cordwood.core.api.exception;

/**
 * Ajax接口异常基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 2:13:24 PM
 */
public class AjaxException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AjaxException(String message, Throwable cause) {
    super(message, cause);
  }
}
