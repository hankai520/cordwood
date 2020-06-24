
package ren.hankai.cordwood.oauth2.core;

/**
 * OAuth 2.0 支持的授权类型。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:40:32 PM
 */
public enum GrantType {

  /**
   * 密码方式（password）。
   */
  Password("password"),
  /**
   * 客户端凭证（client_credentials）。
   */
  ClientCredentials("client_credentials"),
  /**
   * 成功（authorization_code）。
   */
  AuthorizationCode("authorization_code"),
  /**
   * 刷新令牌（refresh_token）。
   */
  RefreshToken("refresh_token"),
  ;

  /**
   * 根据字符串构造授权类型枚举。
   *
   * @param value 枚举对应的字符串值
   * @return 授权类型枚举
   */
  public static GrantType fromString(final String value) {
    if (Password.value.equals(value)) {
      return Password;
    } else if (ClientCredentials.value.equals(value)) {
      return ClientCredentials;
    } else if (AuthorizationCode.value.equals(value)) {
      return AuthorizationCode;
    } else if (RefreshToken.value.equals(value)) {
      return RefreshToken;
    }
    return null;
  }

  private final String value;

  private GrantType(final String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
