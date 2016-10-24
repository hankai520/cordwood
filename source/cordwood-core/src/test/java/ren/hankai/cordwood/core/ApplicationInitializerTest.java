
package ren.hankai.cordwood.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import ren.hankai.cordwood.TestSupport;

/**
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 1:02:16 PM
 */
public class ApplicationInitializerTest extends TestSupport {

  /**
   * Test method for
   * {@link ren.hankai.cordwood.core.ApplicationInitializer#initialize(java.lang.String[])}.
   */
  @Test
  public void testInitialize() throws Exception {
    String[] subDirs = {Preferences.getConfigDir(), Preferences.getDataDir(),
        Preferences.getCacheDir(), Preferences.getLogDir(), Preferences.getTempDir(),
        Preferences.getAttachmentDir(), Preferences.getBackupDir(), Preferences.getDbDir(),
        Preferences.getPluginsDir(), Preferences.getLibsDir()};
    for (String dir : subDirs) {
      File file = new File(dir);
      Assert.assertTrue(file.exists() && file.isDirectory());
    }
    File file = new File(Preferences.getConfigFilePath("testSupport.txt"));
    Assert.assertTrue(file.exists() && file.isFile());
  }
}
