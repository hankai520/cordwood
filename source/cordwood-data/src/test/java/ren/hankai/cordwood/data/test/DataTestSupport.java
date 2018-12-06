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
package ren.hankai.cordwood.data.test;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import ren.hankai.cordwood.core.ApplicationInitInfo;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.data.jpa.config.JpaDataSourceConfig;
import ren.hankai.cordwood.data.jpa.config.JpaDataSourceInfo;
import ren.hankai.cordwood.data.jpa.logging.Slf4jSessionLogger;
import ren.hankai.cordwood.data.jpa.support.BaseRepositoryFactoryBean;

import java.io.File;
import java.util.Map;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DataTestSupport.class})
@ActiveProfiles({Preferences.PROFILE_TEST, Preferences.PROFILE_HSQL})
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
@Configuration
public abstract class DataTestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    final String[] configs = {"system.yml", "hsql.properties"};
    final ApplicationInitInfo initInfo = ApplicationInitInfo.initWithConfigs(configs);
    final String[] templates = {};
    initInfo.addTemplates(templates);
    Assert.assertTrue(
        ApplicationInitializer.initialize(false, initInfo));
    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        try {
          sleep(1000); // wait for application shutdown.
          FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
        } catch (final Exception ex) {
          // Kindly ignore this exception since it just resource clean up.
          ex.getMessage();
        }
      }
    });
  }

  /**
   * 单元测试数据库连接配置。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 3, 2017 4:48:48 PM
   */
  @Profile(Preferences.PROFILE_TEST)
  @Configuration
  public static class DataSourceConfig extends JpaDataSourceConfig {

    static {
      basePackages = new String[] {"ren.hankai.cordwood"};
    }

  }

  /**
   * JPA配置类。
   *
   * @author hankai
   * @version 1.0.0
   * @since Dec 3, 2018 1:16:16 PM
   */
  @EnableJpaRepositories(basePackages = {"ren.hankai.cordwood.data.jpa.repository"},
      repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
  @EnableTransactionManagement
  @Profile(Preferences.PROFILE_TEST)
  @Configuration
  public static class JpaConfiguration extends JpaBaseConfiguration {

    @Autowired
    private JpaDataSourceInfo dataSourceInfo;

    /**
     * 初始化 JPA 配置。
     *
     * @param dataSource 数据源
     * @param properties JPA配置属性
     * @param jtaTransactionManager 事务管理器
     * @param transactionManagerCustomizers 自定义事务管理
     */
    protected JpaConfiguration(DataSource dataSource, JpaProperties properties,
        ObjectProvider<JtaTransactionManager> jtaTransactionManager,
        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
      super(dataSource, properties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected String[] getPackagesToScan() {
      final String[] packages = dataSourceInfo.getPackagesToScan();
      return packages;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
      return dataSourceInfo.createJpaVendorAdapter(true);
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
      final Map<String, Object> jpaProperties = dataSourceInfo.getVendorProperties();
      final boolean enabled =
          Boolean.parseBoolean(Preferences.getCustomConfig("persistence.profiler.enabled"));
      if (enabled) {
        jpaProperties.put("eclipselink.logging.file",
            Preferences.getTempDir() + File.separator + "eclipselink_performance.txt");
      } else {
        jpaProperties.put("eclipselink.logging.logger", Slf4jSessionLogger.class.getName());
      }
      jpaProperties.put("eclipselink.ddl-generation", "drop-and-create-tables");
      jpaProperties.put("eclipselink.ddl-generation.output-mode", "database");
      return jpaProperties;
    }
  }
}
