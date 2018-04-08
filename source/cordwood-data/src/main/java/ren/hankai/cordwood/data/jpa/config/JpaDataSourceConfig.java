package ren.hankai.cordwood.data.jpa.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;
import org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;
import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 数据源配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 18, 2016 2:19:51 PM
 */
public abstract class JpaDataSourceConfig {

  private static final Logger logger = LoggerFactory.getLogger(JpaDataSourceConfig.class);

  protected static String[] basePackages = null;

  /**
   * 从外部配置文件加载数据库连接配置。如果数据库配置子类需要在程序启动时，从程序包外部获取配置文件， 则调用此方法获取外部文件配置参数。
   *
   * @return 是否加载成功
   * @author hankai
   * @since Jun 21, 2016 10:45:48 AM
   */
  private static Properties loadExternalConfig(String fileName) {
    Properties props = null;
    try {
      props = new Properties();
      props.load(new FileInputStream(Preferences.getDbConfigFile(fileName)));
    } catch (final IOException ex) {
      logger.warn("Failed to load external database configuration file for production profile.",
          ex);
    }
    if ((props != null) && (props.size() > 0)) {
      return props;
    }
    return null;
  }

  public JpaDataSourceConfig() {
    String pkg = this.getClass().getPackage().getName();
    if (StringUtils.isNotEmpty(pkg) && (basePackages == null)) {
      pkg = pkg.split("\\.")[0];
      basePackages = new String[] {pkg};
    }
  }

  @Profile(Preferences.PROFILE_HSQL)
  @Configuration
  public static class HsqlConfig {

    /**
     * 数据源。
     *
     * @return 数据源
     * @author hankai
     * @since Oct 25, 2016 11:35:04 AM
     */
    @Bean
    public DataSource dataSource() {
      final Properties props = loadExternalConfig("hsql.properties");
      final DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName(props.getProperty("driverClassName"));
      final String dbPath = Preferences.getDbDir() + File.separator + "application";
      ds.setUrl(String.format(props.getProperty("url"), dbPath));
      ds.setUsername(props.getProperty("username"));
      ds.setPassword(props.getProperty("password"));
      return ds;
    }

    /**
     * 数据源配置信息。
     *
     * @return 数据源配置信息
     * @author hankai
     * @since Oct 25, 2016 11:35:17 AM
     */
    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(HSQLPlatform.class.getName(), basePackages);
    }
  }

  @Configuration
  @Profile(Preferences.PROFILE_MYSQL)
  public static class MySqlConfig {

    /**
     * MySQL 数据源。
     *
     * @return 数据源
     * @author hankai
     * @since Oct 25, 2016 11:35:34 AM
     */
    @Bean
    public DataSource dataSource() {
      final Properties props = loadExternalConfig("mysql.properties");
      final PoolProperties pp = new PoolProperties();
      pp.setUrl(props.getProperty("url"));
      pp.setDriverClassName(props.getProperty("driverClassName"));
      pp.setUsername(props.getProperty("username"));
      pp.setPassword(props.getProperty("password"));
      pp.setJmxEnabled(false);

      pp.setTestWhileIdle(false); // 在连接空闲时，是否检查连接可用性，关闭以提高性能
      pp.setTimeBetweenEvictionRunsMillis(10 * 1000); // 每次做空闲检查、丢弃清理和池大小伸缩作业之间的休息时间

      pp.setValidationQuery("select 1"); // 用于检查连接是否可用的sql语句
      pp.setTestOnBorrow(true); // 是否检查在从池中获取的链接的可用性，打开，避免无效连接导致语句执行失败
      pp.setTestOnReturn(false); // 归还连接到池中时，是否检查连接可用性，关闭以提高性能，因为获取连接时已检查

      pp.setValidationInterval(30 * 1000); // 最快多长时间检查一次连接可用性，用于限制频繁检查带来的性能下降

      pp.setMaxActive(50); // 最大激活连接数
      pp.setInitialSize(4); // 池初始连接数
      pp.setMaxWait(20 * 1000); // 从池中获取连接时，最长等待多长时间

      pp.setRemoveAbandonedTimeout(60); // 多长时间内，不考虑丢弃连接
      pp.setMinEvictableIdleTimeMillis(30 * 1000); // 连接在池中必须存在多长时间后，才能被标记为可回收的
      pp.setMinIdle(4); // 池中至少保持多少个空闲连接
      pp.setLogAbandoned(false); // 当连接被丢弃时，是否打印日志，关闭以提高性能
      pp.setRemoveAbandoned(true); // 是否清除被丢弃的连接（关闭连接，从池中移除）
      pp.setJdbcInterceptors(
          ConnectionState.class.getName() + ";" + StatementFinalizer.class.getName());
      final org.apache.tomcat.jdbc.pool.DataSource ds =
          new org.apache.tomcat.jdbc.pool.DataSource();
      ds.setPoolProperties(pp);
      return ds;
    }

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(MySQLPlatform.class.getName(), basePackages);
    }
  }

  @Configuration
  @Profile(Preferences.PROFILE_ORACLE)
  public static class OracleConfig {

    /**
     * ORACLE 数据源。
     *
     * @return 数据源
     * @author hankai
     * @since Oct 25, 2016 11:35:50 AM
     */
    @Bean
    public DataSource dataSource() {
      final Properties props = loadExternalConfig("oracle.properties");
      final PoolProperties pp = new PoolProperties();
      pp.setUrl(props.getProperty("url"));
      pp.setDriverClassName(props.getProperty("driverClassName"));
      pp.setUsername(props.getProperty("username"));
      pp.setPassword(props.getProperty("password"));
      pp.setJmxEnabled(true);
      pp.setTestWhileIdle(false);
      pp.setTestOnBorrow(true);
      pp.setValidationQuery("select * from dual");
      pp.setTestOnReturn(false);
      pp.setValidationInterval(30000);
      pp.setTimeBetweenEvictionRunsMillis(30000);
      pp.setMaxActive(100);
      pp.setInitialSize(10);
      pp.setMaxWait(10000);
      pp.setRemoveAbandonedTimeout(60);
      pp.setMinEvictableIdleTimeMillis(30000);
      pp.setMinIdle(10);
      pp.setLogAbandoned(true);
      pp.setRemoveAbandoned(true);
      pp.setJdbcInterceptors(
          ConnectionState.class.getName() + ";" + StatementFinalizer.class.getName());
      final org.apache.tomcat.jdbc.pool.DataSource ds =
          new org.apache.tomcat.jdbc.pool.DataSource();
      ds.setPoolProperties(pp);
      return ds;
    }

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(OraclePlatform.class.getName(), basePackages);
    }
  }
}
