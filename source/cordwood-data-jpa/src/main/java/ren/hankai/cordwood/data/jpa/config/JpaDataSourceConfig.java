
package ren.hankai.cordwood.data.jpa.config;

import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.data.AbstractDataSourceConfig;

/**
 * 数据源配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 18, 2016 2:19:51 PM
 */
public abstract class JpaDataSourceConfig extends AbstractDataSourceConfig {

  /**
   * HSQL 数据源配置。
   *
   * @author hankai
   */
  @Profile(Preferences.PROFILE_HSQL)
  @Configuration
  public static class HsqlConfig {

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(HSQLPlatform.class.getName(), basePackages);
    }
  }

  /**
   * MySQL 数据源配置。
   *
   * @author hankai
   */
  @Configuration
  @Profile(Preferences.PROFILE_MYSQL)
  public static class MySqlConfig {

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(MySQLPlatform.class.getName(), basePackages);
    }
  }

  /**
   * Oracle 数据源配置。
   *
   * @author hankai
   */
  @Configuration
  @Profile(Preferences.PROFILE_ORACLE)
  public static class OracleConfig {

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(OraclePlatform.class.getName(), basePackages);
    }
  }
}
