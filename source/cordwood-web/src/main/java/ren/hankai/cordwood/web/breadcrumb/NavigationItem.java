
package ren.hankai.cordwood.web.breadcrumb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 面包屑导航注解，用于标记控制器中的映射为一个导航项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 6, 2016 2:51:57 PM
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NavigationItem {

  /**
   * 标签名（支持i18n）。
   *
   * @return 标签名
   * @author hankai
   * @since Dec 6, 2016 2:52:25 PM
   */
  public String label();

  /**
   * 所属分组。
   *
   * @return 所属分组
   * @author hankai
   * @since Dec 6, 2016 2:52:56 PM
   */
  public String family() default "shared";

  /**
   * 前一个导航项的标签名。
   *
   * @return 前一个导航项的标签名
   * @author hankai
   * @since Dec 6, 2016 2:53:13 PM
   */
  public String parent() default "";
}
