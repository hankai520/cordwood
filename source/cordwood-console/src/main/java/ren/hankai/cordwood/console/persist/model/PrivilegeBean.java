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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 用户账号权限。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 2:36:39 PM
 */
@Entity
@Table(name = "privileges")
@Cacheable(false)
public class PrivilegeBean implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(length = 100, nullable = false, unique = true)
  private String name;
  @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
  private List<RoleBean> roles;

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
   * 获取 roles 字段的值。
   *
   * @return roles 字段值
   */
  public List<RoleBean> getRoles() {
    return roles;
  }

  /**
   * 设置 roles 字段的值。
   *
   * @param roles roles 字段的值
   */
  public void setRoles(List<RoleBean> roles) {
    this.roles = roles;
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
