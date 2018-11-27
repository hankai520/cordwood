package ren.hankai.cordwood.web.security.support;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.web.security.AccessAuthenticator;
import ren.hankai.cordwood.web.security.RequestInspector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
  public String buildSignText(Map<String, ?> parameters, String sk) {
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
        if (array.length == 1) {
          value = array[0];
        } else if (array.length > 1) {
          value = Arrays.toString(array);
        }
      } else {
        value = objValue.toString();
      }
      if (value != null) {
        try {
          value = URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException ex) {
          logger.warn(String.format("Failed to url encode request parameter: %s = ", param, value));
        }
        toBeSigned.append(param + "=" + value + "&");
      }
    }
    if (toBeSigned.length() > 0) {
      toBeSigned.deleteCharAt(toBeSigned.length() - 1);
    }
    toBeSigned.append(sk);
    return toBeSigned.toString();
  }

  @Override
  public String signRequestParameters(Map<String, ?> parameters) {
    return signRequestParameters(parameters, Preferences.getTransferKey());
  }

  @Override
  public String signRequestParameters(Map<String, ?> parameters, String sk) {
    final String toBeSigned = buildSignText(parameters, sk);
    final String expSign = DigestUtils.sha1Hex(toBeSigned);
    return expSign;
  }

  @Override
  public String signRequestBody(String requestBody) {
    return signRequestBody(requestBody, Preferences.getTransferKey());
  }

  @Override
  public String signRequestBody(String requestBody, String sk) {
    final String toBeSigned = requestBody + sk;
    final String expSign = DigestUtils.sha1Hex(toBeSigned);
    return expSign;
  }

  @Override
  public boolean verifyRequestParameters(Map<String, ?> parameters) {
    return verifyRequestParameters(parameters, Preferences.getTransferKey());
  }

  @Override
  public boolean verifyRequestParameters(Map<String, ?> parameters, String sk) {
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
    final String expSign = signRequestParameters(parameters, sk);
    final Object sign = parameters.get(RequestInspector.REQUEST_SIGN);
    if (sign != null) {
      if ((sign instanceof String) && expSign.equalsIgnoreCase((String) sign)) {
        return true;
      } else if (sign instanceof String[]) {
        final String[] strArr = (String[]) sign;
        if ((strArr.length > 0) && expSign.equalsIgnoreCase(strArr[0])) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean verifyRequestParameters(HttpServletRequest request) {
    return verifyRequestParameters(request, Preferences.getTransferKey());
  }

  @Override
  public boolean verifyRequestParameters(HttpServletRequest request, String sk) {
    final Map<String, String[]> params = request.getParameterMap();
    // 检查请求是否是 form
    final MediaType contentType = MediaType.valueOf(request.getContentType());
    if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)
        || MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
      // 请求是一个标准的URL编码表单
      return verifyRequestParameters(params, sk);
    }
    // 非URL编码表单，对整个请求体进行验签
    String requestBody = null;
    try {
      requestBody = IOUtils.toString(request.getInputStream());
    } catch (final IOException ex) {
      logger.warn("Failed to verify request parameters due to io error while parsing request body.",
          ex);
      return false;
    }
    final String expSign = signRequestBody(requestBody, sk);
    final Object sign = params.get(RequestInspector.REQUEST_SIGN);
    if (sign != null) {
      if ((sign instanceof String) && expSign.equalsIgnoreCase((String) sign)) {
        return true;
      } else if (sign instanceof String[]) {
        final String[] strArr = (String[]) sign;
        if ((strArr.length > 0) && expSign.equalsIgnoreCase(strArr[0])) {
          return true;
        }
      }
    }
    return false;
  }
}
