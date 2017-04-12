
package ren.hankai.cordwood.plugin.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.config.PluginConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");
    Assert.assertTrue(ApplicationInitializer.initialize("ehcache.xml"));
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

  /**
   * 单元测试用例初始化。
   *
   * @throws Exception 异常
   * @author hankai
   * @since Nov 8, 2016 8:48:22 AM
   */
  @Before
  public void setup() throws Exception {
    final String fileName = "cordwood-plugin-pojo-0.0.1.RELEASE.jar";
    testPluginPackageUrl = ResourceUtils.getURL("classpath:" + fileName);
    InputStream input = null;
    OutputStream output = null;
    try {
      input = testPluginPackageUrl.openStream();
      final File file = new File(Preferences.getPluginsDir() + File.separator + fileName);
      if (!file.exists()) {
        output = new FileOutputStream(file);
        FileCopyUtils.copy(input, output);
      }
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      IOUtils.closeQuietly(output);
      IOUtils.closeQuietly(input);
    }
  }

  /**
   * 单元测试用例资源销毁。
   *
   * @throws Exception 异常
   * @author hankai
   * @since Nov 8, 2016 8:48:49 AM
   */
  @After
  public void teardown() {

  }
}
