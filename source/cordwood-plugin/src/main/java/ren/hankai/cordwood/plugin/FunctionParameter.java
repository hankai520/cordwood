
package ren.hankai.cordwood.plugin;

import ren.hankai.cordwood.plugin.api.annotation.Optional;

import java.lang.reflect.Parameter;

/**
 * 插件功能入参。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 9, 2016 10:10:05 AM
 */
public class FunctionParameter {
  private boolean required;
  private String description;
  private Parameter actualParameter;

  public FunctionParameter() {}

  /**
   * 构造插件功能参数。
   * 
   * @param plugin 插件
   * @param function 插件功能
   * @param actualParameter 功能对应的方法参数
   */
  public FunctionParameter(Plugin plugin, PluginFunction function, Parameter actualParameter) {
    required = (actualParameter.getAnnotation(Optional.class) == null);
    final String code =
        String.format("%s.%s.%s", plugin.getName(), function.getName(), actualParameter.getName());
    description = plugin.getMessageSource().getMessage(code, null, null);
    this.actualParameter = actualParameter;
  }

  /**
   * 获取 name 字段的值。
   *
   * @return name 字段值
   */
  public String getName() {
    return actualParameter.getName();
  }

  /**
   * 获取 required 字段的值。
   *
   * @return required 字段值
   */
  public boolean isRequired() {
    return required;
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
   * 获取 type 字段的值。
   *
   * @return type 字段值
   */
  public Class<?> getType() {
    return actualParameter.getType();
  }

  /**
   * 获取 actualParameter 字段的值。
   *
   * @return actualParameter 字段值
   */
  public Parameter getActualParameter() {
    return actualParameter;
  }

}
