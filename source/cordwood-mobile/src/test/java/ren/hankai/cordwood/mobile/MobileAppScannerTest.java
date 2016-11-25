
package ren.hankai.cordwood.mobile;

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
    Assert.assertEquals("市场监管法规", appInfo.getName());
    Assert.assertEquals("cn.com.sparksoft.flfg", appInfo.getBundleId());
    Assert.assertEquals("1.0.5#150", appInfo.getVersion());
    Assert.assertEquals("res/drawable-xhdpi-v4/app_logo.png", appInfo.getIconName());
    Assert.assertNotNull(appInfo.getIcon());
  }

  @Test
  public void testScanIosIpa() {
    final URL url = getClass().getClassLoader().getResource("test.ipa");
    final MobileAppInfo appInfo = MobileAppScanner.scanIosIpa(url.getPath());
    Assert.assertEquals("LAWDICT", appInfo.getName());
    Assert.assertEquals("cn.com.sparksoft.flfg", appInfo.getBundleId());
    Assert.assertEquals("1.0.6#212", appInfo.getVersion());
    Assert.assertEquals("AppIcon60x60", appInfo.getIconName());
    Assert.assertNotNull(appInfo.getIcon());
  }

}
