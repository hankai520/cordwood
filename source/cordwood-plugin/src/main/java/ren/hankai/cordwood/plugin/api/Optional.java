
package ren.hankai.cordwood.plugin.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将插件功能的入参标记为可选参数（非必传）。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 9, 2016 9:59:22 AM
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Optional {
}
