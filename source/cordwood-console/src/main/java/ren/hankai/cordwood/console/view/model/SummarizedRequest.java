
package ren.hankai.cordwood.console.view.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ren.hankai.cordwood.jackson.DateTimeSerializer;

import java.util.Date;

/**
 * 按插件分组合并后的访问信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 1:10:06 PM
 */
public final class SummarizedRequest {
  private String pluginName; // 插件显示名称
  private boolean pluginIsActive; // 插件是否已启用
  private double inboundBytes; // 输入流量 KB
  private double outboundBytes; // 输出流量 KB
  private long accessCount; // 访问次数
  private double timeUsageAvg; // 平均耗时 ms
  private Date lastAccessTime; // 最近一次访问的时间

  /**
   * 获取 pluginName 字段的值。
   *
   * @return pluginName 字段值
   */
  public String getPluginName() {
    return pluginName;
  }

  /**
   * 设置 pluginName 字段的值。
   *
   * @param pluginName pluginName 字段的值
   */
  public void setPluginName(String pluginName) {
    this.pluginName = pluginName;
  }

  /**
   * 获取 pluginIsActive 字段的值。
   *
   * @return pluginIsActive 字段值
   */
  public boolean isPluginIsActive() {
    return pluginIsActive;
  }

  /**
   * 设置 pluginIsActive 字段的值。
   *
   * @param pluginIsActive pluginIsActive 字段的值
   */
  public void setPluginIsActive(boolean pluginIsActive) {
    this.pluginIsActive = pluginIsActive;
  }

  /**
   * 获取 inboundBytes 字段的值。
   *
   * @return inboundBytes 字段值
   */
  public double getInboundBytes() {
    return inboundBytes;
  }

  /**
   * 设置 inboundBytes 字段的值。
   *
   * @param inboundBytes inboundBytes 字段的值
   */
  public void setInboundBytes(double inboundBytes) {
    this.inboundBytes = inboundBytes;
  }

  /**
   * 获取 outboundBytes 字段的值。
   *
   * @return outboundBytes 字段值
   */
  public double getOutboundBytes() {
    return outboundBytes;
  }

  /**
   * 设置 outboundBytes 字段的值。
   *
   * @param outboundBytes outboundBytes 字段的值
   */
  public void setOutboundBytes(double outboundBytes) {
    this.outboundBytes = outboundBytes;
  }

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
   * 获取 lastAccessTime 字段的值。
   *
   * @return lastAccessTime 字段值
   */
  @JsonSerialize(using = DateTimeSerializer.class)
  public Date getLastAccessTime() {
    return lastAccessTime;
  }

  /**
   * 设置 lastAccessTime 字段的值。
   *
   * @param lastAccessTime lastAccessTime 字段的值
   */
  public void setLastAccessTime(Date lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

}
