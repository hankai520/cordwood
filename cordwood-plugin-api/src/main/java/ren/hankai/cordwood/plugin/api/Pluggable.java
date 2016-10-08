
package ren.hankai.cordwood.plugin.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 29, 2016 5:34:50 PM
 */
@Target( { ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Pluggable {

    String name() default "";

    String version() default "";

    String description() default "";

    String readme() default "";
}
