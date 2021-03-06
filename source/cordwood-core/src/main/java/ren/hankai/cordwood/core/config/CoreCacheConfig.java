/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

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
 * 核心缓存配置，提供缓存基础配置。缓存基于 Ehcache 实现，默认提供三个级别的缓存，子类可继承此类自定义缓存配置，
 * 但不可自定义新的缓存级别，这是因为缓存级别已经与对应的注解绑定。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2017 6:12:31 PM
 * @see ren.hankai.cordwood.core.cache.LightWeight
 * @see ren.hankai.cordwood.core.cache.MiddleWeight
 * @see ren.hankai.cordwood.core.cache.HeavyWeight
 */
public abstract class CoreCacheConfig extends CachingConfigurerSupport {

  /**
   * 获取重量级缓存配置，子类可重写此方法自定义缓存配置。
   *
   * @return 缓存配置
   * @author hankai
   * @since Nov 30, 2018 1:19:05 PM
   */
  protected CacheConfiguration getHeavyWeightCacheConfig() {
    final PersistenceConfiguration pc = new PersistenceConfiguration();
    pc.setStrategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP.name());

    final CacheConfiguration heavyWeightCache = new CacheConfiguration();
    heavyWeightCache.setMaxEntriesLocalHeap(4000);
    heavyWeightCache.setTimeToIdleSeconds(60 * 60 * 24); // 缓存1天
    // heavyWeightCache.setMemoryStoreEvictionPolicy("LRU"); // One of "LRU", "LFU" or "FIFO".
    heavyWeightCache.persistence(pc);
    return heavyWeightCache;
  }

  /**
   * 获取中量级缓存配置，子类可重写此方法自定义缓存配置。
   *
   * @return 缓存配置
   * @author hankai
   * @since Nov 30, 2018 1:19:21 PM
   */
  protected CacheConfiguration getMiddleWeightCacheConfig() {
    final PersistenceConfiguration pc = new PersistenceConfiguration();
    pc.setStrategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP.name());

    final CacheConfiguration middleWeightCache = new CacheConfiguration();
    middleWeightCache.setMaxEntriesLocalHeap(2000);
    middleWeightCache.setTimeToIdleSeconds(60 * 60 * 12); // 缓存12小时
    middleWeightCache.persistence(pc);
    return middleWeightCache;
  }

  /**
   * 获取轻量级缓存配置，子类可重写此方法自定义缓存配置。
   *
   * @return 缓存配置
   * @author hankai
   * @since Nov 30, 2018 1:19:35 PM
   */
  protected CacheConfiguration getLightWeightCacheConfig() {
    final PersistenceConfiguration pc = new PersistenceConfiguration();
    pc.setStrategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP.name());

    final CacheConfiguration lightWeightCache = new CacheConfiguration();
    lightWeightCache.setMaxEntriesLocalHeap(1000);
    lightWeightCache.setTimeToIdleSeconds(60 * 60 * 4); // 缓存4小时
    lightWeightCache.persistence(pc);
    return lightWeightCache;
  }

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

    // 定义重量级缓存
    final CacheConfiguration heavyWeightCache = getHeavyWeightCacheConfig();
    heavyWeightCache.setName("heavyWeight");
    config.addCache(heavyWeightCache);

    // 定义中量级缓存
    final CacheConfiguration middleWeightCache = getMiddleWeightCacheConfig();
    middleWeightCache.setName("middleWeight");
    config.addCache(middleWeightCache);

    // 定义轻量级缓存
    final CacheConfiguration lightWeightCache = getLightWeightCacheConfig();
    lightWeightCache.setName("lightWeight");
    config.addCache(lightWeightCache);

    final net.sf.ehcache.CacheManager cm = net.sf.ehcache.CacheManager.newInstance(config);
    return new EhCacheCacheManager(cm);
  }

}
