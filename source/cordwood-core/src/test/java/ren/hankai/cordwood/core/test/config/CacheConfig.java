
package ren.hankai.cordwood.core.test.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreCacheConfig;

/**
 * 用于单元测试的缓存配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2018 11:27:07 AM
 */
@Configuration
@EnableCaching
@Profile(Preferences.PROFILE_TEST)
public class CacheConfig extends CoreCacheConfig {

  @Override
  protected CacheConfiguration getLightWeightCacheConfig() {
    final CacheConfiguration config = super.getLightWeightCacheConfig();
    config.setTimeToIdleSeconds(1);
    return config;
  }
}
