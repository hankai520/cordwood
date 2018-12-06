/*******************************************************************************
 * Copyright (C) 2018 hankai
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
    Assert.assertEquals("市场监管法规", appInfo.getName());
    Assert.assertEquals("cn.com.sparksoft.flfg", appInfo.getBundleId());
    Assert.assertEquals("1.0.5#150", appInfo.getVersion());
    Assert.assertEquals("res/drawable-xhdpi-v4/app_logo.png", appInfo.getIconName());
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
    Assert.assertEquals("LAWDICT", appInfo.getName());
    Assert.assertEquals("cn.com.sparksoft.flfg", appInfo.getBundleId());
    Assert.assertEquals("1.0.6#212", appInfo.getVersion());
    Assert.assertEquals("AppIcon60x60", appInfo.getIconName());
    Assert.assertNotNull(appInfo.getIcon());

    Assert.assertNotNull(appInfo.getBase64Icon());
    Assert.assertTrue(Base64.isBase64(appInfo.getBase64Icon()));
    Assert.assertEquals("testPath", appInfo.getBundlePath());
    Assert.assertTrue(appInfo.isValid());
  }

}
