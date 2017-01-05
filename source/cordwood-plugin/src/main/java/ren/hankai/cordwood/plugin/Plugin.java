
package ren.hankai.cordwood.plugin;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import ren.hankai.cordwood.plugin.api.annotation.Pluggable;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件模型，用于封装被标记为插件的类的插件配置信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:04:40 AM
 * @see ren.hankai.cordwood.plugin.api.annotation.Pluggable
 */
public final class Plugin {

  // 所属插件包ID，用于逆向超照插件所属插件包
  private String packageId;
  // 插件名称，用作唯一标识符
  private String name;
  private String displayName;
  private String version;
  private String description;
  private String developer;
  private boolean isActive;
  private Object instance;
  private Class<?> instanceClass;
  private Map<String, PluginFunction> functions = new HashMap<>();
  private MessageSource messageSource;

  private void escapeSpecialCharacters() {
    if (StringUtils.isNotEmpty(packageId)) {
      packageId = StringEscapeUtils.escapeHtml4(packageId);
    }
    if (StringUtils.isNotEmpty(name)) {
      name = StringEscapeUtils.escapeHtml4(name);
    }
    if (StringUtils.isNotEmpty(displayName)) {
      displayName = StringEscapeUtils.escapeHtml4(displayName);
    }
    if (StringUtils.isNotEmpty(version)) {
      version = StringEscapeUtils.escapeHtml4(version);
    }
    if (StringUtils.isNotEmpty(developer)) {
      developer = StringEscapeUtils.escapeHtml4(developer);
    }
    if (StringUtils.isNotEmpty(description)) {
      description = StringEscapeUtils.escapeHtml4(description);
    }
  }

  public Plugin() {}

  /**
   * 构造插件模型。
   *
   * @param name 插件唯一限定名
   * @param pluginInstance 插件实例
   */
  public Plugin(String name, Object pluginInstance) {
    final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setDefaultEncoding("UTF-8");
    instanceClass = AopUtils.getTargetClass(pluginInstance);
    String path = instanceClass.getPackage().getName();
    path = path.replaceAll("\\.", "/");
    final String baseName = String.format("classpath:%s/%s", path, name);
    ms.setBasename(baseName);
    ms.setUseCodeAsDefaultMessage(true);

    String code = String.format("%s.%s", name, Pluggable.PLUGIN_DISPLAY_NAME_I18N_KEY);
    displayName = ms.getMessage(code, null, null);

    code = String.format("%s.%s", name, Pluggable.PLUGIN_DEVELOPER_I18N_KEY);
    developer = ms.getMessage(code, null, null);

    code = String.format("%s.%s", name, Pluggable.PLUGIN_DESCRIPTION_I18N_KEY);
    description = ms.getMessage(code, null, null);

    this.name = name;
    instance = pluginInstance;
    messageSource = ms;
    escapeSpecialCharacters();
  }

  /**
   * 获取插件所属插件包ID。
   *
   * @return 插件包ID
   * @author hankai
   * @since Oct 29, 2016 7:35:53 PM
   */
  public String getPackageId() {
    return packageId;
  }

  /**
   * 设置插件所属插件包ID。
   *
   * @param packageId 插件包ID
   * @author hankai
   * @since Oct 29, 2016 7:36:08 PM
   */
  public void setPackageId(String packageId) {
    this.packageId = packageId;
  }

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
   * 获取 displayName 字段的值。
   *
   * @return displayName 字段值
   */
  public String getDisplayName() {
    return displayName;
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
   * 获取 developer 字段的值。
   *
   * @return developer 字段值
   */
  public String getDeveloper() {
    return developer;
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
   * 获取 instanceClass 字段的值。
   *
   * @return instanceClass 字段值
   */
  public Class<?> getInstanceClass() {
    return instanceClass;
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

  /**
   * 获取 messageSource 字段的值。
   *
   * @return messageSource 字段值
   */
  public MessageSource getMessageSource() {
    return messageSource;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Plugin: \n");
    sb.append(String.format("    Name: %s\n", name));
    sb.append(String.format("    Display Name: %s\n", displayName));
    sb.append(String.format("    Version: %s\n", version));
    sb.append(String.format("    Description: %s\n", description));
    sb.append(String.format("    Developer: %s\n", developer));
    sb.append(String.format("    Is active? %s\n", isActive));
    sb.append(String.format("    Instance: %s\n", AopUtils.getTargetClass(instance).getName()));
    sb.append(String.format("    Functions: %s\n", functions.toString()));
    return sb.toString();
  }
}
