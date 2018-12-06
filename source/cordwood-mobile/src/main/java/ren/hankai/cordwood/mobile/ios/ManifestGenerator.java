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

import com.dd.plist.BinaryPropertyListWriter;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.mobile.MobileAppInfo;

import java.io.IOException;

/**
 * iOS App 的 manifest xml 文件生成器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2016 12:57:44 PM
 */
public class ManifestGenerator {

  private static final Logger logger = LoggerFactory.getLogger(ManifestGenerator.class);

  /**
   * 根据APP包信息生成并设置Assets节点信息。
   *
   * @param mobileApp APP应用包信息
   * @param dict Manifest信息
   * @param packageUrl 应用包下载链接
   * @param displayImageUrl 应用图标下载链接
   * @param fullSizeImageUrl 全尺寸应用图标下载链接
   * @author hankai
   * @since Nov 22, 2018 3:59:00 PM
   */
  private static void setAssetsInfo(MobileAppInfo mobileApp, NSDictionary dict, String packageUrl,
      String displayImageUrl, String fullSizeImageUrl) {
    final NSDictionary packageDict = new NSDictionary();
    packageDict.put("kind", "software-package");
    packageDict.put("url", packageUrl);
    final NSDictionary displayImageDict = new NSDictionary();
    displayImageDict.put("kind", "display-image");
    displayImageDict.put("url", displayImageUrl);
    final NSDictionary fullSizeImageDict = new NSDictionary();
    fullSizeImageDict.put("kind", "full-size-image");
    fullSizeImageDict.put("url", fullSizeImageUrl);
    dict.put("assets", new NSArray(packageDict, displayImageDict, fullSizeImageDict));
  }

  /**
   * 根据APP应用包信息生成并设置APP元数据。
   *
   * @param mobileApp APP应用包信息
   * @param dict Manifest数据
   * @author hankai
   * @since Nov 22, 2018 4:01:02 PM
   */
  private static void setMetaData(MobileAppInfo mobileApp, NSDictionary dict) {
    final NSDictionary metadata = new NSDictionary();
    metadata.put("bundle-identifier", mobileApp.getBundleId());
    metadata.put("bundle-version", mobileApp.getVersion());
    metadata.put("kind", "software");
    metadata.put("title", mobileApp.getName());
    dict.put("metadata", metadata);
  }

  /**
   * 生成 manifest 文件内容。
   *
   * @param mobileApp 应用信息
   * @param packageUrl 安装包 URL
   * @param displayImageUrl 应用图标 URL
   * @param fullSizeImageUrl 全尺寸应用图标 URL
   * @return manifest 文件内容。
   * @author hankai
   * @since Nov 23, 2016 2:34:03 PM
   */
  public static byte[] generateManifest(MobileAppInfo mobileApp, String packageUrl,
      String displayImageUrl, String fullSizeImageUrl) {
    byte[] manifest = null;
    final NSDictionary root = new NSDictionary();
    final NSDictionary appInfo = new NSDictionary();
    setAssetsInfo(mobileApp, appInfo, packageUrl, displayImageUrl, fullSizeImageUrl);
    setMetaData(mobileApp, appInfo);
    root.put("items", new NSArray(appInfo));
    try {
      manifest = BinaryPropertyListWriter.writeToArray(root);
    } catch (final IOException e) {
      logger.error("Failed to generate ios app manifest.", e);
    }
    return manifest;
  }

}
