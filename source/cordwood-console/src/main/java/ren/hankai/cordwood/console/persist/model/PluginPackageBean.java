
package ren.hankai.cordwood.console.persist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
  @Column(length = 100, nullable = false, unique = true)
  private String fileName;
  @Column(length = 100)
  private String checksum;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pluginPackage")
  private List<PluginBean> plugins = new ArrayList<>();

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public List<PluginBean> getPlugins() {
    return plugins;
  }

  public void setPlugins(List<PluginBean> plugins) {
    this.plugins = plugins;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
