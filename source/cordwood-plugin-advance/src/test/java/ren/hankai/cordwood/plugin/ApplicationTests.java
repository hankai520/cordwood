
package ren.hankai.cordwood.plugin;

import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.advance.util.Slf4jSessionLogger;
import ren.hankai.cordwood.plugin.config.PluginConfig;
import ren.hankai.cordwood.plugin.support.PluginRequestDispatcher;

import java.io.File;
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
@ContextConfiguration(classes = ApplicationTests.class)
@EnableJpaRepositories(basePackages = {"ren.hankai.cordwood"})
@EnableTransactionManagement
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
@ActiveProfiles(PluginRequestDispatcher.PROFILE_STANDALONE_MODE)
@Import({PluginConfig.class})
@Configuration
public abstract class ApplicationTests {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    Assert.assertTrue(ApplicationInitializer.initialize("ehcache.xml"));
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
        } catch (final Exception ex) {
          // Kindly ignore this exception
          ex.getMessage();
        }
      }
    });
  }

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
    final DriverManagerDataSource ds = new DriverManagerDataSource();
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
