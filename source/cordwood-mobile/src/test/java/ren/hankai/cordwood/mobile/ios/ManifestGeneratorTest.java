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
