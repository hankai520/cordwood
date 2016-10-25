
package ren.hankai.cordwood.core.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件模型，用于封装被标记为插件的类的插件配置信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:04:40 AM
 * @see ren.hankai.cordwood.plugin.api.Pluggable
 */
public final class Plugin {

  // 插件名称，用作唯一标识符
  private String name;
  private String version;
  private String description;
  private boolean isActive;
  private Object instance;
  private Map<String, PluginFunction> functions = new HashMap<>();

  /**
   * 获取插件名。
   *
   * @return 插件名
   * @author hankai
   * @since Oct 13, 2016 9:57:36 AM
   */
  public String getName() {
    return name;
  }

  /**
   * 设置插件名。
   *
   * @param name 插件名
   * @author hankai
   * @since Oct 13, 2016 9:57:48 AM
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取插件版本。
   *
   * @return 插件版本号
   * @author hankai
   * @since Oct 13, 2016 9:58:06 AM
   */
  public String getVersion() {
    return version;
  }

  /**
   * 设置插件版本。
   *
   * @param version 插件版本号
   * @author hankai
   * @since Oct 13, 2016 9:58:16 AM
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * 获取插件简介。
   *
   * @return 插件简介
   * @author hankai
   * @since Oct 13, 2016 9:58:28 AM
   */
  public String getDescription() {
    return description;
  }

  /**
   * 设置插件简介。
   *
   * @param description 插件简介
   * @author hankai
   * @since Oct 13, 2016 9:58:44 AM
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * 获取插件是否启用。
   *
   * @return 是否已启用
   * @author hankai
   * @since Oct 13, 2016 9:58:57 AM
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * 设置插件启用状态。
   *
   * @param isActive 是否启用
   * @author hankai
   * @since Oct 13, 2016 9:59:18 AM
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * 获取插件底层实例。
   *
   * @return 插件实例
   * @author hankai
   * @since Oct 13, 2016 9:59:30 AM
   */
  public Object getInstance() {
    return instance;
  }

  /**
   * 设置插件底层实例。
   *
   * @param instance 实例
   * @author hankai
   * @since Oct 13, 2016 9:59:46 AM
   */
  public void setInstance(Object instance) {
    this.instance = instance;
  }

  /**
   * 获取插件功能集合。
   *
   * @return 插件功能
   * @author hankai
   * @since Oct 13, 2016 9:59:59 AM
   */
  public Map<String, PluginFunction> getFunctions() {
    return functions;
  }

  /**
   * 设置插件功能集合。
   *
   * @param functions 插件功能集合
   * @author hankai
   * @since Oct 13, 2016 10:00:16 AM
   */
  public void setFunctions(Map<String, PluginFunction> functions) {
    this.functions = functions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Plugin: \n");
    sb.append(String.format("    Name: %s\n", name));
    sb.append(String.format("    Version: %s\n", version));
    sb.append(String.format("    Description: %s\n", description));
    sb.append(String.format("    Is active? %s\n", isActive));
    sb.append(String.format("    Instance: %s\n", instance.getClass().getName()));
    sb.append(String.format("    Functions: %s\n", functions.toString()));
    return sb.toString();
  }
}