
package ren.hankai.cordwood.plugin.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将一个类标记为可热拔插的插件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 29, 2016 5:34:50 PM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pluggable {

  /**
   * 插件部署到控制台之后，访问的根路径（即访问插件服务的路径）。
   */
  public static final String PLUGIN_BASE_URL = "/services";
  /**
   * 插件部署到控制台之后，插件资源访问的根路径，例如：css，js文件。
   */
  public static final String PLUGIN_RESOURCE_BASE_URL = "/resources";

  /**
   * 插件的名称，此名称将被映射为 web service URL 的一部分，因此需要考虑其值是否 满足 HTTP URL 的规范。插件名称将用于唯一区分插件。
   *
   * @return 插件名称
   * @author hankai
   * @since Oct 13, 2016 9:11:48 AM
   */
  String name() default "";

  /**
   * 插件的版本。
   *
   * @return 插件版本。
   * @author hankai
   * @since Oct 13, 2016 9:13:46 AM
   */
  String version() default "";

  /**
   * 插件简介。
   *
   * @return 插件简介
   * @author hankai
   * @since Oct 13, 2016 9:14:15 AM
   */
  String description() default "";
}
