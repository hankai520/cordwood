
package ren.hankai.cordwood.console.view.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ren.hankai.cordwood.jackson.TimeInMillisecondSerializer;

import java.util.Date;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Jan 3, 2017 1:49:14 PM
 */
public class RequestCountAndVolume {

  private Date createDate;
  private Long count;
  private Long bytes;

  public RequestCountAndVolume(Date createDate, Long count, Long bytes) {
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
  public Long getBytes() {
    return bytes;
  }

  /**
   * 设置 bytes 字段的值。
   *
   * @param bytes bytes 字段的值
   */
  public void setBytes(Long bytes) {
    this.bytes = bytes;
  }

}
