
package ren.hankai.cordwood.mobile.ios;

import org.junit.Assert;
import org.junit.Test;
import ren.hankai.cordwood.mobile.MobileAppInfo;
import ren.hankai.cordwood.mobile.MobileAppScanner;

import java.net.URL;

/**
 * Manifest 生成器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2016 2:18:05 PM
 */
public class ManifestGeneratorTest {

  @Test
  public void testGenerateManifest() throws Exception {
    final URL url = getClass().getClassLoader().getResource("test.ipa");
    final MobileAppInfo appInfo = MobileAppScanner.scanIosIpa(url.getPath());
    final byte[] manifest =
        ManifestGenerator.generateManifest(appInfo, "http://www.example.org/test.ipa",
            "http://www.example.org/small.png", "http://www.example.org/large.png");
    Assert.assertNotNull(manifest);
  }

}
