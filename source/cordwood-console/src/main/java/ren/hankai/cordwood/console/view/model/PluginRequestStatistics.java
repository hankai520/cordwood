
package ren.hankai.cordwood.console.view.model;

import java.util.List;

/**
 * 用户插件访问统计。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 1:04:04 PM
 */
public class PluginRequestStatistics {

  private long accessCount; // 访问次数
  private double timeUsageAvg; // 平均响应时间
  private int faultRate; // 故障率的百分值
  private int usageRage; // 使用率的百分值
  private int dataShare; // 流量占比的百分值
  private List<SummarizedRequest> summarizedRequests; // 按插件分组汇总后的访问信息
  private List<String> leaderboard; // 插件排行榜

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
   * 获取 summarizedRequests 字段的值。
   *
   * @return summarizedRequests 字段值
   */
  public List<SummarizedRequest> getSummarizedRequests() {
    return summarizedRequests;
  }

  /**
   * 设置 summarizedRequests 字段的值。
   *
   * @param summarizedRequests summarizedRequests 字段的值
   */
  public void setSummarizedRequests(List<SummarizedRequest> summarizedRequests) {
    this.summarizedRequests = summarizedRequests;
  }

  /**
   * 获取 leaderboard 字段的值。
   *
   * @return leaderboard 字段值
   */
  public List<String> getLeaderboard() {
    return leaderboard;
  }

  /**
   * 设置 leaderboard 字段的值。
   *
   * @param leaderboard leaderboard 字段的值
   */
  public void setLeaderboard(List<String> leaderboard) {
    this.leaderboard = leaderboard;
  }
}
