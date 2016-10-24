
package ren.hankai.cordwood.core.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径工具类
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 24, 2016 5:30:59 PM
 */
public class PathUtil {

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
    }
    return null;
  }
}
