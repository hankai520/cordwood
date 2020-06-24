
package ren.hankai.cordwood.oauth2.test;

import ch.qos.logback.classic.Level;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.hankai.cordwood.core.ApplicationInitInfo;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.util.LogbackUtil;
import ren.hankai.cordwood.oauth2.test.config.BeanConfig;

import java.io.File;

/**
 * 单元测试基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:31:52 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Oauth2TestSupport.class, BeanConfig.class})
@ActiveProfiles({Preferences.PROFILE_TEST})
@Configuration
@ComponentScan(basePackages = {"ren.hankai"})
public abstract class Oauth2TestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");

    LogbackUtil.setupConsoleLoggerFor(Level.WARN);
    LogbackUtil.setupConsoleLoggerFor("ren.hankai.cordwood", Level.DEBUG);

    final ApplicationInitInfo initInfo = ApplicationInitInfo.initWithConfigs();
    initInfo.addSupportFiles("system.yml");
    initInfo.addSupportFiles("oauth.jks");
    Assert.assertTrue(
        ApplicationInitializer.initialize(false, initInfo));

    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        try {
          sleep(1000);
          FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
        } catch (final Exception ex) {
          // Kindly ignore this exception
          ex.getMessage();
        }
      }
    });
  }
}
