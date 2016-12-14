
package ren.hankai.cordwood.console.view.model;

/**
 * 插件的渠道访问信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 14, 2016 11:41:44 AM
 */
public class ChannelRequest {

  private long desktopCount;
  private long tabletCount;
  private long mobileCount;
  private long otherCount;

  /**
   * 获取请求总数。
   *
   * @return 请求总数
   * @author hankai
   * @since Dec 14, 2016 1:23:45 PM
   */
  public long getTotalCount() {
    return desktopCount + tabletCount + mobileCount + otherCount;
  }

  /**
   * 获取 desktopPercent 字段的值。
   *
   * @return desktopPercent 字段值
   */
  public int getDesktopPercent() {
    final long total = getTotalCount();
    if (total == 0) {
      return 0;
    }
    return (int) (((float) desktopCount / total) * 100);
  }

  /**
   * 获取 tabletPercent 字段的值。
   *
   * @return tabletPercent 字段值
   */
  public int getTabletPercent() {
    final long total = getTotalCount();
    if (total == 0) {
      return 0;
    }
    return (int) (((float) tabletCount / total) * 100);
  }

  /**
   * 获取 mobilePercent 字段的值。
   *
   * @return mobilePercent 字段值
   */
  public int getMobilePercent() {
    final long total = getTotalCount();
    if (total == 0) {
      return 0;
    }
    return (int) (((float) mobileCount / total) * 100);
  }

  /**
   * 获取 otherPercent 字段的值。
   *
   * @return otherPercent 字段值
   */
  public int getOtherPercent() {
    final long total = getTotalCount();
    if (total == 0) {
      return 0;
    }
    return (int) (((float) otherCount / total) * 100);
  }

  /**
   * 获取 desktopCount 字段的值。
   *
   * @return desktopCount 字段值
   */
  public long getDesktopCount() {
    return desktopCount;
  }

  /**
   * 设置 desktopCount 字段的值。
   *
   * @param desktopCount desktopCount 字段的值
   */
  public void setDesktopCount(long desktopCount) {
    this.desktopCount = desktopCount;
  }

  /**
   * 获取 tabletCount 字段的值。
   *
   * @return tabletCount 字段值
   */
  public long getTabletCount() {
    return tabletCount;
  }

  /**
   * 设置 tabletCount 字段的值。
   *
   * @param tabletCount tabletCount 字段的值
   */
  public void setTabletCount(long tabletCount) {
    this.tabletCount = tabletCount;
  }

  /**
   * 获取 mobileCount 字段的值。
   *
   * @return mobileCount 字段值
   */
  public long getMobileCount() {
    return mobileCount;
  }

  /**
   * 设置 mobileCount 字段的值。
   *
   * @param mobileCount mobileCount 字段的值
   */
  public void setMobileCount(long mobileCount) {
    this.mobileCount = mobileCount;
  }

  /**
   * 获取 otherCount 字段的值。
   *
   * @return otherCount 字段值
   */
  public long getOtherCount() {
    return otherCount;
  }

  /**
   * 设置 otherCount 字段的值。
   *
   * @param otherCount otherCount 字段的值
   */
  public void setOtherCount(long otherCount) {
    this.otherCount = otherCount;
  }

}
