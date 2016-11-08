
package ren.hankai.cordwood.plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 用于封装插件功能。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 1:41:55 PM
 * @see ren.hankai.cordwood.plugin.api.Functional
 */
public final class PluginFunction {

  private String name;
  private String description;
  private Method method;
  private String resultType;
  private boolean checkInboundParameters = false;
  private boolean checkAccessToken = false;
  private Parameter[] parameters;

  /**
   * 获取功能名。
   *
   * @return 功能名
   * @author hankai
   * @since Oct 13, 2016 10:13:59 AM
   */
  public String getName() {
    return name;
  }

  /**
   * 设置功能名。
   *
   * @param name 功能名
   * @author hankai
   * @since Oct 13, 2016 10:14:15 AM
   */
  public void setName(String name) {
    this.name = name;
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
   * 获取插件对应的方法。
   *
   * @return 方法
   * @author hankai
   * @since Oct 13, 2016 10:14:32 AM
   */
  public Method getMethod() {
    return method;
  }

  /**
   * 设置插件对应的方法。
   *
   * @author hankai
   * @since Oct 13, 2016 10:14:52 AM
   */
  public void setMethod(Method method) {
    this.method = method;
  }

  /**
   * 获取插件功能返回的数据类型。
   *
   * @return 数据类型
   * @author hankai
   * @since Oct 13, 2016 10:15:03 AM
   */
  public String getResultType() {
    return resultType;
  }

  /**
   * 设置插件功能返回的数据类型。
   *
   * @param resultType 数据类型
   * @author hankai
   * @since Oct 13, 2016 10:15:18 AM
   */
  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  /**
   * 是否检查入参完整性。
   *
   * @return 是否检查入参
   * @author hankai
   * @since Oct 27, 2016 6:20:25 PM
   */
  public boolean isCheckInboundParameters() {
    return checkInboundParameters;
  }

  /**
   * 设置是否检查入参。
   *
   * @param checkInboundParameters 是否检查
   * @author hankai
   * @since Oct 27, 2016 6:20:40 PM
   */
  public void setCheckInboundParameters(boolean checkInboundParameters) {
    this.checkInboundParameters = checkInboundParameters;
  }

  /**
   * 是否检查令牌。
   *
   * @return 是否检查令牌
   * @author hankai
   * @since Oct 27, 2016 6:20:57 PM
   */
  public boolean isCheckAccessToken() {
    return checkAccessToken;
  }

  /**
   * 设置是否检查令牌。
   *
   * @param checkAccessToken 是否检查令牌
   * @author hankai
   * @since Oct 27, 2016 6:21:10 PM
   */
  public void setCheckAccessToken(boolean checkAccessToken) {
    this.checkAccessToken = checkAccessToken;
  }

  /**
   * 插件功能需要的参数。
   *
   * @return 参数数组
   * @author hankai
   * @since Oct 28, 2016 9:30:49 AM
   */
  public Parameter[] getParameters() {
    return parameters;
  }

  /**
   * 设置插件功能需要的参数。
   *
   * @param parameters 参数数组
   * @author hankai
   * @since Oct 28, 2016 9:30:51 AM
   */
  public void setParameters(Parameter[] parameters) {
    this.parameters = parameters;
  }

}
