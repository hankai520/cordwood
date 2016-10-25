
package com.demo;

import com.demo.plugins.config.PluginBootstrap;
import com.demo.plugins.util.Slf4jSessionLogger;

import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * 基于 Spring boot 的单元测试基类，需要测试 spring mvc，只需要继承此类即可。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 1:29:53 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PluginBootstrap.class})
@ComponentScan(basePackages = {"com.demo"})
@Configuration
@ActiveProfiles({"unit-test"})
public abstract class ApplicationTests {

  @Before
  public void setup() {
    initTestFixures();
  }

  private void initTestFixures() {}

  /**
   * 定义测试数据源。
   * 
   * @return 数据源
   * @author hankai
   * @since Oct 25, 2016 2:15:51 PM
   */
  @Bean
  @Primary
  public DataSource dataSource() {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.hsqldb.jdbcDriver");
    ds.setUrl("jdbc:hsqldb:mem:ut-db");
    ds.setUsername("sa");
    ds.setPassword(null);
    return ds;
  }

  /**
   * 定义测试用的 EntityManager 工厂。
   * 
   * @param dataSource 数据源
   * @return EntityManager 工厂
   * @author hankai
   * @since Oct 25, 2016 2:16:09 PM
   */
  @Bean(name = "entityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setPersistenceUnitName("defaultUnit");
    factory.setDataSource(dataSource);
    EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
    adapter.setDatabasePlatform(HSQLPlatform.class.getName());
    adapter.setShowSql(true);
    factory.setJpaVendorAdapter(adapter);
    factory.setPackagesToScan("com.demo");
    Properties jpaProperties = new Properties();
    jpaProperties.setProperty("eclipselink.target-database", HSQLPlatform.class.getName());
    jpaProperties.setProperty("eclipselink.ddl-generation", "drop-and-create-tables");
    // this controls what will be logged during DDL execution
    jpaProperties.setProperty("eclipselink.ddl-generation.output-mode", "both");
    jpaProperties.setProperty("eclipselink.weaving", "static");
    jpaProperties.setProperty("eclipselink.logging.level", "FINE");
    jpaProperties.setProperty("eclipselink.logging.parameters", "true");
    jpaProperties.setProperty("eclipselink.logging.logger", Slf4jSessionLogger.class.getName());
    factory.setJpaProperties(jpaProperties);
    return factory;
  }
}
