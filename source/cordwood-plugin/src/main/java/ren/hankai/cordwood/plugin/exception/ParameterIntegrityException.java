package ren.hankai.cordwood.plugin.exception;

/**
 * 参数完整性异常。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:54:57 AM
 */
public class ParameterIntegrityException extends PluginException {

  private static final long serialVersionUID = 1L;

  public ParameterIntegrityException() {}

  public ParameterIntegrityException(String message) {
    super(message);
  }

  public ParameterIntegrityException(Throwable cause) {
    super(cause);
  }

  public ParameterIntegrityException(String message, Throwable cause) {
    super(message, cause);
  }
}
