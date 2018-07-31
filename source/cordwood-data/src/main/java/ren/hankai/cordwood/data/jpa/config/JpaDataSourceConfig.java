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

  /**
   * 默认构造方法，用当前类所在包作为默认值，初始化基包。
   */
  public JpaDataSourceConfig() {
    String pkg = this.getClass().getPackage().getName();
    if (StringUtils.isNotEmpty(pkg) && (basePackages == null)) {
      pkg = pkg.split("\\.")[0];
      basePackages = new String[] {pkg};
    }
  }

  /**
   * 配置连接池。将连接池配置文件中的参数配置到连接池属性中。
   *
   * @param props 数据源配置文件（e.g. mysql.properties）
   * @param poolProps 连接池属性
   * @author hankai
   * @since Jul 31, 2018 3:21:48 PM
   */
  private static void configureDataSourcePool(Properties props, PoolProperties poolProps) {
    poolProps.setJmxEnabled(false);
    poolProps.setTestWhileIdle(false); // 在连接空闲时，是否检查连接可用性，关闭以提高性能
    poolProps.setTestOnBorrow(true); // 是否检查在从池中获取的链接的可用性，打开，避免无效连接导致语句执行失败
    poolProps.setTestOnReturn(false); // 归还连接到池中时，是否检查连接可用性，关闭以提高性能，因为获取连接时已检查
    poolProps.setLogAbandoned(true); // 当连接被丢弃时，是否打印日志，关闭以提高性能
    poolProps.setRemoveAbandoned(true); // 是否清除被丢弃的连接（关闭连接，从池中移除）

    // 每次做空闲检查、丢弃清理和池大小伸缩作业之间的休息时间
    String param = props.getProperty("pool.time.between.eviction.runs.millis");
    if (StringUtils.isEmpty(param)) {
      poolProps.setTimeBetweenEvictionRunsMillis(10 * 1000);
    } else {
      poolProps.setTimeBetweenEvictionRunsMillis(Integer.parseInt(param));
    }

    // 最快多长时间检查一次连接可用性，用于限制频繁检查带来的性能下降
    param = props.getProperty("pool.validation.interval");
    if (StringUtils.isEmpty(param)) {
      poolProps.setValidationInterval(30 * 1000);
    } else {
      poolProps.setValidationInterval(Integer.parseInt(param));
    }

    // 最大激活连接数
    param = props.getProperty("pool.max.active");
    if (StringUtils.isEmpty(param)) {
      poolProps.setMaxActive(20);
    } else {
      poolProps.setMaxActive(Integer.parseInt(param));
    }

    // 池初始连接数
    param = props.getProperty("pool.initial.size");
    if (StringUtils.isEmpty(param)) {
      poolProps.setInitialSize(4);
    } else {
      poolProps.setInitialSize(Integer.parseInt(param));
    }

    // 从池中获取连接时，最长等待多长时间
    param = props.getProperty("pool.max.wait");
    if (StringUtils.isEmpty(param)) {
      poolProps.setMaxWait(20 * 1000);
    } else {
      poolProps.setMaxWait(Integer.parseInt(param));
    }

    // 多长时间内，不考虑丢弃连接
    param = props.getProperty("pool.remove.abandoned.timeout");
    if (StringUtils.isEmpty(param)) {
      poolProps.setRemoveAbandonedTimeout(60);
    } else {
      poolProps.setRemoveAbandonedTimeout(Integer.parseInt(param));
    }

    // 连接在池中必须存在多长时间后，才能被标记为可回收的
    param = props.getProperty("pool.min.evictable.idle.time.millis");
    if (StringUtils.isEmpty(param)) {
      poolProps.setMinEvictableIdleTimeMillis(30 * 1000);
    } else {
      poolProps.setMinEvictableIdleTimeMillis(Integer.parseInt(param));
    }

    // 池中至少保持多少个空闲连接
    param = props.getProperty("pool.min.idle");
    if (StringUtils.isEmpty(param)) {
      poolProps.setMinIdle(4);
    } else {
      poolProps.setMinIdle(Integer.parseInt(param));
    }

    poolProps.setJdbcInterceptors(
        ConnectionState.class.getName() + ";" + StatementFinalizer.class.getName());
  }

  /**
   * HSQL 数据源配置。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 31, 2018 2:59:52 PM
   */
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

  /**
   * MySQL 数据源配置。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 31, 2018 3:00:14 PM
   */
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

      pp.setValidationQuery("select 1"); // 用于检查连接是否可用的sql语句
      JpaDataSourceConfig.configureDataSourcePool(props, pp);

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

  /**
   * Oracle 数据源配置。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 31, 2018 3:00:41 PM
   */
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

      pp.setValidationQuery("select * from dual");
      JpaDataSourceConfig.configureDataSourcePool(props, pp);

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
