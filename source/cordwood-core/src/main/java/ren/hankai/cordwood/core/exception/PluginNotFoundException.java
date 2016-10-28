package ren.hankai.cordwood.core.exception;

/**
 * 插件未找到。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 10:02:27 AM
 */
public class PluginNotFoundException extends PluginException {

  private static final long serialVersionUID = 1L;

  public PluginNotFoundException() {}

  public PluginNotFoundException(String message) {
    super(message);
  }

  public PluginNotFoundException(Throwable cause) {
    super(cause);
  }

  public PluginNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
