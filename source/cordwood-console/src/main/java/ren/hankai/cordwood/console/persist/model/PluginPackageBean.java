
package ren.hankai.cordwood.console.persist.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.jackson.DateTimeSerializer;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 插件包实体。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 11:34:32 AM
 */
@Entity
@Table(name = "plugin_packages")
@Cacheable(false)
public final class PluginPackageBean implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  private String id; // 插件包标识符，e.g. org.example:calculator:1.3.2
  @Column(length = 100, nullable = false, unique = true)
  private String fileName;
  @Column(length = 100, nullable = false)
  private String developer;
  @Column(length = 200, nullable = false)
  private String description;
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pluginPackage")
  private List<PluginBean> plugins = new ArrayList<>();

  /**
   * 获取 id 字段的值。
   *
   * @return id 字段值
   */
  public String getId() {
    return id;
  }

  /**
   * 设置 id 字段的值。
   *
   * @param id id 字段的值
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 获取 fileName 字段的值。
   *
   * @return fileName 字段值
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 设置 fileName 字段的值。
   *
   * @param fileName fileName 字段的值
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
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
   * 获取 plugins 字段的值。
   *
   * @return plugins 字段值
   */
  public List<PluginBean> getPlugins() {
    return plugins;
  }

  /**
   * 设置 plugins 字段的值。
   *
   * @param plugins plugins 字段的值
   */
  public void setPlugins(List<PluginBean> plugins) {
    this.plugins = plugins;
  }

  /**
   * 获取插件包安装路径。
   *
   * @return 安装路径
   * @author hankai
   * @since Nov 12, 2016 12:36:34 AM
   */
  public URL getInstallationUrl() {
    final String path = Preferences.getPluginsDir() + File.separator + fileName;
    try {
      return new File(path).toURI().toURL();
    } catch (final MalformedURLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 获取 serialversionuid 字段的值。
   *
   * @return serialversionuid 字段值
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
