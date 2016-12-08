
package ren.hankai.cordwood.core.api.support;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * API 响应代码。
 *
 * @author hankai
 * @version 1.0
 * @since Jan 7, 2016 3:11:54 PM
 */
public enum ApiCode {
  /**
   * API 已禁用。
   */
  ApiDisabled(-2),
  /**
   * 签名错误。
   */
  BadSignature(-1),
  /**
   * 成功。
   */
  Success(1),
  /**
   * 参数错误。
   */
  BadParams(2),
  /**
   * 系统内部错误。
   */
  InternalError(3),
  /**
   * 未知错误。
   */
  UnknownError(4),
  /**
   * 需要授权。
   */
  AuthorizationRequired(5),;

  /**
   * 从整型数字构造 API 响应代码。
   *
   * @param value 整型数字
   * @return 响应代码
   * @author hankai
   * @since Nov 22, 2016 1:14:28 PM
   */
  @JsonCreator
  public static ApiCode fromInteger(Integer value) {
    if (value == Success.value) {
      return Success;
    } else if (value == BadParams.value) {
      return BadParams;
    } else if (value == UnknownError.value) {
      return UnknownError;
    } else if (value == AuthorizationRequired.value) {
      return AuthorizationRequired;
    } else if (value == BadSignature.value) {
      return BadSignature;
    } else if (value == ApiDisabled.value) {
      return ApiDisabled;
    }
    return null;
  }

  private final int value;

  private ApiCode(int value) {
    this.value = value;
  }

  @JsonValue
  public int value() {
    return value;
  }
}
