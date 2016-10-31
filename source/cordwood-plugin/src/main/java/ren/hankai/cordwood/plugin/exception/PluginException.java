package ren.hankai.cordwood.plugin.exception;

/**
 * 插件异常基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 10:14:26 AM
 */
public class PluginException extends Exception {
  private static final long serialVersionUID = 1L;

  public PluginException() {}

  public PluginException(String message) {
    super(message);
  }

  public PluginException(Throwable cause) {
    super(cause);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
