
package ren.hankai.cordwood.oauth2.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OAuth2客户端信息，主要用于调用本模块API。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 19, 2019 11:33:16 AM
 */
public final class Oauth2Client {

  private String id;// 客户端ID

  private String secret;// 客户端秘钥

  private String scopes;// 客户端授权范围

  // 客户端支持的授权类型，一般在初始化时调用，不需要考虑线程同步
  private final List<GrantType> grantTypes = new ArrayList<>();

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
  public GrantType[] getGrantTypes() {
    // 深复制，避免修改
    final GrantType[] types = new GrantType[this.grantTypes.size()];
    return this.grantTypes.toArray(types);
  }

  /**
   * 获取授权类型。
   *
   * @return 返回授权类型字符串数组
   */
  public String[] getGrantTypesString() {
    final String[] types = new String[this.grantTypes.size()];
    for (int i = 0; i < types.length; i++) {
      types[i] = this.grantTypes.get(i).value();
    }
    return types;
  }

  /**
   * 设置 grantTypes 字段的值。
   *
   * @param grantTypes grantTypes 字段的值
   */
  public void setGrantTypes(final GrantType[] grantTypes) {
    this.grantTypes.clear();
    this.grantTypes.addAll(Arrays.asList(grantTypes));
  }

  public void addGrantType(final GrantType grantType) {
    this.grantTypes.add(grantType);
  }

  public void removeGrantType(final GrantType grantType) {
    this.grantTypes.remove(grantType);
  }

}
