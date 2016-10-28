package ren.hankai.cordwood.core.exception;

/**
 * 插件资源未找到。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 10:02:27 AM
 */
public class PluginResourceNotFoundException extends PluginException {

  private static final long serialVersionUID = 1L;

  public PluginResourceNotFoundException() {}

  public PluginResourceNotFoundException(String message) {
    super(message);
  }

  public PluginResourceNotFoundException(Throwable cause) {
    super(cause);
  }

  public PluginResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
