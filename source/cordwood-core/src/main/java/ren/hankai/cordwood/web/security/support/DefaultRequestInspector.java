/**
 *
 */
package ren.hankai.cordwood.web.security.support;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.web.security.AccessAuthenticator;
import ren.hankai.cordwood.web.security.RequestInspector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 请求检查接口默认实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:30:05 PM
 *
 */
@Component
public class DefaultRequestInspector implements RequestInspector {

  private static final Logger logger = LoggerFactory.getLogger(DefaultRequestInspector.class);

  @Override
  public String signRequestParameters(Map<String, ?> parameters) {
    final StringBuffer toBeSigned = new StringBuffer();
    final List<String> paramNames = new ArrayList<>();
    paramNames.addAll(parameters.keySet());
    Collections.sort(paramNames);
    for (final String param : paramNames) {
      if (param.equalsIgnoreCase(AccessAuthenticator.ACCESS_TOKEN)
          || param.equalsIgnoreCase(RequestInspector.REQUEST_SIGN)) {
        continue;
      }
      final Object objValue = parameters.get(param);
      String value = null;
      if (objValue instanceof String[]) {
        final String[] array = (String[]) objValue;
        if (array.length > 0) {
          value = array[0];
        }
      } else {
        value = objValue.toString();
      }
      if (value != null) {
        try {
          value = URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
          logger.warn(String.format("Failed to url encode request parameter: %s = ", param, value));
        }
        toBeSigned.append(param + "=" + value + "&");
      }
    }
    if (toBeSigned.length() > 0) {
      toBeSigned.deleteCharAt(toBeSigned.length() - 1);
    }
    toBeSigned.append(Preferences.getTransferKey());
    final String expSign = DigestUtils.sha1Hex(toBeSigned.toString());
    return expSign;
  }

  @Override
  public boolean verifyRequestParameters(Map<String, ?> parameters) {
    boolean hasParams = false;
    final Iterator<String> it = parameters.keySet().iterator();
    // 检查是否有入参，若无，则不会验证
    while (it.hasNext()) {
      final String key = it.next();
      // 忽略白名单字段
      if (key.equalsIgnoreCase(AccessAuthenticator.ACCESS_TOKEN)
          || key.equalsIgnoreCase(RequestInspector.REQUEST_SIGN)) {
        continue;
      }
      hasParams = true;
      break;
    }
    if (!hasParams) {
      return true;
    }
    final String expSign = signRequestParameters(parameters);
    final Object sign = parameters.get(RequestInspector.REQUEST_SIGN);
    if ((sign instanceof String) && expSign.equalsIgnoreCase((String) sign)) {
      return true;
    }
    return false;
  }

}
