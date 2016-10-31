package ren.hankai.cordwood.plugin.exception;

/**
 * 插件功能未找到。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 10:02:27 AM
 */
public class PluginFunctionNotFoundException extends PluginException {

  private static final long serialVersionUID = 1L;

  public PluginFunctionNotFoundException() {}

  public PluginFunctionNotFoundException(String message) {
    super(message);
  }

  public PluginFunctionNotFoundException(Throwable cause) {
    super(cause);
  }

  public PluginFunctionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
