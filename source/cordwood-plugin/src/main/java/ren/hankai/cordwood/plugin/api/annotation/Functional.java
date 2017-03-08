
package ren.hankai.cordwood.plugin.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将一个方法标记为插件所提供的功能。使用此注解来标记插件类中的方法，可以将这些方法发布为 web service。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 1:44:55 PM
 * @see ren.hankai.cordwood.plugin.api.annotation.Pluggable
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Functional {

  /**
   * 插件功能简介的国际化键名。
   */
  public static final String FUNCTION_DESCRIPTION_I18N_KEY = "description";

  /**
   * 插件功能的名称，此名称将被映射为 web service URL 的一部分，因此为其赋值时，需要考虑 其是否兼容 HTTP URL
   * 规范。若不设置此属性，则会使用被其标记的方法的方法名作为名称，否则 就使用指定的名称。建议采用下划线分割英文单词的命名方法。
   *
   * @return 插件功能的名称
   * @author hankai
   * @since Oct 13, 2016 8:54:28 AM
   */
  String name() default "";

  /**
   * 插件功能返回的 HTTP 数据类型，即 content-type。
   *
   * @return 插件功能的返回的 HTTP 数据类型
   * @author hankai
   * @since Oct 13, 2016 8:55:54 AM
   */
  String resultType() default "text/plain";
}
