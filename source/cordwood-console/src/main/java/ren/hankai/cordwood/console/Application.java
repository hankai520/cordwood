
package ren.hankai.cordwood.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ren.hankai.cordwood.core.ApplicationInitializer;

/**
 * 控制台程序入口。
 *
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:18:12 AM
 */
@SpringBootApplication
@EnableSpringConfigured
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = {"ren.hankai"})
@EnableTransactionManagement
@EnableJdbcHttpSession
@EnableScheduling
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  /**
   * 自启动入口。
   *
   * @param args 命令行参数
   * @author hankai
   * @since Oct 25, 2016 10:53:27 AM
   */
  public static void main(String[] args) {
    final String[] configs = {"hsql.properties", "system.yml"};
    if (ApplicationInitializer.initialize(configs)) {
      if (SpringApplication.run(Application.class, args) != null) {
        logger.info(
            "\n============== JVM Arguments ==============\n\n" + System.getProperties().toString()
                + "\n\n" + "==========================================================\n");
        logger.info(
            "\n============== Environment Variables ==============\n\n" + System.getenv().toString()
                + "\n\n" + "==========================================================\n");
      }
    }
  }
}
