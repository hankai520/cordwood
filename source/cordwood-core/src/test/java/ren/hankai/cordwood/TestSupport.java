
package ren.hankai.cordwood;

import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.CoreSpringConfig;
import ren.hankai.cordwood.core.Preferences;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * 单元测试基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:05:07 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSupport.class, CoreSpringConfig.class})
@Configuration
@ComponentScan(basePackages = {"ren.hankai"})
public abstract class TestSupport {

  @Before
  public void setup() throws Exception {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    Assert.assertTrue(ApplicationInitializer.initialize("testSupport.txt"));
  }

  @After
  public void teardown() throws Exception {
    FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
  }
}
