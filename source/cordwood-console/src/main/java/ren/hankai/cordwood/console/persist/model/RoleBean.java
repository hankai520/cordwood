package ren.hankai.cordwood.console.persist.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 用户角色。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 2:39:44 PM
 */
@Entity
@Table(name = "roles")
@Cacheable(false)
public class RoleBean implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(length = 100, nullable = false, unique = true)
  private String name;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "roles_privileges",
      joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilegeId", referencedColumnName = "id"))
  private List<PrivilegeBean> privileges;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "sidebar_items_roles",
      joinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "sidebarItemId", referencedColumnName = "id"))
  private List<SidebarItemBean> sidebarItems;

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
   * 获取 privileges 字段的值。
   *
   * @return privileges 字段值
   */
  public List<PrivilegeBean> getPrivileges() {
    return privileges;
  }

  /**
   * 设置 privileges 字段的值。
   *
   * @param privileges privileges 字段的值
   */
  public void setPrivileges(List<PrivilegeBean> privileges) {
    this.privileges = privileges;
  }

  /**
   * 获取 sidebarItems 字段的值。
   *
   * @return sidebarItems 字段值
   */
  public List<SidebarItemBean> getSidebarItems() {
    return sidebarItems;
  }

  /**
   * 设置 sidebarItems 字段的值。
   *
   * @param sidebarItems sidebarItems 字段的值
   */
  public void setSidebarItems(List<SidebarItemBean> sidebarItems) {
    this.sidebarItems = sidebarItems;
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
