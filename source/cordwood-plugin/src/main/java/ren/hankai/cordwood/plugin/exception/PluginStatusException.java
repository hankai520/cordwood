package ren.hankai.cordwood.plugin.exception;

/**
 * 插件状态不正常。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 10:02:27 AM
 */
public class PluginStatusException extends PluginException {

  private static final long serialVersionUID = 1L;

  public PluginStatusException() {}

  public PluginStatusException(String message) {
    super(message);
  }

  public PluginStatusException(Throwable cause) {
    super(cause);
  }

  public PluginStatusException(String message, Throwable cause) {
    super(message, cause);
  }
}
