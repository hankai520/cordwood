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
import org.apache.commons.lang3.StringUtils;

/**
 * APP 元数据。
 *
 * @author hankai
 * @version 1.0
 * @since Aug 1, 2016 2:21:55 PM
 */
public class MobileAppInfo {

  private String name; // App 名称
  private String version; // 版本号
  private String bundleId; // 包ID
  private String iconName; // 图标文件名
  private byte[] icon; // 应用图标
  private String bundlePath;// 应用包缓存位置

  /**
   * 获取 name 字段的值。
   *
   * @return name 字段值
   */
  public String getName() {
    return name;
  }

  /**
   * 设置 name 字段的值。
   *
   * @param name name 字段的值
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取 version 字段的值。
   *
   * @return version 字段值
   */
  public String getVersion() {
    return version;
  }

  /**
   * 设置 version 字段的值。
   *
   * @param version version 字段的值
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * 获取 bundleId 字段的值。
   *
   * @return bundleId 字段值
   */
  public String getBundleId() {
    return bundleId;
  }

  /**
   * 设置 bundleId 字段的值。
   *
   * @param bundleId bundleId 字段的值
   */
  public void setBundleId(String bundleId) {
    this.bundleId = bundleId;
  }

  /**
   * 获取 iconName 字段的值。
   *
   * @return iconName 字段值
   */
  public String getIconName() {
    return iconName;
  }

  /**
   * 设置 iconName 字段的值。
   *
   * @param iconName iconName 字段的值
   */
  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  /**
   * 获取 icon 字段的值。
   *
   * @return icon 字段值
   */
  public byte[] getIcon() {
    return icon;
  }

  /**
   * 获取 icon 内容的 base64 编码字串。
   *
   * @return 图标的 base64 编码字串
   * @author hankai
   * @since Nov 24, 2016 5:58:50 PM
   */
  public String getBase64Icon() {
    if ((icon == null) || (icon.length == 0)) {
      return "";
    }
    return Base64.encodeBase64String(icon);
  }

  /**
   * 设置 icon 字段的值。
   *
   * @param icon icon 字段的值
   */
  public void setIcon(byte[] icon) {
    this.icon = icon;
  }

  /**
   * 获取 bundlePath 字段的值。
   *
   * @return bundlePath 字段值
   */
  public String getBundlePath() {
    return bundlePath;
  }

  /**
   * 设置 bundlePath 字段的值。
   *
   * @param bundlePath bundlePath 字段的值
   */
  public void setBundlePath(String bundlePath) {
    this.bundlePath = bundlePath;
  }

  /**
   * 验证 app 信息是否有效。
   *
   * @return 是否有效
   * @author hankai
   * @since Nov 23, 2016 4:05:31 PM
   */
  public boolean isValid() {
    return StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(version)
        && StringUtils.isNotEmpty(bundleId) && (icon != null);
  }

}
