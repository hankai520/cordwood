
package ren.hankai.cordwood.core.config;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.cache.MethodSignatureKeyGenerator;

/**
 * 核心缓存配置，提供缓存基础配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2017 6:12:31 PM
 */
public class CoreCacheConfig extends CachingConfigurerSupport {

  /**
   * 基于方法签名的缓存键生成器。
   *
   * @return 键生成器
   * @author hankai
   * @since Nov 29, 2017 5:25:39 PM
   */
  // 必须显式的定义为此名称，因为缓存注解使用此名称
  @Bean(name = "methodSignatureKeyGenerator")
  @Override
  public KeyGenerator keyGenerator() {
    return new MethodSignatureKeyGenerator();
  }

  @Bean
  @Override
  public CacheManager cacheManager() {
    final net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
    final DiskStoreConfiguration dsc = new DiskStoreConfiguration();
    dsc.setPath(Preferences.getCacheDir());
    config.diskStore(dsc);

    final PersistenceConfiguration pc = new PersistenceConfiguration();
    pc.setStrategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP.name());

    /*
     * no expiry this means cache mappings will never expire,
     *
     * time-to-live this means cache mappings will expire after a fixed duration following their
     * creation,
     *
     * time-to-idle this means cache mappings will expire after a fixed duration following the time
     * they were last accessed.
     */

    // 定义重量级缓存
    final CacheConfiguration heavyWeightCache = new CacheConfiguration();
    heavyWeightCache.setName("heavyWeight");
    heavyWeightCache.setMaxEntriesLocalHeap(4000);
    heavyWeightCache.setTimeToIdleSeconds(60 * 60 * 24); // 缓存1天
    // heavyWeightCache.setMemoryStoreEvictionPolicy("LRU"); // One of "LRU", "LFU" or "FIFO".
    heavyWeightCache.persistence(pc);
    config.addCache(heavyWeightCache);

    // 定义中量级缓存
    final CacheConfiguration middleWeightCache = new CacheConfiguration();
    middleWeightCache.setName("middleWeight");
    middleWeightCache.setMaxEntriesLocalHeap(2000);
    middleWeightCache.setTimeToIdleSeconds(60 * 60 * 12); // 缓存12小时
    middleWeightCache.persistence(pc);
    config.addCache(middleWeightCache);

    // 定义轻量级缓存
    final CacheConfiguration lightWeightCache = new CacheConfiguration();
    lightWeightCache.setName("lightWeight");
    lightWeightCache.setMaxEntriesLocalHeap(1000);
    lightWeightCache.setTimeToIdleSeconds(60 * 60 * 4); // 缓存4小时
    lightWeightCache.persistence(pc);
    config.addCache(lightWeightCache);

    final net.sf.ehcache.CacheManager cm = net.sf.ehcache.CacheManager.newInstance(config);
    return new EhCacheCacheManager(cm);
  }

}
