
package ren.hankai.cordwood.web.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 鉴权码信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2016 1:34:50 PM
 */
@JsonIgnoreProperties(
    ignoreUnknown = true)
public final class TokenInfo {

  /**
   * 访问令牌字段名（HTTP 请求中的入参名）。
   */
  public static String ACCESS_TOKEN = "access_token";

  /**
   * 令牌数据错误。
   */
  public static final int TOKEN_ERROR_INVALID = -1;
  /**
   * 令牌已过期。
   */
  public static final int TOKEN_ERROR_EXPIRED = -2;

  /**
   * 令牌权限不够。
   */
  public static final int TOKEN_ERROR_NOT_PERMITTED = -3;

  /**
   * 用户唯一标识，用于支持用户验证。
   */
  private String userKey;

  /**
   * 用户秘钥。
   */
  private String userSk;

  /**
   * 到期时间（毫秒）。
   */
  private long expiryTime;

  // 用于支持 OAuth 2.0 客户端验证。
  /**
   * 客户端ID。
   */
  private String clientId;

  /**
   * 授权类型。
   */
  private String grantType;

  /**
   * 授权范围。
   */
  private String scope;

  /**
   * 额外的信息。
   */
  private Map<String, String> extraInfo;

  /**
   * 生成一个将在若干分钟后过期的令牌。
   *
   * @param userKey 用户标识
   * @param userSk 用户秘钥
   * @param minutes 分钟数
   * @return 令牌
   * @author hankai
   * @since Oct 27, 2016 6:15:42 PM
   */
  public static TokenInfo withinMinutes(final String userKey, final String userSk,
      final int minutes) {
    final TokenInfo ti = new TokenInfo();
    ti.setUserKey(userKey);
    ti.setUserSk(userSk);
    final long time = System.currentTimeMillis() + (minutes * 60 * 1000);
    ti.setExpiryTime(time);
    return ti;
  }

  /**
   * 生成一个将在若干小时后过期的令牌。
   *
   * @param userKey 用户标识
   * @param userSk 用户秘钥
   * @param hours 小时数
   * @return 令牌
   * @author hankai
   * @since Oct 27, 2016 6:15:42 PM
   */
  public static TokenInfo withinHours(final String userKey, final String userSk,
      final int hours) {
    return withinMinutes(userKey, userSk, hours * 60);
  }

  /**
   * 生成一个将在若干天后过期的令牌。
   *
   * @param userKey 用户标识
   * @param userSk 用户秘钥
   * @param days 天数
   * @return 令牌
   * @author hankai
   * @since Oct 27, 2016 6:15:42 PM
   */
  public static TokenInfo withinDays(final String userKey, final String userSk, final int days) {
    return withinHours(userKey, userSk, days * 24);
  }

  /**
   * 生成一个永不过期的令牌。
   *
   * @param userKey 用户标识
   * @param userSk 用户秘钥
   * @return 令牌
   * @author hankai
   * @since Dec 8, 2016 2:42:27 PM
   */
  public static TokenInfo neverExpire(final String userKey, final String userSk) {
    final TokenInfo ti = new TokenInfo();
    ti.setUserKey(userKey);
    ti.setUserSk(userSk);
    ti.setExpiryTime(-1);
    return ti;
  }

  /**
   * 是否是 OAuth 2.0 令牌。
   *
   * @return 是否是 OAuth 2.0 令牌。
   */
  public boolean isOauth2() {
    return StringUtils.isNotEmpty(clientId)
        && StringUtils.isNotEmpty(grantType);
  }

  /**
   * 获取 userKey 字段的值。
   *
   * @return userKey 字段值
   */
  public String getUserKey() {
    return userKey;
  }

  /**
   * 设置 userKey 字段的值。
   *
   * @param userKey userKey 字段的值
   */
  public void setUserKey(final String userKey) {
    this.userKey = userKey;
  }

  /**
   * 获取 userSk 字段的值。
   *
   * @return userSk 字段值
   */
  public String getUserSk() {
    return userSk;
  }

  /**
   * 设置 userSk 字段的值。
   *
   * @param userSk userSk 字段的值
   */
  public void setUserSk(final String userSk) {
    this.userSk = userSk;
  }

  /**
   * 获取 expiryTime 字段的值。
   *
   * @return expiryTime 字段值
   */
  public long getExpiryTime() {
    return expiryTime;
  }

  /**
   * 设置 expiryTime 字段的值。
   *
   * @param expiryTime expiryTime 字段的值
   */
  public void setExpiryTime(final long expiryTime) {
    this.expiryTime = expiryTime;
  }

  /**
   * 获取 clientId 字段的值。
   *
   * @return clientId 字段值
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * 设置 clientId 字段的值。
   *
   * @param clientId clientId 字段的值
   */
  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  /**
   * 获取 grantType 字段的值。
   *
   * @return grantType 字段值
   */
  public String getGrantType() {
    return grantType;
  }

  /**
   * 设置 grantType 字段的值。
   *
   * @param grantType grantType 字段的值
   */
  public void setGrantType(final String grantType) {
    this.grantType = grantType;
  }

  /**
   * 获取 scope 字段的值。
   *
   * @return scope 字段值
   */
  public String getScope() {
    return scope;
  }

  /**
   * 设置 scope 字段的值。
   *
   * @param scope scope 字段的值
   */
  public void setScope(final String scope) {
    this.scope = scope;
  }

  /**
   * 获取 extraInfo 字段的值。
   *
   * @return extraInfo 字段值
   */
  public Map<String, String> getExtraInfo() {
    return extraInfo;
  }

  /**
   * 设置 extraInfo 字段的值。
   *
   * @param extraInfo extraInfo 字段的值
   */
  public void setExtraInfo(final Map<String, String> extraInfo) {
    this.extraInfo = extraInfo;
  }

}
