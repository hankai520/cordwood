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

package ren.hankai.cordwood.web.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求检查接口，用于检查参数是否被篡改。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:12:54 PM
 *
 */
public interface RequestInspector {

  /**
   * 请求入参的签名字段名。
   */
  static final String REQUEST_SIGN = "sign";

  /**
   * 构建签名明文。
   *
   * @param parameters 入参
   * @param sk 秘钥
   * @return 签名原文
   * @author hankai
   * @since Aug 28, 2018 9:44:40 PM
   */
  String buildSignText(Map<String, ?> parameters, String sk);

  /**
   * 计算参数签名（使用首选项中配置的传输秘钥 Preferences.getTransferKey()）。
   *
   * @param parameters 参数集合
   * @return 签名字符串
   * @author hankai
   * @since Oct 31, 2016 10:22:35 PM
   */
  String signRequestParameters(Map<String, ?> parameters);

  /**
   * 用自定义秘钥计算参数签名。
   *
   * @param parameters 参数集合
   * @param sk 秘钥
   * @return 签名字符串
   * @author hankai
   * @since Jan 26, 2018 8:44:36 PM
   */
  String signRequestParameters(Map<String, ?> parameters, String sk);

  /**
   * 对请求体进行签名。
   *
   * @param requestBody HTTP请求体
   * @return 签名字符串
   * @author hankai
   * @since Nov 22, 2018 3:27:34 PM
   */
  String signRequestBody(String requestBody);

  /**
   * 对请求体进行签名。
   *
   * @param requestBody 请求体（Http body）
   * @param sk 秘钥
   * @return 签名字符串
   * @author hankai
   * @since Nov 22, 2018 3:27:34 PM
   */
  String signRequestBody(String requestBody, String sk);

  /**
   * 验证请求参数是否未篡改（使用首选项中配置的传输秘钥 Preferences.getTransferKey()）。
   *
   * @param parameters 参数集合
   * @return 是否被篡改
   * @author hankai
   * @since Oct 31, 2016 10:24:55 PM
   */
  boolean verifyRequestParameters(Map<String, ?> parameters);

  /**
   * 用自定义秘钥验证请求参数是否未篡改。
   *
   * @param parameters 参数集合
   * @param sk 秘钥
   * @return 是否被篡改
   * @author hankai
   * @since Jan 26, 2018 8:44:39 PM
   */
  boolean verifyRequestParameters(Map<String, ?> parameters, String sk);

  /**
   * 用自定义秘钥验证请求参数是否未篡改。
   *
   * @param request HTTP请求
   * @return 是否被篡改
   * @author hankai
   * @since Nov 22, 2018 3:28:41 PM
   */
  boolean verifyRequestParameters(HttpServletRequest request);

  /**
   * 用自定义秘钥验证请求参数是否未篡改。
   *
   * @param request HTTP请求
   * @param sk 秘钥
   * @return 是否被篡改
   * @author hankai
   * @since Nov 22, 2018 3:28:41 PM
   */
  boolean verifyRequestParameters(HttpServletRequest request, String sk);
}
