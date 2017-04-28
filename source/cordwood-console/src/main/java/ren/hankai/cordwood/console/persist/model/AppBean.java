
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
  @NotNull
  @Size(min = 1, max = 20)
  private String name;
  @Column(length = 200, nullable = false)
  @NotNull
  @Size(min = 1, max = 180)
  private String introduction;
  private AppPlatform platform;
  @Transient
  private String platformName;
  private AppStatus status;
  @Transient
  private String statusName;
  @Column(length = 120, nullable = false, unique = true)
  @Pattern(regexp = "[a-zA-Z0-9]{20,120}")
  private String appKey; // app 唯一标识
  @Column(length = 120, nullable = false)
  @Pattern(regexp = "[a-zA-Z0-9]{20,120}")
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
   * 获取 platformName 字段的值。
   *
   * @return platformName 字段值
   */
  public String getPlatformName() {
    return platformName;
  }

  /**
   * 设置 platformName 字段的值。
   *
   * @param platformName platformName 字段的值
   */
  public void setPlatformName(String platformName) {
    this.platformName = platformName;
  }

  /**
   * 获取 status 字段的值。
   *
   * @return status 字段值
   */
  public AppStatus getStatus() {
    return status;
  }

  /**
   * 设置 status 字段的值。
   *
   * @param status status 字段的值
   */
  public void setStatus(AppStatus status) {
    this.status = status;
  }

  /**
   * 获取 statusName 字段的值。
   *
   * @return statusName 字段值
   */
  public String getStatusName() {
    return statusName;
  }

  /**
   * 设置 statusName 字段的值。
   *
   * @param statusName statusName 字段的值
   */
  public void setStatusName(String statusName) {
    this.statusName = statusName;
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
     *
     * @param withStyle 是否返回带HTML样式的值
     * @return 国际化后的值
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

  /**
   * 应用状态。
   *
   * @author hankai
   * @version 1.0.0
   * @since Dec 23, 2016 11:35:43 AM
   */
  public enum AppStatus {
    /**
     * 已上架。
     */
    Enabled(0),
    /**
     * 已下架。
     */
    Disabled(1),

    /**
     * 维护中。
     */
    Maintaining(2);

    /**
     * 将整型转换为应用状态枚举。
     *
     * @param value 整型值
     * @return 应用状态
     * @author hankai
     * @since Nov 8, 2016 8:43:21 AM
     */
    @JsonCreator
    public static AppStatus fromInteger(Integer value) {
      if (value == Enabled.value) {
        return Enabled;
      } else if (value == Disabled.value) {
        return Disabled;
      } else if (value == Maintaining.value) {
        return Maintaining;
      }
      return null;
    }

    private final int value;

    private AppStatus(int value) {
      this.value = value;
    }

    /**
     * 获取用于国际化的键名。
     *
     * @param withStyle 是否返回带HTML样式的值
     * @return 国际化后的值
     */
    public String i18nKey(boolean withStyle) {
      if (withStyle) {
        return String.format("app.status.%d.html", value);
      } else {
        return String.format("app.status.%d", value);
      }
    }

    @JsonValue
    public int value() {
      return value;
    }
  }
}
