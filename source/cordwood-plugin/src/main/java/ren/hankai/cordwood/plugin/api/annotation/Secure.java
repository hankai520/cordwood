package ren.hankai.cordwood.plugin.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将插件的一个功能标记为需要安全保护。插件容器将会对分发给此插件功能的请求进行检查。 如果直接标记在插件类上，则表示所有功能都要进行检查，方法签名上的标记可以覆盖类标记。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 27, 2016 5:40:30 PM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {
  /**
   * 是否验证参数完整性，启用则会对入参进行验签。
   *
   * @return 是否验证参数完整性
   * @author hankai
   * @since Oct 27, 2016 5:44:16 PM
   */
  boolean checkParameterIntegrity() default true;

  /**
   * 是否检查访问令牌。
   *
   * @return 是否检查访问令牌
   * @author hankai
   * @since Oct 27, 2016 5:45:00 PM
   */
  boolean checkAccessToken() default true;
}
