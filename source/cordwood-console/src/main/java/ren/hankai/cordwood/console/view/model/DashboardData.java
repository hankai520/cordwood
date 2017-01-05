
package ren.hankai.cordwood.console.view.model;

import ren.hankai.cordwood.core.util.MathUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 仪表盘页面所需数据。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 4, 2017 4:19:19 PM
 */
public class DashboardData {

  private long numberOfPlugins; // 插件数
  private long numberOfDevelopers; // 开发者数
  private double responseTime; // 平均响应时间
  private double faultRate; // 故障率
  private double usedMemory; // 已用内存
  private double totalMemory; // 总内存
  private double usedStorage; // 已用硬盘存储空间
  private double totalStorage; // 硬盘总存储空间
  private double dataVolume; // 日均数据量

  /**
   * 平均响应时间描述。
   *
   * @return 平均响应时间描述
   * @author hankai
   * @since Jan 5, 2017 10:52:44 AM
   */
  public String getResponseTimeDesc() {
    return MathUtil.toHumanReadableString(responseTime, new long[] {1000, 60, 60, 24},
        new String[] {"ms", "s", "min", "h", "d"});
  }

  /**
   * 获取已用内存描述。
   *
   * @return 已用内存描述
   * @author hankai
   * @since Jan 5, 2017 1:17:26 PM
   */
  public String getUsedMemoryDesc() {
    return MathUtil.toHumanReadableString(usedMemory, new long[] {1024},
        new String[] {"B", "KB", "MB", "GB", "TB"});
  }

  /**
   * 获取内存总大小描述。
   *
   * @return 内存总大小描述
   * @author hankai
   * @since Jan 5, 2017 1:17:49 PM
   */
  public String getTotalMemoryDesc() {
    return MathUtil.toHumanReadableString(totalMemory, new long[] {1024},
        new String[] {"B", "KB", "MB", "GB", "TB"});
  }

  /**
   * 获取内存使用率描述。
   *
   * @return 内存使用率百分比
   * @author hankai
   * @since Jan 5, 2017 1:10:32 PM
   */
  public String getMemoryUsage() {
    if (totalMemory > 0) {
      final double usage = (usedMemory / totalMemory) * 100;
      final DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(2);
      df.setMaximumIntegerDigits(3);
      df.setRoundingMode(RoundingMode.HALF_UP);
      return df.format(usage);
    }
    return "0";
  }

  /**
   * 获取系统已使用存储空间大小的描述。
   *
   * @return 系统已使用存储空间大小的描述
   * @author hankai
   * @since Jan 5, 2017 1:48:21 PM
   */
  public String getUsedStorageDesc() {
    return MathUtil.toHumanReadableString(usedStorage, new long[] {1024},
        new String[] {"B", "KB", "MB", "GB", "TB"});
  }

  /**
   * 获取系统存储空间总大小描述。
   *
   * @return 系统存储空间总大小描述
   * @author hankai
   * @since Jan 5, 2017 1:48:23 PM
   */
  public String getTotalStorageDesc() {
    return MathUtil.toHumanReadableString(totalStorage, new long[] {1024},
        new String[] {"B", "KB", "MB", "GB", "TB"});
  }

  /**
   * 获取存储空间使用率。
   *
   * @return 存储空间使用率百分比
   * @author hankai
   * @since Jan 5, 2017 1:11:37 PM
   */
  public String getStorageUsage() {
    if (totalStorage > 0) {
      final double usage = (usedStorage / totalStorage) * 100;
      final DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(2);
      df.setMaximumIntegerDigits(3);
      df.setRoundingMode(RoundingMode.HALF_UP);
      return df.format(usage);
    }
    return "0";
  }

  /**
   * 获取日均流量描述。
   *
   * @return 日均流量描述
   * @author hankai
   * @since Jan 5, 2017 2:45:49 PM
   */
  public String getDataVolumeDesc() {
    return MathUtil.toHumanReadableString(dataVolume, new long[] {1024},
        new String[] {"B", "KB", "MB", "GB", "TB"});
  }

  /**
   * 获取日均流量与流量上限之比（百分比）。
   *
   * @return 日均流量与流量上限之比（百分比）
   * @author hankai
   * @since Jan 5, 2017 2:54:51 PM
   */
  public String getDataVolumePercent() {
    final DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(3);
    df.setMaximumIntegerDigits(3);
    df.setRoundingMode(RoundingMode.HALF_UP);
    // TODO: 硬编码的日均流量上限
    final long max = 1024 * 1024 * 6;// 6MB
    final double percent = (dataVolume / max) * 100;
    return df.format(percent);
  }

  /**
   * 获取 numberOfPlugins 字段的值。
   *
   * @return numberOfPlugins 字段值
   */
  public long getNumberOfPlugins() {
    return numberOfPlugins;
  }

  /**
   * 设置 numberOfPlugins 字段的值。
   *
   * @param numberOfPlugins numberOfPlugins 字段的值
   */
  public void setNumberOfPlugins(long numberOfPlugins) {
    this.numberOfPlugins = numberOfPlugins;
  }

  /**
   * 获取 numberOfDevelopers 字段的值。
   *
   * @return numberOfDevelopers 字段值
   */
  public long getNumberOfDevelopers() {
    return numberOfDevelopers;
  }

  /**
   * 设置 numberOfDevelopers 字段的值。
   *
   * @param numberOfDevelopers numberOfDevelopers 字段的值
   */
  public void setNumberOfDevelopers(long numberOfDevelopers) {
    this.numberOfDevelopers = numberOfDevelopers;
  }

  /**
   * 获取 responseTime 字段的值。
   *
   * @return responseTime 字段值
   */
  public double getResponseTime() {
    return responseTime;
  }

  /**
   * 设置 responseTime 字段的值。
   *
   * @param responseTime responseTime 字段的值
   */
  public void setResponseTime(double responseTime) {
    this.responseTime = responseTime;
  }

  /**
   * 获取 faultRate 字段的值。
   *
   * @return faultRate 字段值
   */
  public double getFaultRate() {
    return faultRate;
  }

  /**
   * 设置 faultRate 字段的值。
   *
   * @param faultRate faultRate 字段的值
   */
  public void setFaultRate(double faultRate) {
    this.faultRate = faultRate;
  }

  /**
   * 获取 usedMemory 字段的值。
   *
   * @return usedMemory 字段值
   */
  public double getUsedMemory() {
    return usedMemory;
  }

  /**
   * 设置 usedMemory 字段的值。
   *
   * @param usedMemory usedMemory 字段的值
   */
  public void setUsedMemory(double usedMemory) {
    this.usedMemory = usedMemory;
  }

  /**
   * 获取 totalMemory 字段的值。
   *
   * @return totalMemory 字段值
   */
  public double getTotalMemory() {
    return totalMemory;
  }

  /**
   * 设置 totalMemory 字段的值。
   *
   * @param totalMemory totalMemory 字段的值
   */
  public void setTotalMemory(double totalMemory) {
    this.totalMemory = totalMemory;
  }


  /**
   * 获取 usedStorage 字段的值。
   *
   * @return usedStorage 字段值
   */
  public double getUsedStorage() {
    return usedStorage;
  }

  /**
   * 设置 usedStorage 字段的值。
   *
   * @param usedStorage usedStorage 字段的值
   */
  public void setUsedStorage(double usedStorage) {
    this.usedStorage = usedStorage;
  }

  /**
   * 获取 totalStorage 字段的值。
   *
   * @return totalStorage 字段值
   */
  public double getTotalStorage() {
    return totalStorage;
  }

  /**
   * 设置 totalStorage 字段的值。
   *
   * @param totalStorage totalStorage 字段的值
   */
  public void setTotalStorage(double totalStorage) {
    this.totalStorage = totalStorage;
  }

  /**
   * 获取 dataVolume 字段的值。
   *
   * @return dataVolume 字段值
   */
  public double getDataVolume() {
    return dataVolume;
  }

  /**
   * 设置 dataVolume 字段的值。
   *
   * @param dataVolume dataVolume 字段的值
   */
  public void setDataVolume(double dataVolume) {
    this.dataVolume = dataVolume;
  }
}
