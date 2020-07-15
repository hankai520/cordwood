
package ren.hankai.cordwood.data;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 抽象的数据源配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 15, 2019 6:11:00 PM
 */
public abstract class AbstractDataSourceConfig {

  private static final Logger logger = LoggerFactory.getLogger(AbstractDataSourceConfig.class);

  protected static String[] basePackages = null;

  /**
   * 从外部配置文件加载数据库连接配置。如果数据库配置子类需要在程序启动时，从程序包外部获取配置文件， 则调用此方法获取外部文件配置参数。
   *
   * @param fileName 文件名
   * @return 是否加载成功
   * @author hankai
   * @since Jun 21, 2016 10:45:48 AM
   */
  protected static Properties loadExternalConfig(final String fileName) {
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
   * 配置过滤器。
   *
   * @param props 从数据库连接配置文件中读取的参数
   * @param dataSource Druid数据源
   * @author hankai
   * @since Dec 3, 2018 5:23:54 PM
   */
  protected static void configureFilters(final Properties props, final DruidDataSource dataSource) {
    final List<Filter> filters = new ArrayList<>();
    // 记录慢SQL
    final StatFilter statFilter = new StatFilter();
    String param = props.getProperty("pool.slow.sql.millis", "5000");
    statFilter.setSlowSqlMillis(Long.parseLong(param));
    param = props.getProperty("pool.log.slow.sql", "true");
    statFilter.setLogSlowSql(Boolean.parseBoolean(param));
    // 合并统计没有参数化的sql
    param = props.getProperty("pool.merge.sql", "false");
    statFilter.setMergeSql(Boolean.parseBoolean(param));
    filters.add(statFilter); // 启用统计过滤器
    // 将日志桥接到SLF4J
    final Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
    slf4jLogFilter.setResultSetLogEnabled(false);
    filters.add(slf4jLogFilter);
    // 防止SQL注入攻击
    param = props.getProperty("pool.check.sql.injection", "false");
    if (Boolean.parseBoolean(param)) {
      // 更多Druid wall filter 配置，请访问 https://github.com/alibaba/druid/wiki/配置-wallfilter
      final WallFilter wallFilter = new WallFilter();
      // wallFilter.getConfig().setXX
      wallFilter.setLogViolation(true);// 记录被认为是SQL攻击的语句
      filters.add(wallFilter);
    }
    dataSource.setProxyFilters(filters);
  }

  /**
   * 配置连接池。将连接池配置文件中的参数配置到连接池属性中。
   *
   * @param props 数据源配置文件（e.g. mysql.properties）
   * @param dataSource Druid 数据源
   * @author hankai
   * @since Jul 31, 2018 3:21:48 PM
   */
  protected static void configureDataSourcePool(final Properties props,
      final DruidDataSource dataSource) {
    // 基本属性 url、user、password
    dataSource.setDriverClassName(props.getProperty("driverClassName"));
    dataSource.setUrl(props.getProperty("url"));
    dataSource.setUsername(props.getProperty("username"));
    dataSource.setPassword(props.getProperty("password"));

    // 配置初始化大小、最小、最大
    String param = props.getProperty("pool.initial.size", "1");
    dataSource.setInitialSize(Integer.parseInt(param));
    param = props.getProperty("pool.min.idle", "1");
    dataSource.setMinIdle(Integer.parseInt(param));
    param = props.getProperty("pool.max.active", "20");
    dataSource.setMaxActive(Integer.parseInt(param));

    // 配置获取连接等待超时的时间
    param = props.getProperty("pool.max.wait", "60000");
    dataSource.setMaxWait(Long.parseLong(param));

    // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    param = props.getProperty("pool.time.between.eviction.runs.millis", "60000");
    dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(param));

    // 配置一个连接在池中最小生存的时间，单位是毫秒
    param = props.getProperty("pool.min.evictable.idle.time.millis", "300000");
    dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(param));

    dataSource.setTestWhileIdle(true);// 是否在连接空闲时检查连接可用性
    dataSource.setTestOnBorrow(false);// 在从池中获取连接时是否检查连接可用性
    dataSource.setTestOnReturn(false);// 是否在归还连接到池中时检查连接可用性

    // 打开PSCache，并且指定每个连接上PSCache的大小
    dataSource.setPoolPreparedStatements(true);
    dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

    param = props.getProperty("pool.debug", "false");
    if (Boolean.parseBoolean(param)) {
      // 超过时间限制是否回收
      dataSource.setRemoveAbandoned(true);
      // 超时时间：单位为秒
      param = props.getProperty("pool.remove.abandoned.timeout", "180");
      dataSource.setRemoveAbandonedTimeout(Integer.parseInt(param));
      dataSource.setLogAbandoned(true);
    } else {
      dataSource.setRemoveAbandoned(false);
    }
    // 配置过滤器
    configureFilters(props, dataSource);
  }

  /**
   * 默认构造方法，用当前类所在包作为默认值，初始化基包。
   */
  public AbstractDataSourceConfig() {
    String pkg = this.getClass().getPackage().getName();
    if (StringUtils.isNotEmpty(pkg) && (basePackages == null)) {
      pkg = pkg.split("\\.")[0];
      basePackages = new String[] {pkg};
    }
  }

  /**
   * 数据源。
   *
   * @return 数据源
   */
  @Profile(Preferences.PROFILE_HSQL)
  @Bean
  public DataSource hsqlDataSource() {
    final Properties props = loadExternalConfig("hsql.properties");
    // 由于配置文件中无法指定在运行时才能确定的数据库文件存储目录，需要在此处重新设定
    String url = props.getProperty("url");
    if (url.contains("%s")) {
      // 包含通配符，说明需要设置数据库文件路径
      final String dbPath = Preferences.getDbDir() + File.separator + "application";
      url = String.format(props.getProperty("url"), dbPath);
      props.setProperty("url", url);
    }

    final DruidDataSource dataSource = new DruidDataSource();
    configureDataSourcePool(props, dataSource);
    // 用于检查连接是否可用的sql语句
    dataSource.setValidationQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
    return dataSource;
  }

  /**
   * MySQL 数据源。
   *
   * @return 数据源
   */
  @Profile(Preferences.PROFILE_MYSQL)
  @Bean
  public DataSource mysqlDataSource() {
    final Properties props = loadExternalConfig("mysql.properties");
    final DruidDataSource dataSource = new DruidDataSource();
    configureDataSourcePool(props, dataSource);
    dataSource.setValidationQuery("select 1");// 用于检查连接是否可用的sql语句
    return dataSource;
  }

  /**
   * ORACLE 数据源。
   *
   * @return 数据源
   */
  @Profile(Preferences.PROFILE_ORACLE)
  @Bean
  public DataSource oracleDataSource() {
    final Properties props = loadExternalConfig("oracle.properties");
    final DruidDataSource dataSource = new DruidDataSource();
    configureDataSourcePool(props, dataSource);
    dataSource.setValidationQuery("select * from dual");// 用于检查连接是否可用的sql语句
    return dataSource;
  }
}
