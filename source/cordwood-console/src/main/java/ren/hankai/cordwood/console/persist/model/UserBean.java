package ren.hankai.cordwood.console.persist.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ren.hankai.cordwood.jackson.DateTimeSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 平台运维账号。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 11:27:47 AM
 */
@Entity
@Table(name = "users")
@Cacheable(false)
public class UserBean implements Serializable, UserDetails {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Transient
  private String avatarBase64Data; // 用于接收用户修改后的新头像
  @Column(length = 100, unique = true, nullable = false)
  @Pattern(
      regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
  private String email;
  @Column(length = 100, nullable = false)
  @Pattern(regexp = "[a-fA-F0-9]{40}")
  private String password;
  @Column(length = 20, unique = true)
  @Pattern(regexp = "\\d*")
  private String mobile;
  @Column(length = 45, nullable = false)
  @Size(min = 1, max = 20)
  private String name;
  @Column(length = 200)
  @Size(min = 1, max = 140)
  private String aboutMe;
  private UserStatus status;
  @Transient
  private String statusName;
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"))
  private List<RoleBean> roles;
  @Transient
  private List<Integer> selectedRoleIds;

  /**
   * 用已关联的角色来初始化角色ID数组。
   *
   * @author hankai
   * @since Dec 7, 2016 3:01:00 PM
   */
  public void initSelectedRoleIds() {
    final List<Integer> ids = new ArrayList<>();
    final List<RoleBean> list = getRoles();
    for (final RoleBean roleBean : list) {
      ids.add(roleBean.getId());
    }
    selectedRoleIds = ids;
  }

  /**
   * 用选择的角色ID数组来更新角色数组（用于修改用户角色）。
   *
   * @author hankai
   * @since Dec 7, 2016 3:01:34 PM
   */
  public void updateRolesWithSelectedIds() {
    final List<RoleBean> list = new ArrayList<>();
    if (getSelectedRoleIds() != null) {
      for (final Integer rid : getSelectedRoleIds()) {
        final RoleBean rb = new RoleBean();
        rb.setId(rid);
        list.add(rb);
      }
    }
    setRoles(list);
  }

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
   * 获取 avatarBase64Data 字段的值。
   *
   * @return avatarBase64Data 字段值
   */
  public String getAvatarBase64Data() {
    return avatarBase64Data;
  }

  /**
   * 设置 avatarBase64Data 字段的值。
   *
   * @param avatarBase64Data avatarBase64Data 字段的值
   */
  public void setAvatarBase64Data(String avatarBase64Data) {
    this.avatarBase64Data = avatarBase64Data;
  }

  /**
   * 获取 email 字段的值。
   *
   * @return email 字段值
   */
  public String getEmail() {
    return email;
  }

  /**
   * 设置 email 字段的值。
   *
   * @param email email 字段的值
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * 获取 mobile 字段的值。
   *
   * @return mobile 字段值
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * 设置 mobile 字段的值。
   *
   * @param mobile mobile 字段的值
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
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
   * 获取 aboutMe 字段的值。
   *
   * @return aboutMe 字段值
   */
  public String getAboutMe() {
    return aboutMe;
  }

  /**
   * 设置 aboutMe 字段的值。
   *
   * @param aboutMe aboutMe 字段的值
   */
  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  /**
   * 获取 password 字段的值。
   *
   * @return password 字段值
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * 设置 password 字段的值。
   *
   * @param password password 字段的值
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * 获取 status 字段的值。
   *
   * @return status 字段值
   */
  public UserStatus getStatus() {
    return status;
  }

  /**
   * 设置 status 字段的值。
   *
   * @param status status 字段的值
   */
  public void setStatus(UserStatus status) {
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
   * 获取 selectedRoleIds 字段的值。
   *
   * @return selectedRoleIds 字段值
   */
  public List<Integer> getSelectedRoleIds() {
    return selectedRoleIds;
  }

  /**
   * 设置 selectedRoleIds 字段的值。
   *
   * @param selectedRoleIds selectedRoleIds 字段的值
   */
  public void setSelectedRoleIds(List<Integer> selectedRoleIds) {
    this.selectedRoleIds = selectedRoleIds;
  }

  /**
   * 获取 serialversionuid 字段的值。
   *
   * @return serialversionuid 字段值
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    final List<RoleBean> roles = getRoles();
    if (roles != null) {
      for (final RoleBean roleBean : roles) {
        authorities.add(new SimpleGrantedAuthority(roleBean.getName()));
      }
    }
    return authorities;
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return UserStatus.Enabled.equals(getStatus());
  }



  /**
   * 用户账号状态。
   *
   * @author hankai
   * @version 1.0.0
   * @since Nov 2, 2016 11:31:15 AM
   */
  public enum UserStatus {
    /**
     * 禁用。
     */
    Disabled(0),
    /**
     * 启用。
     */
    Enabled(1),;

    /**
     * 将整型转换为用户状态枚举。
     *
     * @param value 整型值
     * @return 用户状态
     * @author hankai
     * @since Nov 8, 2016 8:43:21 AM
     */
    @JsonCreator
    public static UserStatus fromInteger(Integer value) {
      if (value == Disabled.value) {
        return Disabled;
      } else if (value == Enabled.value) {
        return Enabled;
      }
      return null;
    }

    private final int value;

    private UserStatus(int value) {
      this.value = value;
    }

    /**
     * 获取用于国际化的键名。
     */
    public String i18nKey(boolean withStyle) {
      if (withStyle) {
        return String.format("user.status.%d.html", value);
      } else {
        return String.format("user.status.%d", value);
      }
    }

    @JsonValue
    public int value() {
      return value;
    }
  }

}
