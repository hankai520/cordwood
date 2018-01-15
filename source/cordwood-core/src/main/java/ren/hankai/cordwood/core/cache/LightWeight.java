
package ren.hankai.cordwood.core.cache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 轻量级插件缓存配置，用于标记提供复杂服务的插件，此类插件方法被调用时，结果将被缓存。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 14, 2016 6:33:36 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Cacheable(cacheNames = "lightWeight", keyGenerator = "methodSignatureKeyGenerator")
public @interface LightWeight {
}
