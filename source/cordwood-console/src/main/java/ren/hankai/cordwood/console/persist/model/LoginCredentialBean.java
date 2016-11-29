
package ren.hankai.cordwood.console.persist.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户登录凭证（用于实现 remember-me 功能）
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2016 3:02:50 PM
 */
@Entity
@Table(name = "login_credentials")
@Cacheable(false)
public class LoginCredentialBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 64)
  private String series;

  @Column(length = 64)
  private String userName;

  @Column(length = 64)
  private String token;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUsed;

  /**
   * 获取 series 字段的值。
   *
   * @return series 字段值
   */
  public String getSeries() {
    return series;
  }

  /**
   * 设置 series 字段的值。
   *
   * @param series series 字段的值
   */
  public void setSeries(String series) {
    this.series = series;
  }

  /**
   * 获取 userName 字段的值。
   *
   * @return userName 字段值
   */
  public String getUserName() {
    return userName;
  }

  /**
   * 设置 userName 字段的值。
   *
   * @param userName userName 字段的值
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * 获取 token 字段的值。
   *
   * @return token 字段值
   */
  public String getToken() {
    return token;
  }

  /**
   * 设置 token 字段的值。
   *
   * @param token token 字段的值
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * 获取 lastUsed 字段的值。
   *
   * @return lastUsed 字段值
   */
  public Date getLastUsed() {
    return lastUsed;
  }

  /**
   * 设置 lastUsed 字段的值。
   *
   * @param lastUsed lastUsed 字段的值
   */
  public void setLastUsed(Date lastUsed) {
    this.lastUsed = lastUsed;
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
