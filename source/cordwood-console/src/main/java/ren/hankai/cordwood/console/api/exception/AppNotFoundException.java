
package ren.hankai.cordwood.console.api.exception;

import ren.hankai.cordwood.core.api.exception.ApiException;

/**
 * APP 不存在。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 2:18:39 PM
 */
public class AppNotFoundException extends ApiException {

  private static final long serialVersionUID = 1L;

  public AppNotFoundException(String code) {
    super(code);
  }

  public AppNotFoundException(String code, String message) {
    super(code, message);
  }

}
