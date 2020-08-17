
package ren.hankai.cordwood.core.cache;

/**
 * 缓存量级。
 *
 * @author hankai
 * @since 1.0.0
 */
public enum CacheWeight {

  /**
   * 重量级缓存（heavyWeight）。
   */
  Heavy("heavyWeight"),
  /**
   * 中量级缓存（middleWeight）。
   */
  Middle("middleWeight"),
  /**
   * 轻量级缓存（lightWeight）。
   */
  Light("lightWeight"),
  ;

  private final String value;

  private CacheWeight(final String value) {
    this.value = value;
  }

  /**
   * 从字符串构造枚举。
   *
   * @param value 字符串
   * @return 缓存量级枚举
   */
  public static CacheWeight fromString(final String value) {
    if (Heavy.value.equals(value)) {
      return Heavy;
    } else if (Middle.value.equals(value)) {
      return Middle;
    } else if (Light.value.equals(value)) {
      return Light;
    }
    return null;
  }

  public String value() {
    return value;
  }
}
