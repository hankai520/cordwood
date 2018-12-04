package ren.hankai.cordwood.mobile;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 扫描移动应用包元数据。
 *
 * @author hankai
 * @version 1.0
 * @since May 16, 2016 11:44:24 AM
 */
public class MobileAppScanner {

  private static final Logger logger = LoggerFactory.getLogger(MobileAppScanner.class);

  /**
   * 扫描 Android .apk 文件元数据。
   *
   * @param filePath apk 文件路径
   * @return 文件元数据
   * @author hankai
   * @since May 20, 2016 3:46:18 PM
   */
  public static MobileAppInfo scanAndroidApk(String filePath) {
    final MobileAppInfo appInfo = new MobileAppInfo();
    ApkParser parser = null;
    ZipFile zipFile = null;
    InputStream inputStream = null;
    try {
      parser = new ApkParser(filePath);
      final ApkMeta meta = parser.getApkMeta();
      appInfo.setName(meta.getName());
      appInfo.setVersion(meta.getVersionName() + "#" + meta.getVersionCode());
      appInfo.setBundleId(meta.getPackageName());
      final String iconPath = meta.getIcon();
      appInfo.setIconName(iconPath);
      if (!StringUtils.isEmpty(iconPath)) {
        zipFile = new ZipFile(filePath, Charset.forName("UTF-8"));
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
          final ZipEntry entry = entries.nextElement();
          if (entry.getName().contains(iconPath)) {
            inputStream = zipFile.getInputStream(entry);
            appInfo.setIcon(IOUtils.toByteArray(inputStream));
            break;
          }
        }
      }
    } catch (final Exception ex) {
      logger.error(String.format("Failed to parse apk file at %s", filePath));
    } finally {
      try {
        if (parser != null) {
          parser.close();
        }
        if (zipFile != null) {
          zipFile.close();
        }
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (final Exception ex) {
        logger.trace("Failed to close stream.", ex);
      }
    }
    return appInfo;
  }

  /**
   * 解析 ipa 文件中的 info.plist。
   *
   * @param appInfo app信息
   * @param entry ipa 压缩包中的条目
   * @param zipFile 压缩包文件
   * @author hankai
   * @since Nov 23, 2016 2:32:08 PM
   */
  private static void parseIosInfoPlist(MobileAppInfo appInfo, ZipEntry entry, ZipFile zipFile) {
    if (entry.getName().matches("Payload/.*\\.app/Info.plist")) {
      InputStream stream = null;
      try {
        stream = zipFile.getInputStream(entry);
        final NSDictionary dict = (NSDictionary) PropertyListParser.parse(stream);
        appInfo.setName(dict.get("CFBundleName").toString());
        appInfo.setVersion(dict.get("CFBundleShortVersionString").toString() + "#"
            + dict.get("CFBundleVersion").toString());
        appInfo.setBundleId(dict.get("CFBundleIdentifier").toString());
        final NSDictionary icons = (NSDictionary) dict.get("CFBundleIcons");
        final NSDictionary primaryIcons = (NSDictionary) icons.get("CFBundlePrimaryIcon");
        final NSArray iconFiles = (NSArray) primaryIcons.get("CFBundleIconFiles");
        if (iconFiles.count() > 0) {
          appInfo.setIconName(iconFiles.lastObject().toString());
        }
      } catch (final Exception ex) {
        logger.error(String.format("Failed to parse entry %s", entry.getName()));
      } finally {
        IOUtils.closeQuietly(stream);
      }
    }
  }

  /**
   * 解析 ipa 包中的应用图标。
   *
   * @param appInfo 应用信息
   * @param zipFile 压缩包文件
   * @param images 候选图标
   * @author hankai
   * @since Nov 23, 2016 2:32:45 PM
   */
  private static void parseIosAppIcon(MobileAppInfo appInfo, ZipFile zipFile,
      Map<String, ZipEntry> images) {
    if (!StringUtils.isEmpty(appInfo.getIconName())) {
      InputStream stream = null;
      for (final String key : images.keySet()) {
        if (key.contains(appInfo.getIconName())) {
          try {
            stream = zipFile.getInputStream(images.get(key));
            appInfo.setIcon(IOUtils.toByteArray(stream));
          } catch (final Exception ex) {
            logger.error("Failed to read app icon from package.", ex);
          } finally {
            IOUtils.closeQuietly(stream);
          }
          break;
        }
      }
    }
  }

  /**
   * 扫描 iOS .ipa 文件元数据。
   *
   * @param filePath ipa 文件路径
   * @return 文件元数据
   * @author hankai
   * @since May 20, 2016 3:46:18 PM
   */
  public static MobileAppInfo scanIosIpa(String filePath) {
    final MobileAppInfo appInfo = new MobileAppInfo();
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(filePath, Charset.forName("UTF-8"));
      final Enumeration<? extends ZipEntry> entries = zipFile.entries();
      final Map<String, ZipEntry> images = new HashMap<>();
      while (entries.hasMoreElements()) {
        final ZipEntry entry = entries.nextElement();
        parseIosInfoPlist(appInfo, entry, zipFile);
        if (entry.getName().toLowerCase().matches("payload/.*\\.app/.*\\.png")) {
          images.put(entry.getName(), entry);
        }
      }
      parseIosAppIcon(appInfo, zipFile, images);
    } catch (final Exception ex) {
      logger.error(String.format("Failed to parse ipa file at %s", filePath));
    } finally {
      try {
        if (zipFile != null) {
          zipFile.close();
        }
      } catch (final Exception ex) {
        logger.trace("Failed to close stream.", ex);
      }
    }
    return appInfo;
  }
}
