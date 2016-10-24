
package ren.hankai.cordwood.core;

import org.junit.Assert;
import org.junit.Test;

import ren.hankai.cordwood.TestSupport;

import java.io.File;

/**
 * 程序初始化器测试
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:02:16 PM
 */
public class ApplicationInitializerTest extends TestSupport {

  @Test
  public void testInitialize() throws Exception {
    String[] subDirs = { Preferences.getConfigDir(), Preferences.getDataDir(),
        Preferences.getCacheDir(), Preferences.getLogDir(), Preferences.getTempDir(),
        Preferences.getAttachmentDir(), Preferences.getBackupDir(), Preferences.getDbDir(),
        Preferences.getPluginsDir(), Preferences.getLibsDir() };
    for (String dir : subDirs) {
      File file = new File(dir);
      Assert.assertTrue(file.exists() && file.isDirectory());
    }
    File file = new File(Preferences.getConfigFilePath("testSupport.txt"));
    Assert.assertTrue(file.exists() && file.isFile());
  }
}
