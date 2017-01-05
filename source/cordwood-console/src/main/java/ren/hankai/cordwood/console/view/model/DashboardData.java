
package ren.hankai.cordwood.console.view.model;

import ren.hankai.cordwood.core.util.MathUtil;

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
  private double memoryUsage; // 内存使用占比
  private double usedStorage; // 已用硬盘存储空间
  private double totalStorage; // 硬盘总存储空间
  private double storageUsage; // 存储空间使用率
  private double dataVolume; // 日均数据量
  private double dataVolumePercent; // 日均数据量百分比（手工设置上限值）

  /**
   * 平均响应时间描述。
   * 
   * @return 平均响应时间描述。
   * @author hankai
   * @since Jan 5, 2017 10:52:44 AM
   */
  public String getResponseTimeDesc() {
    return MathUtil.toHumanReadableString(responseTime, new long[] {1000, 60, 60, 24},
        new String[] {"ms", "s", "min", "h", "d"});
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
   * 获取 memoryUsage 字段的值。
   *
   * @return memoryUsage 字段值
   */
  public double getMemoryUsage() {
    return memoryUsage;
  }

  /**
   * 设置 memoryUsage 字段的值。
   *
   * @param memoryUsage memoryUsage 字段的值
   */
  public void setMemoryUsage(double memoryUsage) {
    this.memoryUsage = memoryUsage;
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
   * 获取 storageUsage 字段的值。
   *
   * @return storageUsage 字段值
   */
  public double getStorageUsage() {
    return storageUsage;
  }

  /**
   * 设置 storageUsage 字段的值。
   *
   * @param storageUsage storageUsage 字段的值
   */
  public void setStorageUsage(double storageUsage) {
    this.storageUsage = storageUsage;
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

  /**
   * 获取 dataVolumePercent 字段的值。
   *
   * @return dataVolumePercent 字段值
   */
  public double getDataVolumePercent() {
    return dataVolumePercent;
  }

  /**
   * 设置 dataVolumePercent 字段的值。
   *
   * @param dataVolumePercent dataVolumePercent 字段的值
   */
  public void setDataVolumePercent(double dataVolumePercent) {
    this.dataVolumePercent = dataVolumePercent;
  }

}
