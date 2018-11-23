package ren.hankai.cordwood.web.security.exception;

/**
 * 令牌异常。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:53:58 AM
 */
public class AccessTokenException extends Exception {

  // TODO: AccessTokenException 单元测试访问令牌错误的情况
  private static final long serialVersionUID = 1L;

  public AccessTokenException() {}

  public AccessTokenException(String message) {
    super(message);
  }

  public AccessTokenException(Throwable cause) {
    super(cause);
  }

  public AccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
