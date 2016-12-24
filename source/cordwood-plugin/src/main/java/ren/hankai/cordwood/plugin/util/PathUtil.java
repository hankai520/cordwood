
package ren.hankai.cordwood.plugin.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ren.hankai.cordwood.plugin.api.annotation.Pluggable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

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
   * 根据请求的路径解析出资源相对路径。
   *
   * @param url 请求路径（形如：/resources/css/style.css 或 http://www.exam.org/resources/css/style.css）
   * @return 资源相对路径
   * @author hankai
   * @since Oct 25, 2016 1:39:03 AM
   */
  public static String parseResourcePath(String url) {
    try {
      String decodedUrl = URLDecoder.decode(url, "UTF-8");

      final int index = decodedUrl.indexOf(Pluggable.PLUGIN_RESOURCE_BASE_URL);
      if (index >= 0) {
        decodedUrl = decodedUrl.substring(index + 1);
        decodedUrl = decodedUrl.startsWith("/") ? decodedUrl.substring(1) : decodedUrl;
        final String[] parts = decodedUrl.split("/");
        if ((parts != null) && (parts.length > 2)) {
          final StringBuilder sb = new StringBuilder();
          for (int i = 2; i < parts.length; i++) {
            sb.append(parts[i] + "/");
          }
          if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
          }
          return sb.toString();
        }
      }
    } catch (final UnsupportedEncodingException e) {
      logger.error(String.format("Failed to decode url: \"%s\"", url), e);
    }
    return null;
  }

  public static URL getFileUrlInPluginJar(Class<?> pluginClass, String fileRelativePath)
      throws URISyntaxException, MalformedURLException {
    final URL jarUrl = pluginClass.getProtectionDomain().getCodeSource().getLocation();
    final String jarPath = jarUrl.getPath().toLowerCase();
    URL fileUrl = null;
    if (FilenameUtils.isExtension(jarPath, "jar")) {
      String filePath = fileRelativePath;
      if (!fileRelativePath.startsWith(File.separator)) {
        filePath = File.separator + fileRelativePath;
      }
      fileUrl = new URL(String.format("jar:file:%s!%s", jarPath, filePath));
    } else {
      fileUrl = pluginClass.getClassLoader().getResource(fileRelativePath);
    }
    return fileUrl;
  }
}
