
package ren.hankai.cordwood.core.domain;

import java.lang.reflect.Method;

/**
 * 用于封装插件功能
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 1:41:55 PM
 * @see ren.hankai.cordwood.plugin.api.Functional
 */
public final class PluginFunction {

  private String name;
  private Method method;
  private String resultType;

  /**
   * 获取功能名
   *
   * @return 功能名
   * @author hankai
   * @since Oct 13, 2016 10:13:59 AM
   */
  public String getName() {
    return name;
  }

  /**
   * 设置功能名
   *
   * @param name 功能名
   * @author hankai
   * @since Oct 13, 2016 10:14:15 AM
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取插件对应的方法
   *
   * @return 方法
   * @author hankai
   * @since Oct 13, 2016 10:14:32 AM
   */
  public Method getMethod() {
    return method;
  }

  /**
   * 设置插件对应的方法
   *
   * @return 方法
   * @author hankai
   * @since Oct 13, 2016 10:14:52 AM
   */
  public void setMethod(Method method) {
    this.method = method;
  }

  /**
   * 获取插件功能返回的数据类型
   *
   * @return 数据类型
   * @author hankai
   * @since Oct 13, 2016 10:15:03 AM
   */
  public String getResultType() {
    return resultType;
  }

  /**
   * 设置插件功能返回的数据类型
   *
   * @param resultType 数据类型
   * @author hankai
   * @since Oct 13, 2016 10:15:18 AM
   */
  public void setResultType(String resultType) {
    this.resultType = resultType;
  }
}
