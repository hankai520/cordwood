
package ren.hankai.cordwood.console.persist.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ren.hankai.cordwood.jackson.DateTimeSerializer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * 应用信息（客户端通过应用身份信息访问插件服务或 cordwood 的 web 服务）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 7, 2016 5:20:35 PM
 */
@Entity
@Table(name = "apps")
@Cacheable(false)
public class AppBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(length = 20, nullable = false, unique = true)
  @Size(min = 1, max = 20)
  private String name;
  @Column(length = 200, nullable = false)
  @Size(min = 1, max = 180)
  private String introduction;
  private AppPlatform platform;
  @Column(length = 120, nullable = false, unique = true)
  private String appKey; // app 唯一标识
  @Column(length = 120, nullable = false)
  private String secretKey; // app 秘钥
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  /**
   * 获取 id 字段的值。
   *
   * @return id 字段值
   */
  public Integer getId() {
    return id;
  }



  /**
   * 设置 id 字段的值。
   *
   * @param id id 字段的值
   */
  public void setId(Integer id) {
    this.id = id;
  }



  /**
   * 获取 name 字段的值。
   *
   * @return name 字段值
   */
  public String getName() {
    return name;
  }



  /**
   * 设置 name 字段的值。
   *
   * @param name name 字段的值
   */
  public void setName(String name) {
    this.name = name;
  }



  /**
   * 获取 introduction 字段的值。
   *
   * @return introduction 字段值
   */
  public String getIntroduction() {
    return introduction;
  }



  /**
   * 设置 introduction 字段的值。
   *
   * @param introduction introduction 字段的值
   */
  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }



  /**
   * 获取 platform 字段的值。
   *
   * @return platform 字段值
   */
  public AppPlatform getPlatform() {
    return platform;
  }



  /**
   * 设置 platform 字段的值。
   *
   * @param platform platform 字段的值
   */
  public void setPlatform(AppPlatform platform) {
    this.platform = platform;
  }



  /**
   * 获取 appKey 字段的值。
   *
   * @return appKey 字段值
   */
  public String getAppKey() {
    return appKey;
  }



  /**
   * 设置 appKey 字段的值。
   *
   * @param appKey appKey 字段的值
   */
  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }



  /**
   * 获取 secretKey 字段的值。
   *
   * @return secretKey 字段值
   */
  public String getSecretKey() {
    return secretKey;
  }



  /**
   * 设置 secretKey 字段的值。
   *
   * @param secretKey secretKey 字段的值
   */
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }



  /**
   * 获取 updateTime 字段的值。
   *
   * @return updateTime 字段值
   */
  @JsonSerialize(using = DateTimeSerializer.class)
  public Date getUpdateTime() {
    return updateTime;
  }



  /**
   * 设置 updateTime 字段的值。
   *
   * @param updateTime updateTime 字段的值
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }



  /**
   * 获取 createTime 字段的值。
   *
   * @return createTime 字段值
   */
  @JsonSerialize(using = DateTimeSerializer.class)
  public Date getCreateTime() {
    return createTime;
  }



  /**
   * 设置 createTime 字段的值。
   *
   * @param createTime createTime 字段的值
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }



  /**
   * 获取 serialversionuid 字段的值。
   *
   * @return serialversionuid 字段值
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }



  public enum AppPlatform {
    /**
     * iOS。
     */
    iOS(0),
    /**
     * Android。
     */
    Android(1),

    /**
     * Web。
     */
    Web(2);

    /**
     * 将整型转换为平台枚举。
     *
     * @param value 整型值
     * @return app平台
     * @author hankai
     * @since Nov 8, 2016 8:43:21 AM
     */
    @JsonCreator
    public static AppPlatform fromInteger(Integer value) {
      if (value == iOS.value) {
        return iOS;
      } else if (value == Android.value) {
        return Android;
      } else if (value == Web.value) {
        return Web;
      }
      return null;
    }

    private final int value;

    private AppPlatform(int value) {
      this.value = value;
    }

    /**
     * 获取用于国际化的键名。
     */
    public String i18nKey(boolean withStyle) {
      if (withStyle) {
        return String.format("app.platform.%d.html", value);
      } else {
        return String.format("app.platform.%d", value);
      }
    }

    @JsonValue
    public int value() {
      return value;
    }
  }
}
