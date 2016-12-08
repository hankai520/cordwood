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

    private String userKey; // 用户唯一标识
    private String userSk; // 用户秘钥
    private long expiryTime;

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
    public static TokenInfo withinMinutes(String userKey, String userSk, int minutes) {
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
    public static TokenInfo withinHours(String userKey, String userSk, int hours) {
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
    public static TokenInfo withinDays(String userKey, String userSk, int days) {
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
    public static TokenInfo neverExpire(String userKey, String userSk) {
      final TokenInfo ti = new TokenInfo();
      ti.setUserKey(userKey);
      ti.setUserSk(userSk);
      ti.setExpiryTime(-1);
      return ti;
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
    public void setUserKey(String userKey) {
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
    public void setUserSk(String userSk) {
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
    public void setExpiryTime(long expiryTime) {
      this.expiryTime = expiryTime;
    }

  }
}
