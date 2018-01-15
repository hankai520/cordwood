
package ren.hankai.cordwood.core.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import ren.hankai.cordwood.core.cache.MethodSignatureKeyGenerator;

/**
 * 核心缓存配置，提供缓存基础配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2017 6:12:31 PM
 */
public class CoreCacheConfig {

  /**
   * 基于方法签名的缓存键生成器。
   *
   * @return 键生成器
   * @author hankai
   * @since Nov 29, 2017 5:25:39 PM
   */
  @Bean(name = "methodSignatureKeyGenerator")
  public KeyGenerator keyGenerator() {
    return new MethodSignatureKeyGenerator();
  }

}
