
package ren.hankai.cordwood.web.api.exception;

/**
 * Ajax接口异常基类，用于在web应用中处理ajax调用时发生的异常。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 2:13:24 PM
 */
public class AjaxException extends ApiException {

  private static final long serialVersionUID = 1L;

  public AjaxException(String code, String message) {
    super(code, message);
  }
}
