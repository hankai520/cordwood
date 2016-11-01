
package ren.hankai.cordwood.plugin.test;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.config.PluginConfig;

import java.io.File;
import java.net.URL;

/**
 * 单元测试基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:05:07 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PluginTestSupport.class})
@Import({CoreSpringConfig.class, PluginConfig.class})
@Configuration
@ComponentScan(basePackages = {"ren.hankai.cordwood"})
public abstract class PluginTestSupport {

  protected URL testPluginPackageUrl;

  @Before
  public void setup() throws Exception {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    Assert.assertTrue(ApplicationInitializer.initialize("testSupport.txt"));
    testPluginPackageUrl = ResourceUtils.getURL("classpath:cordwood-plugin-pojo-0.0.1.RELEASE.jar");
  }

  @After
  public void teardown() throws Exception {
    FileUtils.deleteDirectory(new File(Preferences.getHomeDir()));
  }
}
