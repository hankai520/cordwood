
package ren.hankai.cordwood.console.view.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ren.hankai.cordwood.jackson.TimeInMillisecondSerializer;

import java.util.Date;

/**
 * 插件访问次数和访问量。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 3, 2017 1:49:14 PM
 */
public class RequestCountAndVolume {

  private Date createDate;
  private Long count;
  private Float bytes;

  /**
   * 初始化插件访问次数和访问量信息。
   *
   * @param createDate 访问时间
   * @param count 访问次数
   * @param bytes 数据量（字节）
   */
  public RequestCountAndVolume(Date createDate, Long count, Float bytes) {
    this.createDate = createDate;
    this.count = count;
    this.bytes = bytes;
  }

  /**
   * 获取 createDate 字段的值。
   *
   * @return createDate 字段值
   */
  @JsonSerialize(using = TimeInMillisecondSerializer.class)
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * 设置 createDate 字段的值。
   *
   * @param createDate createDate 字段的值
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  /**
   * 获取 count 字段的值。
   *
   * @return count 字段值
   */
  public Long getCount() {
    return count;
  }

  /**
   * 设置 count 字段的值。
   *
   * @param count count 字段的值
   */
  public void setCount(Long count) {
    this.count = count;
  }

  /**
   * 获取 bytes 字段的值。
   *
   * @return bytes 字段值
   */
  public Float getBytes() {
    return bytes;
  }

  /**
   * 设置 bytes 字段的值。
   *
   * @param bytes bytes 字段的值
   */
  public void setBytes(Float bytes) {
    this.bytes = bytes;
  }

}
