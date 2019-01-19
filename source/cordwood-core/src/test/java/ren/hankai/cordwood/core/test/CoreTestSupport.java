/*******************************************************************************
 * Copyright (C) 2019 hankai
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

package ren.hankai.cordwood.core.test;

import static org.junit.Assert.fail;

import ch.qos.logback.classic.Level;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;
import ren.hankai.cordwood.core.ApplicationInitInfo;
import ren.hankai.cordwood.core.ApplicationInitializer;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.test.config.BeanConfig;
import ren.hankai.cordwood.core.util.LogbackUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * 单元测试基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:05:07 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreTestSupport.class, BeanConfig.class})
@ActiveProfiles({Preferences.PROFILE_TEST})
@Configuration
@ComponentScan(basePackages = {"ren.hankai"})
public abstract class CoreTestSupport {

  static {
    System.setProperty(Preferences.ENV_APP_HOME_DIR, "./test-home");

    LogbackUtil.setupConsoleLoggerFor(Level.WARN);
    LogbackUtil.setupConsoleLoggerFor("ren.hankai.cordwood", Level.DEBUG);

    final ApplicationInitInfo initInfo =
        ApplicationInitInfo.initWithConfigs("testSupport.txt");
    initInfo.addSupportFiles("system.yml");
    initInfo.addTemplates("ut_only.txt");
    Assert.assertTrue(
        ApplicationInitializer.initialize(false, initInfo));

    Assert.assertNotNull(Preferences.getAttachmentDir());
    Assert.assertNotNull(Preferences.getBackupDir());
    Assert.assertNotNull(Preferences.getCacheDir());
    Assert.assertNotNull(Preferences.getConfigDir());
    Assert.assertTrue(Preferences.getConfigFilePath("test.conf").contains("test.conf"));
    Assert.assertEquals("1", Preferences.getCustomConfig("test"));
    Assert.assertNotNull(Preferences.getDataDir());
    Assert.assertNotNull(Preferences.getDbConfigFile());
    Assert.assertTrue(Preferences.getDbConfigFile("db.conf").contains("db.conf"));
    Assert.assertNotNull(Preferences.getDbDir());
    Assert.assertNotNull(Preferences.getHomeDir());
    Assert.assertNotNull(Preferences.getLibsDir());
    Assert.assertNotNull(Preferences.getLogDir());
    Assert.assertNotNull(Preferences.getPluginsDir());
    Assert.assertNotNull(Preferences.getTempDir());
    Assert.assertNotNull(Preferences.getTemplatesDir());

    Assert.assertEquals(7, Preferences.getApiAccessTokenExpiry().intValue());
    Assert.assertEquals("ut.test", Preferences.getProxyName());
    Assert.assertEquals(123, Preferences.getProxyPort().intValue());
    Assert.assertEquals("https", Preferences.getProxyScheme());
    Assert.assertEquals("46ee56cd32f85737", Preferences.getSystemSk());
    Assert.assertEquals("51f72611acf6df792025ae5ce341b01f", Preferences.getTransferKey());

    try {
      final File srcLib = ResourceUtils.getFile("classpath:empty.jar");
      final String destLibPath = Preferences.getLibsDir() + File.separator + "empty.jar";
      IOUtils.copy(new FileInputStream(srcLib), new FileOutputStream(destLibPath));
      Assert.assertEquals(1, Preferences.getLibUrls().length);
      Assert.assertEquals(2, Preferences.getLibUrls(new URL("file:///test")).length);
    } catch (final Exception e) {
      fail("Failed to test Preferences.getLibUrls() method!");
    }

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
