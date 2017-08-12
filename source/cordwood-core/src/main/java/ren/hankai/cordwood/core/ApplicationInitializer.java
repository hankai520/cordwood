
package ren.hankai.cordwood.core;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 程序初始化类，用于自检，修复错误，初始化依赖项等。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 12:51:54 PM
 */
public class ApplicationInitializer {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

  /**
   * 程序初始化。
   *
   * @param supportFileNames 主程序位于 support 目录的预定义配置文件名称列表
   * @return 是否成功
   * @author hankai
   * @since Jun 21, 2016 12:52:30 PM
   */
  public static boolean initialize(String... supportFileNames) {
    printClassPaths();
    boolean success = false;
    logger.info("Initializing application ...");
    success = checkHome();
    if (success) {
      success = checkConfigurations(supportFileNames);
    }
    if (success) {
      logger.info("Application initialized successfully.");
    } else {
      logger.error("Application initialization failed.");
    }
    return success;
  }

  /**
   * 打印类路径信息。
   *
   * @author hankai
   * @since Oct 13, 2016 9:52:48 AM
   */
  private static void printClassPaths() {
    logger.info("Class paths:");
    final URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
    final URL[] urls = cl.getURLs();
    for (final URL url : urls) {
      logger.info(url.getPath());
    }
  }

  /**
   * 检查数据根目录下的配置文件，如果有丢失，则复制默认配置文件。注意：该操作不是基于事务的，因此在遇到失败时，可能出现部分文件复制成功。 该方法不会覆盖用户指定的配置目录下已存在的配置文件。
   *
   * @param fileNames 配置文件名称列表
   * @return 是否正常
   * @author hankai
   * @since Jun 21, 2016 12:52:59 PM
   */
  private static boolean checkConfigurations(String... fileNames) {
    if (fileNames == null) {
      return true;
    }
    InputStream in = null;
    OutputStream out = null;
    try {
      for (final String fileName : fileNames) {
        in = ApplicationInitializer.class.getResourceAsStream("/support/" + fileName);
        if (in != null) {
          final File destFile = new File(Preferences.getConfigDir() + File.separator + fileName);
          if (!destFile.exists()) {
            out = new FileOutputStream(destFile);
            FileCopyUtils.copy(in, out);
            logger.info(String.format("Copied support file: %s", fileName));
          }
        } else {
          logger.warn(String.format("Missing support file: %s", fileName));
        }
      }
      return true;
    } catch (final IOException ex) {
      logger.error("Error occurred while copying support files.", ex);
      return false;
    } finally {
      if (out != null) {
        IOUtils.closeQuietly(out);
      }
      if (in != null) {
        IOUtils.closeQuietly(in);
      }
    }
  }

  /**
   * 创建或修复程序数据根目录结构。
   *
   * @return 是否正常
   * @author hankai
   * @since Jun 21, 2016 12:53:23 PM
   */
  private static boolean checkHome() {
    logger.info(String.format("Application home is: \"%s\"", Preferences.getHomeDir()));
    final String[] subDirs = {Preferences.getConfigDir(), Preferences.getDataDir(),
        Preferences.getCacheDir(), Preferences.getLogDir(), Preferences.getTempDir(),
        Preferences.getAttachmentDir(), Preferences.getBackupDir(), Preferences.getDbDir(),
        Preferences.getPluginsDir(), Preferences.getLibsDir(), Preferences.getTemplatesDir()};
    for (final String dir : subDirs) {
      final File file = new File(dir);
      if (!file.exists()) {
        logger.info(String.format("Creating \"%s\"...", dir));
        if (!file.mkdirs()) {
          logger.error(String.format("Failed to create \"%s\" ...", dir));
          return false;
        }
      }
    }
    logger.info("Application home directory is fine.");
    return true;
  }
}
