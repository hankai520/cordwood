/*******************************************************************************
 * Copyright (C) 2019 hankai
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

package ren.hankai.cordwood.data.jpa.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.data.AbstractDataSourceConfig;

import java.io.File;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 数据源配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 18, 2016 2:19:51 PM
 */
public abstract class JpaDataSourceConfig extends AbstractDataSourceConfig {

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
      final DruidDataSource dataSource = new DruidDataSource();
      configureDataSourcePool(props, dataSource);
      dataSource.setValidationQuery("select 1");// 用于检查连接是否可用的sql语句
      return dataSource;
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
      final DruidDataSource dataSource = new DruidDataSource();
      configureDataSourcePool(props, dataSource);
      dataSource.setValidationQuery("select * from dual");// 用于检查连接是否可用的sql语句
      return dataSource;
    }

    @Bean
    public JpaDataSourceInfo dataSourceInfo() {
      return new JpaDataSourceInfo(OraclePlatform.class.getName(), basePackages);
    }
  }
}
