
package ren.hankai.cordwood.core.test.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.cache.CacheWeight;
import ren.hankai.cordwood.core.config.CoreCacheConfig;

import java.io.IOException;

/**
 * 用于单元测试的缓存配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2018 11:27:07 AM
 */
@Configuration
@EnableCaching
@Profile({Preferences.PROFILE_TEST, Preferences.PROFILE_REDIS})
public class CacheConfig extends CoreCacheConfig {

  private static RedisServer redisServer;

  /**
   * 启动内嵌的Redis服务器。
   *
   * @throws IOException 异常
   */
  public static void startRedis() throws IOException {
    if (null == redisServer) {
      final String port = Preferences.getCustomConfig("redis.port");
      redisServer = new RedisServer(Integer.parseInt(port));
    }
    if (!redisServer.isActive()) {
      redisServer.start();
    }
  }

  /**
   * 停止Redis服务器。
   *
   */
  public static void stopRedis() {
    if ((null != redisServer) && redisServer.isActive()) {
      redisServer.stop();
    }
  }

  @Override
  protected int getCacheTimeToLiveSeconds(final CacheWeight weight) {
    if (weight == CacheWeight.Light) {
      return 10;
    }
    return super.getCacheTimeToLiveSeconds(weight);
  }

  @Override
  protected int getCacheTimeToIdleSeconds(final CacheWeight weight) {
    if (weight == CacheWeight.Light) {
      return 1;
    }
    return super.getCacheTimeToIdleSeconds(weight);
  }
}
