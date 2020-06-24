
package ren.hankai.cordwood.web.api.support;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * API 响应代码。代码值分配规则：负值表示服务器端错误，正值表示客户端错误；代码绝对值越大，表示错误越严重。
 *
 * @author hankai
 * @version 1.0
 * @since Jan 7, 2016 3:11:54 PM
 */
public enum ApiCode {

  /**
   * 未知错误（-2）。
   */
  UnknownError(-2),
  /**
   * 系统内部错误（-1）。
   */
  InternalError(-1),
  /**
   * 成功（1）。
   */
  Success(1),
  /**
   * API 已禁用（2）。
   */
  ApiDisabled(2),
  /**
   * 需要授权（3）。
   */
  AuthorizationRequired(3),
  /**
   * 参数错误（4）。
   */
  BadParams(4),
  /**
   * 签名错误（5）。
   */
  BadSignature(5),
  ;

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
    if (value == UnknownError.value) {
      return UnknownError;
    } else if (value == InternalError.value) {
      return InternalError;
    } else if (value == Success.value) {
      return Success;
    } else if (value == ApiDisabled.value) {
      return ApiDisabled;
    } else if (value == AuthorizationRequired.value) {
      return AuthorizationRequired;
    } else if (value == BadParams.value) {
      return BadParams;
    } else if (value == BadSignature.value) {
      return BadSignature;
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
