
package ren.hankai.cordwood.data.mybatis.test.domain;

import org.apache.ibatis.type.Alias;

/**
 * 用于测试的用户模型。
 *
 * @author hankai
 * @since 1.0.0
 */
@Alias("User")
public class User {

  private String name;

  private int age;

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
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * 获取 age 字段的值。
   *
   * @return age 字段值
   */
  public int getAge() {
    return age;
  }

  /**
   * 设置 age 字段的值。
   *
   * @param age age 字段的值
   */
  public void setAge(final int age) {
    this.age = age;
  }

}
