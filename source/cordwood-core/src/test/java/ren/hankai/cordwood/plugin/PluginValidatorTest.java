
package ren.hankai.cordwood.plugin;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import ren.hankai.cordwood.TestSupport;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * 插件验证器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:26:43 PM
 */
public class PluginValidatorTest extends TestSupport {

  @Autowired
  protected PluginValidator pluginValidator;

  @Test
  public void testValidatePackage() throws Exception {
    String fileName = FilenameUtils.getName(testPluginPackageUrl.getPath());
    FileOutputStream fos =
        new FileOutputStream(Preferences.getPluginsDir() + File.separator + fileName);
    FileCopyUtils.copy(testPluginPackageUrl.openStream(), fos);
    URL url = new File(Preferences.getPluginsDir() + File.separator + fileName).toURI().toURL();
    Assert.assertTrue(pluginValidator.validatePackage(url));
  }
}
