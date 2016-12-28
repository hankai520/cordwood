
package ren.hankai.cordwood.console.view.model;

/**
 * 用户插件访问统计。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 1:04:04 PM
 */
public class PluginRequestStatistics {

  private long accessCount; // 访问次数
  private String accessCountDesc; // 访问次数描述信息
  private double timeUsageAvg; // 平均响应时间
  private String timeUsageAvgDesc; // 平均响应时间描述信息
  private int faultRate; // 故障率的百分值
  private int usageRage; // 使用率的百分值
  private int dataShare; // 流量占比的百分值
  private ChannelRequest channelRequest; // 渠道统计

  /**
   * 获取 accessCount 字段的值。
   *
   * @return accessCount 字段值
   */
  public long getAccessCount() {
    return accessCount;
  }

  /**
   * 设置 accessCount 字段的值。
   *
   * @param accessCount accessCount 字段的值
   */
  public void setAccessCount(long accessCount) {
    this.accessCount = accessCount;
  }

  /**
   * 获取 accessCountDesc 字段的值。
   *
   * @return accessCountDesc 字段值
   */
  public String getAccessCountDesc() {
    return accessCountDesc;
  }

  /**
   * 设置 accessCountDesc 字段的值。
   *
   * @param accessCountDesc accessCountDesc 字段的值
   */
  public void setAccessCountDesc(String accessCountDesc) {
    this.accessCountDesc = accessCountDesc;
  }

  /**
   * 获取 timeUsageAvg 字段的值。
   *
   * @return timeUsageAvg 字段值
   */
  public double getTimeUsageAvg() {
    return timeUsageAvg;
  }

  /**
   * 设置 timeUsageAvg 字段的值。
   *
   * @param timeUsageAvg timeUsageAvg 字段的值
   */
  public void setTimeUsageAvg(double timeUsageAvg) {
    this.timeUsageAvg = timeUsageAvg;
  }

  /**
   * 获取 timeUsageAvgDesc 字段的值。
   *
   * @return timeUsageAvgDesc 字段值
   */
  public String getTimeUsageAvgDesc() {
    return timeUsageAvgDesc;
  }

  /**
   * 设置 timeUsageAvgDesc 字段的值。
   *
   * @param timeUsageAvgDesc timeUsageAvgDesc 字段的值
   */
  public void setTimeUsageAvgDesc(String timeUsageAvgDesc) {
    this.timeUsageAvgDesc = timeUsageAvgDesc;
  }

  /**
   * 获取 faultRate 字段的值。
   *
   * @return faultRate 字段值
   */
  public int getFaultRate() {
    return faultRate;
  }

  /**
   * 设置 faultRate 字段的值。
   *
   * @param faultRate faultRate 字段的值
   */
  public void setFaultRate(int faultRate) {
    this.faultRate = faultRate;
  }

  /**
   * 获取 usageRage 字段的值。
   *
   * @return usageRage 字段值
   */
  public int getUsageRage() {
    return usageRage;
  }

  /**
   * 设置 usageRage 字段的值。
   *
   * @param usageRage usageRage 字段的值
   */
  public void setUsageRage(int usageRage) {
    this.usageRage = usageRage;
  }

  /**
   * 获取 dataShare 字段的值。
   *
   * @return dataShare 字段值
   */
  public int getDataShare() {
    return dataShare;
  }

  /**
   * 设置 dataShare 字段的值。
   *
   * @param dataShare dataShare 字段的值
   */
  public void setDataShare(int dataShare) {
    this.dataShare = dataShare;
  }

  /**
   * 获取 channelRequest 字段的值。
   *
   * @return channelRequest 字段值
   */
  public ChannelRequest getChannelRequest() {
    return channelRequest;
  }

  /**
   * 设置 channelRequest 字段的值。
   *
   * @param channelRequest channelRequest 字段的值
   */
  public void setChannelRequest(ChannelRequest channelRequest) {
    this.channelRequest = channelRequest;
  }

}
