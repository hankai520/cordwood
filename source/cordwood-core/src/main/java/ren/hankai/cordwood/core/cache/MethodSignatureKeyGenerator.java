
package ren.hankai.cordwood.core.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 基于方法签名的缓存键生成器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Feb 17, 2017 3:53:02 PM
 */
public class MethodSignatureKeyGenerator implements KeyGenerator {

  @Override
  public Object generate(Object target, Method method, Object... params) {
    final StringBuilder builder = new StringBuilder();
    builder.append(target.getClass().toString());
    builder.append(method.getName());
    if (params != null) {
      for (final Object object : params) {
        if (object == null) {
          continue;
        }
        builder.append(object.getClass().toString() + object.hashCode());
      }
    }
    return builder.toString();
  }

}
