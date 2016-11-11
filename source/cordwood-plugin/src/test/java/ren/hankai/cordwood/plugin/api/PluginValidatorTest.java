
package ren.hankai.cordwood.plugin.api;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.test.PluginTestSupport;

import java.io.File;
import java.net.URL;

/**
 * 插件验证器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 21, 2016 3:26:43 PM
 */
public class PluginValidatorTest extends PluginTestSupport {

  @Autowired
  protected PluginValidator pluginValidator;

  @Test
  public void testValidatePackage() throws Exception {
    final String fileName = FilenameUtils.getName(testPluginPackageUrl.getPath());
    final URL url =
        new File(Preferences.getPluginsDir() + File.separator + fileName).toURI().toURL();
    final PluginPackage pp = new PluginPackage(url);
    Assert.assertTrue(pluginValidator.validatePackage(pp));
  }
}
