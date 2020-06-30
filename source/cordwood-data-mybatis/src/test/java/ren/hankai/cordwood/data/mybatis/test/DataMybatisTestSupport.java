
package ren.hankai.cordwood.data.mybatis.test;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.hankai.cordwood.core.ApplicationInitInfo;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DataMybatisTestSupport.class})
@ActiveProfiles({Preferences.PROFILE_TEST, Preferences.PROFILE_HSQL})
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
@Configuration
public abstract class DataMybatisTestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    final String[] configs = {"system.properties", "hsql.properties"};
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

}
