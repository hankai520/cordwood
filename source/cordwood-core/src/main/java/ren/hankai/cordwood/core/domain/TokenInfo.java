
package ren.hankai.cordwood.core.domain;

/**
 * 鉴权码信息
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2016 1:34:50 PM
 */
public class TokenInfo {

  /**
   * 令牌数据错误。
   */
  public static final int TOKEN_ERROR_INVALID = -1;
  /**
   * 令牌已过期
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
    TokenInfo ti = new TokenInfo();
    ti.setUid(uid);
    long time = System.currentTimeMillis() + (minutes * 60 * 1000);
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
