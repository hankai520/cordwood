
package ren.hankai.cordwood.core.config;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.cache.CacheWeight;
import ren.hankai.cordwood.core.cache.MethodSignatureKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心缓存配置，提供缓存基础配置。缓存基于 Ehcache 实现，默认提供三个级别的缓存，子类可继承此类自定义缓存配置，
 * 但不可自定义新的缓存级别，这是因为缓存级别已经与对应的注解绑定。
 * 示例：
 *
 * <pre>
 * &#64;EnableCaching
 * &#64;Profile(Preferences.PROFILE_REDIS) //激活redis，去掉此行则默认使用ehcache。
 * public class MyCacheConfig extends CoreCacheConfig {...}
 * </pre>
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2017 6:12:31 PM
 * @see ren.hankai.cordwood.core.cache.LightWeight
 * @see ren.hankai.cordwood.core.cache.MiddleWeight
 * @see ren.hankai.cordwood.core.cache.HeavyWeight
 */
public abstract class CoreCacheConfig extends CachingConfigurerSupport {

  @Autowired
  private Environment env;

  /**
   * 获取Ehcache缓存管理器。
   *
   * @return 缓存管理器
   */
  private CacheManager getEhcacheManager() {
    final net.sf.ehcache.config.Configuration config = getEhcacheConfig();
    final PersistenceConfiguration pc = new PersistenceConfiguration();
    pc.setStrategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP.name());
    for (final CacheWeight cw : CacheWeight.values()) {
      final CacheConfiguration cc = new CacheConfiguration();
      cc.setMaxEntriesLocalHeap(getMaxNumberOfCacheEntries()); // 尽可能利用内存
      cc.setTimeToIdleSeconds(getCacheTimeToIdleSeconds(cw));
      cc.setTimeToLiveSeconds(getCacheTimeToLiveSeconds(cw));
      // cc.setMemoryStoreEvictionPolicy("LRU"); // One of "LRU", "LFU" or "FIFO".
      cc.persistence(pc);
      cc.setName(cw.value());
      config.addCache(cc);
    }
    final net.sf.ehcache.CacheManager cm = net.sf.ehcache.CacheManager.newInstance(config);
    return new EhCacheCacheManager(cm);
  }

  /**
   * 获取Redis缓存管理器。
   *
   * @return 缓存管理器
   */
  private CacheManager getRedisCacheManager() {
    final RedissonClient redissonClient = getRedisClient();
    final Map<String, CacheConfig> caches = new HashMap<>();
    CacheConfig cacheConfig = null;
    for (final CacheWeight cw : CacheWeight.values()) {
      cacheConfig = new CacheConfig();
      cacheConfig.setMaxSize(getMaxNumberOfCacheEntries()); // 尽可能利用内存
      cacheConfig.setMaxIdleTime(getCacheTimeToIdleSeconds(cw) * 1000);
      cacheConfig.setTTL(getCacheTimeToLiveSeconds(cw) * 1000);
      caches.put(cw.value(), cacheConfig);
    }
    return new RedissonSpringCacheManager(redissonClient, caches);
  }

  /**
   * 配置缓存最大生命期。
   *
   * @param weight 缓存量级
   * @return 缓存最大生命期
   */
  protected int getCacheTimeToLiveSeconds(final CacheWeight weight) {
    if (CacheWeight.Heavy == weight) {
      return 60 * 60 * 4;
    } else if (CacheWeight.Middle == weight) {
      return 60 * 60;
    } else if (CacheWeight.Light == weight) {
      return 60 * 15;
    }
    throw new RuntimeException("Cache weight not supported.");
  }

  /**
   * 配置缓存最大空闲期。
   *
   * @param weight 缓存量级
   * @return 最大空闲期
   */
  protected int getCacheTimeToIdleSeconds(final CacheWeight weight) {
    if (CacheWeight.Heavy == weight) {
      return 60 * 60 * 4;
    } else if (CacheWeight.Middle == weight) {
      return 60 * 60;
    } else if (CacheWeight.Light == weight) {
      return 60 * 15;
    }
    throw new RuntimeException("Cache weight not supported.");
  }

  /**
   * 缓存最大条目数。
   *
   * @return 最大条目数
   */
  protected int getMaxNumberOfCacheEntries() {
    // 按每个缓存条目1KB来算，默认32MB用于缓存。缓存量级数为3，因此为 32*3=96MB
    return 32 * 1024;
  }

  /**
   * 配置Redis客户端。默认是单实例，若需配置集群、主备等模式，可覆盖此方法。
   *
   * @return redis客户端实例
   */
  protected RedissonClient getRedisClient() {
    final Config config = new Config();
    final String host = Preferences.getCustomConfig("redis.host");
    final String port = Preferences.getCustomConfig("redis.port");
    if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port)) {
      throw new RuntimeException("redis.host or redis.port not configured.");
    }
    config.useSingleServer().setAddress(String.format("redis://%s:%s", host, port));
    final RedissonClient redissonClient = Redisson.create(config);
    return redissonClient;
  }

  /**
   * 配置Ehcache。默认通过硬盘存储缓存数据（数据根目录/cache）。
   *
   * @return Ehcache配置
   */
  protected net.sf.ehcache.config.Configuration getEhcacheConfig() {
    final net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
    final DiskStoreConfiguration dsc = new DiskStoreConfiguration();
    dsc.setPath(Preferences.getCacheDir());
    config.diskStore(dsc);
    return config;
  }

  /**
   * 基于方法签名的缓存键生成器。
   *
   * @return 键生成器
   */
  @Bean(name = "methodSignatureKeyGenerator") // 必须显式的定义为此名称，因为缓存注解使用此名称
  @Override
  public KeyGenerator keyGenerator() {
    return new MethodSignatureKeyGenerator();
  }

  @Bean
  @Override
  public CacheManager cacheManager() {
    if (env.acceptsProfiles(Preferences.PROFILE_REDIS)) {
      return getRedisCacheManager();
    } else {
      // 默认用ehcache
      return getEhcacheManager();
    }
  }

}
