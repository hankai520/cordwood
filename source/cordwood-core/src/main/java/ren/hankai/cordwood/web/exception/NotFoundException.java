
package ren.hankai.cordwood.web.exception;

/**
 * 未找到异常（用于web中页面所需数据、页面等未找到时抛出的异常）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 24, 2017 10:00:23 AM
 */
public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
