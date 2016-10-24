
package ren.hankai.cordwood.persist.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author hankai
 * @version TODO Missing version number
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
  @JoinColumn(name = "packageId", referencedColumnName = "id")
  private PluginPackageBean pluginPackage;
  private String name;
  private String version;
  private String description;
  private boolean isActive;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PluginPackageBean getPluginPackage() {
    return pluginPackage;
  }

  public void setPluginPackage(PluginPackageBean pluginPackage) {
    this.pluginPackage = pluginPackage;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
