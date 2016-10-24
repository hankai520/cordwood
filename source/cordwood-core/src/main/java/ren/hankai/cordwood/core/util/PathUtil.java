
package ren.hankai.cordwood.core.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径工具类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 24, 2016 5:30:59 PM
 */
public class PathUtil {

  private static final Logger logger = LoggerFactory.getLogger(PathUtil.class);

  /**
   * 根据请求的路径解析出资源相对路径
   *
   * @param url 请求路径
   * @return 资源相对路径
   * @author hankai
   * @since Oct 25, 2016 1:39:03 AM
   */
  public static String parseResourcePath(String url) {
    try {
      String decodedUrl = URLDecoder.decode(url, "UTF-8");
      decodedUrl = decodedUrl.startsWith("/") ? decodedUrl.substring(1) : decodedUrl;
      String[] parts = decodedUrl.split("/");
      if ((parts != null) && (parts.length > 2)) {
        List<String> list = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
          list.add(parts[i]);
        }
        return StringUtils.join(list, "/");
      }
    } catch (UnsupportedEncodingException e) {
      logger.error(String.format("Failed to decode url: \"%s\"", url), e);
    }
    return null;
  }
}
