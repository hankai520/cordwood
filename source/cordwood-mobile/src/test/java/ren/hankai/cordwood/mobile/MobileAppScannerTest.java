
package ren.hankai.cordwood.mobile;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * app扫描器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2016 1:56:00 PM
 */
public class MobileAppScannerTest {

  @Test
  public void testScanAndroidApk() {
    final URL url = getClass().getClassLoader().getResource("test.apk");
    final MobileAppInfo appInfo = MobileAppScanner.scanAndroidApk(url.getPath());
    appInfo.setBundlePath("testPath");
    Assert.assertEquals("hello world", appInfo.getName());
    Assert.assertEquals("com.example.listsparknetcomcn.helloworld", appInfo.getBundleId());
    Assert.assertEquals("1.0#1", appInfo.getVersion());
    Assert.assertEquals("res/drawable/app_logo.png", appInfo.getIconName());
    Assert.assertNotNull(appInfo.getIcon());

    Assert.assertNotNull(appInfo.getBase64Icon());
    Assert.assertTrue(Base64.isBase64(appInfo.getBase64Icon()));
    Assert.assertEquals("testPath", appInfo.getBundlePath());
    Assert.assertTrue(appInfo.isValid());
  }

  @Test
  public void testScanIosIpa() {
    final URL url = getClass().getClassLoader().getResource("test.ipa");
    final MobileAppInfo appInfo = MobileAppScanner.scanIosIpa(url.getPath());
    appInfo.setBundlePath("testPath");
    Assert.assertEquals("helloworld", appInfo.getName());
    Assert.assertEquals("cn.sparknet.helloworld.portal.helloworld", appInfo.getBundleId());
    Assert.assertEquals("1.0#1", appInfo.getVersion());
    Assert.assertEquals("AppIcon60x60", appInfo.getIconName());
    Assert.assertNotNull(appInfo.getIcon());

    Assert.assertNotNull(appInfo.getBase64Icon());
    Assert.assertTrue(Base64.isBase64(appInfo.getBase64Icon()));
    Assert.assertEquals("testPath", appInfo.getBundlePath());
    Assert.assertTrue(appInfo.isValid());
  }

}
