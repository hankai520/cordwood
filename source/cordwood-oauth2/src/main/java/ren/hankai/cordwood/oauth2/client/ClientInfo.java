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

package ren.hankai.cordwood.oauth2.client;

/**
 * OAuth2客户端信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 19, 2019 11:33:16 AM
 */
public final class ClientInfo {

  // 客户端ID
  private String id;

  // 客户端秘钥
  private String secret;

  // 客户端授权范围
  private String scopes;

  // 客户端支持的授权类型
  private String[] grantTypes;

  /**
   * 获取 id 字段的值。
   *
   * @return id 字段值
   */
  public String getId() {
    return id;
  }

  /**
   * 设置 id 字段的值。
   *
   * @param id id 字段的值
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * 获取 secret 字段的值。
   *
   * @return secret 字段值
   */
  public String getSecret() {
    return secret;
  }

  /**
   * 设置 secret 字段的值。
   *
   * @param secret secret 字段的值
   */
  public void setSecret(final String secret) {
    this.secret = secret;
  }

  /**
   * 获取 scopes 字段的值。
   *
   * @return scopes 字段值
   */
  public String getScopes() {
    return scopes;
  }

  /**
   * 设置 scopes 字段的值。
   *
   * @param scopes scopes 字段的值
   */
  public void setScopes(final String scopes) {
    this.scopes = scopes;
  }

  /**
   * 获取 grantTypes 字段的值。
   *
   * @return grantTypes 字段值
   */
  public String[] getGrantTypes() {
    return grantTypes;
  }

  /**
   * 设置 grantTypes 字段的值。
   *
   * @param grantTypes grantTypes 字段的值
   */
  public void setGrantTypes(final String[] grantTypes) {
    this.grantTypes = grantTypes;
  }

}
