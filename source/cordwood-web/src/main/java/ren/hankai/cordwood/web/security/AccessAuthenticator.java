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

package ren.hankai.cordwood.web.security;

/**
 * 访问认证接口，用于验证客户端是否有权访问服务。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:14:12 PM
 *
 */
public interface AccessAuthenticator {

  /**
   * 根据鉴权信息生成授权令牌。
   *
   * @param tokenInfo 鉴权信息
   * @return 授权令牌字符串。
   * @author hankai
   * @since Oct 31, 2016 10:17:06 PM
   */
  String generateAccessToken(TokenInfo tokenInfo);

  /**
   * 根据鉴权信息和秘钥生成授权令牌。
   *
   * @param tokenInfo 鉴权信息
   * @param secretKey 秘钥
   * @return 授权令牌字符串
   * @author hankai
   * @since Nov 8, 2017 9:29:57 AM
   */
  String generateAccessToken(TokenInfo tokenInfo, String secretKey);

  /**
   * 解析授权令牌。
   *
   * @param tokenString 授权令牌字符串
   * @return 鉴权信息
   * @author hankai
   * @since Oct 31, 2016 10:17:42 PM
   */
  TokenInfo parseAccessToken(String tokenString);

  /**
   * 解析授权令牌。
   *
   * @param tokenString 授权令牌字符串
   * @param secretKey 秘钥
   * @return 鉴权信息
   * @author hankai
   * @since Nov 8, 2017 9:30:57 AM
   */
  TokenInfo parseAccessToken(String tokenString, String secretKey);

  /**
   * 验证授权令牌是否有效。
   *
   * @param tokenString 授权令牌字符串。
   * @return 验证结果，参考 TokenInfo 中的错误信息定义，返回0表示有效。
   * @author hankai
   * @since Oct 31, 2016 10:18:03 PM
   */
  int verifyAccessToken(String tokenString);

  /**
   * 验证授权令牌是否有效。
   *
   * @param tokenString 授权令牌字符串。
   * @param secretKey 秘钥
   * @return 验证结果，参考 TokenInfo 中的错误信息定义，返回0表示有效。
   * @author hankai
   * @since Nov 8, 2017 9:31:08 AM
   */
  int verifyAccessToken(String tokenString, String secretKey);
}