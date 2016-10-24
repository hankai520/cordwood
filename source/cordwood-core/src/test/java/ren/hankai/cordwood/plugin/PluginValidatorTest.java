
package ren.hankai.cordwood.plugin;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.Preferences;

/**
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:26:43 PM
 */
public class PluginValidatorTest extends TestSupport {

  /**
   * Test method for
   * {@link ren.hankai.cordwood.plugin.PluginValidator#validatePackage(java.net.URL)}.
   */
  @Test
  public void testValidatePackage() throws Exception {
    URL url = ResourceUtils.getURL("classpath:pojo-0.0.1.RELEASE.jar");
    String fileName = FilenameUtils.getName(url.getPath());
    FileOutputStream fos =
        new FileOutputStream(Preferences.getPluginsDir() + File.separator + fileName);
    FileCopyUtils.copy(url.openStream(), fos);
    url = new File(Preferences.getPluginsDir() + File.separator + "pojo-0.0.1.RELEASE.jar").toURI()
        .toURL();
    Assert.assertTrue(pluginValidator.validatePackage(url));
  }
}
