
package ren.hankai.cordwood.plugin.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 1:44:55 PM
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Functional {

    String name() default "";

    String resultType() default "text/plain";
}
