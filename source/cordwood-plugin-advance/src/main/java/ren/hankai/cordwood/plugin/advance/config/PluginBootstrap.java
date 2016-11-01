
package ren.hankai.cordwood.plugin.advance.config;

import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ren.hankai.cordwood.plugin.advance.util.Slf4jSessionLogger;
import ren.hankai.cordwood.plugin.config.PluginConfig;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * 当前插件包的核心 spring 配置类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 8:48:53 AM
 */
@Configuration
@EnableJpaRepositories(basePackages = {"ren.hankai.cordwood.plugin"})
@EnableTransactionManagement
@Import({PluginConfig.class})
@ComponentScan(basePackages = "ren.hankai.cordwood.plugin")
public class PluginBootstrap {

  /**
   * 定义数据源。
   *
   * @return 数据源
   * @author hankai
   * @since Oct 25, 2016 1:06:41 PM
   */
  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.hsqldb.jdbcDriver");
    ds.setUrl("jdbc:hsqldb:mem:ut-db");
    ds.setUsername("sa");
    ds.setPassword(null);
    return ds;
  }

  /**
   * 定义用于生产 JPA 核心对象 EntityManager 的工厂。
   *
   * @param dataSource 数据源
   * @return EntityManager 工厂
   * @author hankai
   * @since Oct 25, 2016 1:07:02 PM
   */
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean factory =
        new LocalContainerEntityManagerFactoryBean();
    factory.setPersistenceUnitName("defaultUnit");
    factory.setDataSource(dataSource);
    final EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
    adapter.setDatabasePlatform(HSQLPlatform.class.getName());
    adapter.setShowSql(true);
    factory.setJpaVendorAdapter(adapter);
    factory.setPackagesToScan("ren.hankai.cordwood.plugin");
    final Properties jpaProperties = new Properties();
    jpaProperties.setProperty("eclipselink.target-database", HSQLPlatform.class.getName());
    jpaProperties.setProperty("eclipselink.ddl-generation", "drop-and-create-tables");
    // this controls what will be logged during DDL execution
    // jpaProperties.setProperty("eclipselink.ddl-generation.output-mode", "both");
    jpaProperties.setProperty("eclipselink.weaving", "static");
    jpaProperties.setProperty("eclipselink.logging.level", "FINE");
    jpaProperties.setProperty("eclipselink.logging.parameters", "true");
    jpaProperties.setProperty("eclipselink.logging.logger", Slf4jSessionLogger.class.getName());
    factory.setJpaProperties(jpaProperties);
    return factory;
  }

  /**
   * 定义事务管理器。
   *
   * @return 事务管理器
   * @author hankai
   * @since Oct 25, 2016 1:07:56 PM
   */
  @Bean(name = "transactionManager")
  public PlatformTransactionManager getTransactionManager() {
    return new JpaTransactionManager();
  }
}
