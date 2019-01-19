/*******************************************************************************
 * Copyright (C) 2019 hankai
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

package ren.hankai.cordwood.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用初始化信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Aug 13, 2017 11:46:53 AM
 */
public final class ApplicationInitInfo {

  /**
   * support 根目录下的默认配置文件。
   */
  private final List<String> configFiles = new ArrayList<>(3);

  /**
   * support/templates 下的默认模板文件。
   */
  private final List<String> templates = new ArrayList<>();

  private ApplicationInitInfo() {}

  /**
   * 用默认配置文件构造应用初始化信息。
   *
   * @param supportFileNames 默认配置文件
   * @return 应用初始化信息
   * @author hankai
   * @since Aug 13, 2017 11:54:45 AM
   */
  public static ApplicationInitInfo initWithConfigs(String... supportFileNames) {
    final ApplicationInitInfo info = new ApplicationInitInfo();
    if (supportFileNames != null) {
      for (final String string : supportFileNames) {
        info.configFiles.add(string);
      }
    }
    return info;
  }

  /**
   * 添加默认应用配置文件。
   *
   * @param supportFileNames 默认配置文件
   * @author hankai
   * @since Aug 13, 2017 11:56:37 AM
   */
  public void addSupportFiles(String... supportFileNames) {
    if (supportFileNames != null) {
      for (final String string : supportFileNames) {
        configFiles.add(string);
      }
    }
  }

  /**
   * 添加应用默认模板文件。
   *
   * @param templates 模板文件名（support/templates/abc.html，则传 abc.html）
   * @author hankai
   * @since Aug 13, 2017 11:56:55 AM
   */
  public void addTemplates(String... templates) {
    if (templates != null) {
      for (final String string : templates) {
        this.templates.add(string);
      }
    }
  }

  /**
   * 获取 configFiles 字段的值。
   *
   * @return configFiles 字段值
   */
  public List<String> getConfigFiles() {
    return configFiles;
  }

  /**
   * 获取 templates 字段的值。
   *
   * @return templates 字段值
   */
  public List<String> getTemplates() {
    return templates;
  }

}
