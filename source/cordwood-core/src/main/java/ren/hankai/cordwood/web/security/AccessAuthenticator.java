/**
 *
 */
package ren.hankai.cordwood.web.security;

/**
 * 访问认证接口，用于验证客户端是否有权访问服务。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:14:12 PM
 *
 */
public interface AccessAuthenticator {

  /**
   * 访问令牌字段名。
   */
  static final String ACCESS_TOKEN = "access_token";

  /**
   * 根据鉴权信息生成授权令牌。
   *
   * @param tokenInfo 鉴权信息
   * @return 授权令牌字符串。
   * @author hankai
   * @since Oct 31, 2016 10:17:06 PM
   */
  String generateAccessToken(TokenInfo tokenInfo);

  /**
   * 解析授权令牌。
   *
   * @param tokenString 授权令牌字符串
   * @return 鉴权信息
   * @author hankai
   * @since Oct 31, 2016 10:17:42 PM
   */
  TokenInfo parseAccessToken(String tokenString);

  /**
   * 验证授权令牌是否有效。
   *
   * @param tokenString 授权令牌字符串。
   * @return 验证结果，参考 TokenInfo 中的错误信息定义。
   * @author hankai
   * @since Oct 31, 2016 10:18:03 PM
   */
  int verifyAccessToken(String tokenString);

  /**
   * 鉴权码信息。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jun 28, 2016 1:34:50 PM
   */
  public static class TokenInfo {

    /**
     * 令牌数据错误。
     */
    public static final int TOKEN_ERROR_INVALID = -1;
    /**
     * 令牌已过期。
     */
    public static final int TOKEN_ERROR_EXPIRED = -2;

    private int uid;
    private long expiryTime;

    /**
     * 生成一个将在若干分钟后过期的令牌。
     *
     * @param uid 用户ID
     * @param minutes 分钟数
     * @return 令牌
     * @author hankai
     * @since Oct 27, 2016 6:15:42 PM
     */
    public static TokenInfo withinMinutes(int uid, int minutes) {
      final TokenInfo ti = new TokenInfo();
      ti.setUid(uid);
      final long time = System.currentTimeMillis() + (minutes * 60 * 1000);
      ti.setExpiryTime(time);
      return ti;
    }

    /**
     * 生成一个将在若干小时后过期的令牌。
     *
     * @param uid 用户ID
     * @param hours 小时数
     * @return 令牌
     * @author hankai
     * @since Oct 27, 2016 6:15:42 PM
     */
    public static TokenInfo withinHours(int uid, int hours) {
      return withinMinutes(uid, hours * 60);
    }

    /**
     * 生成一个将在若干天后过期的令牌。
     *
     * @param uid 用户ID
     * @param days 天数
     * @return 令牌
     * @author hankai
     * @since Oct 27, 2016 6:15:42 PM
     */
    public static TokenInfo withinDays(int uid, int days) {
      return withinHours(uid, days * 24);
    }

    public int getUid() {
      return uid;
    }

    public void setUid(int uid) {
      this.uid = uid;
    }

    public long getExpiryTime() {
      return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
      this.expiryTime = expiryTime;
    }
  }
}
