
package ren.hankai.cordwood.plugin.api;

import ren.hankai.cordwood.plugin.PluginPackage;

import java.util.List;

/**
 * 插件加载器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:45:01 PM
 */
public interface PluginLoader {

  /**
   * 插件包所属分组ID。
   */
  public static final String GROUP_ID = "Group-Id";

  /**
   * 插件包 build ID。
   */
  public static final String ARTIFACT_ID = "Artifact-Id";

  /**
   * 插件包版本。
   */
  public static final String PACKAGE_VERSION = "Implementation-Version";

  /**
   * 开发者。
   */
  public static final String DEVELOPER = "Developer";

  /**
   * 插件包介绍。
   */
  public static final String DESCRIPTION = "Description";

  /**
   * 插件基包。例如：所有插件都在 com.example.demo 包中，则基包为 com.example.demo、或 com.example 或 com。多个基包用逗号分隔。
   * 插件加载器会扫描这些包中的所有类，并加载含有 Pluggable 注解的类。
   *
   * @see ren.hankai.cordwood.plugin.api.annotation.Pluggable
   */
  public static final String PLUGIN_BASE_PACKAGES = "Plugin-Packages";
  /**
   * 对于基于spring框架的插件包，在 jar 文件的 META-INF/MANIFEST.MF 文件中需要加入以下配置来告知加载器 应该使用哪个配置类作为插件的 spring
   * 配置类。加载器将会使用此类的配置来初始化插件包专用的 spring 上下文。
   */
  public static final String PLUGIN_SPRING_CONFIG_CLASS = "Spring-Config-Class";

  /**
   * 将插件包中的插件实例载入内存。
   *
   * @param pluginPackage 插件包信息
   * @return 插件实例，返回 null 表示加载失败
   * @author hankai
   * @since Sep 30, 2016 10:43:38 AM
   */
  List<Object> loadPlugins(PluginPackage pluginPackage);

  /**
   * 从内存中卸载指定插件实例。
   *
   * @param instance 插件实例
   * @return 是否载出成功
   * @author hankai
   * @since Sep 30, 2016 10:44:05 AM
   */
  boolean unloadPlugin(Object instance);
}
