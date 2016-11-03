package ren.hankai.cordwood.console.view.frontend;

import java.io.Serializable;

/**
 * 前台登录表单。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 3, 2016 11:50:08 AM
 */
public class LoginForm implements Serializable {

  private static final long serialVersionUID = 1L;

  private String loginId;
  private String password;
  private boolean remember;

  /**
   * 获取 loginId 字段的值。
   *
   * @return loginId 字段值
   */
  public String getLoginId() {
    return loginId;
  }

  /**
   * 设置 loginId 字段的值。
   *
   * @param loginId loginId 字段的值
   */
  public void setLoginId(String loginId) {
    this.loginId = loginId;
  }

  /**
   * 获取 password 字段的值。
   *
   * @return password 字段值
   */
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
   * 获取 remember 字段的值。
   *
   * @return remember 字段值
   */
  public boolean isRemember() {
    return remember;
  }

  /**
   * 设置 remember 字段的值。
   *
   * @param remember remember 字段的值
   */
  public void setRemember(boolean remember) {
    this.remember = remember;
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
