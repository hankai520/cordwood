
package ren.hankai.cordwood.console.persist.model;

import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.api.Pluggable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 插件实体。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:04:40 AM
 */
@Entity
@Table(name = "plugins")
@Cacheable(false)
public final class PluginBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 45)
  private String name;
  @Column(length = 120)
  private String displayName;
  @Column(length = 100)
  private String version;
  @Column(length = 800)
  private String description;
  @Column(length = 100)
  private String developer;
  private boolean isActive;
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "packageChecksum", referencedColumnName = "checksum", nullable = false)
  private PluginPackageBean pluginPackage;
  @Transient
  private List<PluginFunction> features;

  /**
   * 获取 pluginPackage 字段的值。
   *
   * @return pluginPackage 字段值
   */
  public PluginPackageBean getPluginPackage() {
    return pluginPackage;
  }

  /**
   * 设置 pluginPackage 字段的值。
   *
   * @param pluginPackage pluginPackage 字段的值
   */
  public void setPluginPackage(PluginPackageBean pluginPackage) {
    this.pluginPackage = pluginPackage;
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
   * 获取 displayName 字段的值。
   *
   * @return displayName 字段值
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * 设置 displayName 字段的值。
   *
   * @param displayName displayName 字段的值
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * 获取 version 字段的值。
   *
   * @return version 字段值
   */
  public String getVersion() {
    return version;
  }

  /**
   * 设置 version 字段的值。
   *
   * @param version version 字段的值
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * 获取 description 字段的值。
   *
   * @return description 字段值
   */
  public String getDescription() {
    return description;
  }

  /**
   * 设置 description 字段的值。
   *
   * @param description description 字段的值
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * 获取 developer 字段的值。
   *
   * @return developer 字段值
   */
  public String getDeveloper() {
    return developer;
  }

  /**
   * 设置 developer 字段的值。
   *
   * @param developer developer 字段的值
   */
  public void setDeveloper(String developer) {
    this.developer = developer;
  }

  /**
   * 获取 isActive 字段的值。
   *
   * @return isActive 字段值
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * 设置 isActive 字段的值。
   *
   * @param isActive isActive 字段的值
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * 获取 createTime 字段的值。
   *
   * @return createTime 字段值
   */
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
   * 获取 updateTime 字段的值。
   *
   * @return updateTime 字段值
   */
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
   * 获取 features 字段的值。
   *
   * @return features 字段值
   */
  public List<PluginFunction> getFeatures() {
    return features;
  }

  /**
   * 设置 features 字段的值。
   *
   * @param features features 字段的值
   */
  public void setFeatures(List<PluginFunction> features) {
    this.features = features;
  }

  /**
   * 获取插件功能访问地址。
   *
   * @param featureName 插件功能名
   * @return 插件功能访问地址
   * @author hankai
   * @since Nov 8, 2016 4:56:11 PM
   */
  public String getFeatureUrl(String featureName) {
    return String.format("%s/%s/%s", Pluggable.PLUGIN_BASE_URL, name, featureName);
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
