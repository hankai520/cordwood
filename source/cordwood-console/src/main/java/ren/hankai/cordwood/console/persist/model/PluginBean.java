
package ren.hankai.cordwood.console.persist.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "packageId", referencedColumnName = "id", nullable = false)
  private PluginPackageBean pluginPackage;
  @Column(length = 45, unique = true)
  private String name;
  @Column(length = 100)
  private String version;
  @Column(length = 800)
  private String description;
  private boolean isActive;
  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date createTime;
  @Temporal(TemporalType.DATE)
  private Date updateTime;

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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
