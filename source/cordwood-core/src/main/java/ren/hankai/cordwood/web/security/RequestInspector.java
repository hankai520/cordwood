/**
 *
 */
package ren.hankai.cordwood.web.security;

import java.util.Map;

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
   * 计算参数签名。
   *
   * @param parameters 参数集合
   * @return 签名字符串
   * @author hankai
   * @since Oct 31, 2016 10:22:35 PM
   */
  String signRequestParameters(Map<String, ?> parameters);

  /**
   * 验证请求参数是否未篡改。
   *
   * @param parameters 参数集合
   * @return 是否被篡改
   * @author hankai
   * @since Oct 31, 2016 10:24:55 PM
   */
  boolean verifyRequestParameters(Map<String, ?> parameters);
}
