
package ren.hankai.cordwood.core;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 程序只读配置项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 12:54:36 PM
 */
public final class Preferences {

  /**
   * 测试 运行时配置，启用后所有添加了 @profile(Bootstrap.PROFILE_TEST) 标记的配置将被加载。
   */
  public static final String PROFILE_TEST = "test";

  /**
   * 调试运行时配置，启用后所有添加了 @profile(Bootstrap.PROFILE_PRODUCTION) 标记的配置将被加载。
   */
  public static final String PROFILE_PRODUCTION = "prod";

  /**
   * 启用 HSQL 数据库。
   */
  public static final String PROFILE_HSQL = "hsql";

  /**
   * 启用 MySQL 数据库。
   */
  public static final String PROFILE_MYSQL = "mysql";

  /**
   * .启用 Oracle 数据库。
   */
  public static final String PROFILE_ORACLE = "oracle";

  /**
   * 命令行参数：程序数据根目录。
   */
  public static final String ENV_APP_HOME_DIR = "app.home";

  /**
   * 配置：程序内部或数据库采用的数据分隔符。例如：字符串hello,apple,etc就是采用了分隔符将子串连接为 一个字符串。
   */
  public static final String DATA_SEPARATOR = ",";

  /**
   * 插件资源根路径。
   */
  public static final String PLUGIN_RESOURCE_BASE = "resources";

  // 程序默认数据根目录（此默认名称用于提示开发者环境变量缺失）
  private static String appHome = null;

  // 系统运行参数
  private static Map<String, Object> parameters = null;

  private Preferences() {}

  /**
   * 从外部配置文件加载系统参数配置。
   *
   * @author hankai
   * @since Jun 27, 2016 10:09:35 PM
   */
  private static Map<String, Object> getSystemPrefs() {
    if (parameters == null) {
      parameters = new HashMap<>();
      final YamlMapFactoryBean bean = new YamlMapFactoryBean();
      bean.setResources(new FileSystemResource(Preferences.getConfigDir() + "/system.yml"));
      parameters.putAll(bean.getObject());
    }
    return parameters;
  }

  /**
   * 获取程序数据根目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:51:49 AM
   */
  public static String getHomeDir() {
    if (appHome == null) {
      // 优先检查 JVM 环境变量
      appHome = System.getProperty(ENV_APP_HOME_DIR);
      if (StringUtils.isEmpty(appHome)) {
        // 检查系统环境变量
        appHome = System.getenv(ENV_APP_HOME_DIR);
      }
      // 去掉尾部的路径分隔符
      if ((appHome != null) && appHome.endsWith(File.separator)) {
        appHome = appHome.substring(0, appHome.length());
      }
      if (StringUtils.isEmpty(appHome)) {
        // 如果没有检测到任何环境变量设置了home目录，则使用默认目录。取名 home-not-set 是为了提示用户环境变量没有正确设置
        appHome = System.getProperty("user.dir") + File.separator + "home-not-set";
      }
      System.setProperty(ENV_APP_HOME_DIR, appHome);
    }
    return appHome;
  }

  /**
   * 获取程序缓存目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:52:19 AM
   */
  public static String getCacheDir() {
    final String dir = getHomeDir() + File.separator + "cache";
    System.setProperty("app.cache", dir);
    return dir;
  }

  /**
   * 获取程序外部配置文件存储目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:52:44 AM
   */
  public static String getConfigDir() {
    final String dir = getHomeDir() + File.separator + "config";
    System.setProperty("app.config", dir);
    return dir;
  }

  /**
   * 获取配置文件路径。
   *
   * @param configFile 配置文件名
   * @return 文件路径
   * @author hankai
   * @since Oct 21, 2016 1:13:13 PM
   */
  public static String getConfigFilePath(String configFile) {
    return getConfigDir() + File.separator + configFile;
  }

  /**
   * 获取程序数据存储目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:53:05 AM
   */
  public static String getDataDir() {
    final String dir = getHomeDir() + File.separator + "data";
    System.setProperty("app.data", dir);
    return dir;
  }

  /**
   * 数据库存储目录。
   *
   * @return 数据库目录
   * @author hankai
   * @since Sep 30, 2016 3:39:19 PM
   */
  public static String getDbDir() {
    return getHomeDir() + File.separator + "data" + File.separator + "database";
  }

  /**
   * 获取默认的数据库配置文件路径。
   *
   * @return 配置文件路径
   * @author hankai
   * @since Jun 21, 2016 11:21:50 AM
   */
  public static String getDbConfigFile() {
    return getDbConfigFile(null);
  }

  /**
   * 获取指定数据库配置文件路径。
   *
   * @param fileName 数据库配置文件名
   * @return 配置文件路径
   * @author hankai
   * @since Jun 21, 2016 11:21:15 AM
   */
  public static String getDbConfigFile(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      fileName = "database.properties";
    }
    return getConfigDir() + File.separator + fileName;
  }

  /**
   * 获取程序日志目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:53:49 AM
   */
  public static String getLogDir() {
    final String dir = getHomeDir() + File.separator + "logs";
    System.setProperty("app.log", dir);
    return dir;
  }

  /**
   * 获取程序临时数据目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:54:02 AM
   */
  public static String getTempDir() {
    final String dir = getHomeDir() + File.separator + "temp";
    System.setProperty("app.temp", dir);
    return dir;
  }

  /**
   * 获取程序附件存储目录。
   *
   * @author hankai
   * @since Jul 28, 2015 10:54:16 AM
   */
  public static String getAttachmentDir() {
    final String dir = getDataDir() + File.separator + "attachment";
    System.setProperty("app.attachment", dir);
    return dir;
  }

  /**
   * 获取程序数据备份目录。
   *
   * @return 备份目录
   * @author hankai
   * @since Aug 18, 2016 5:09:38 PM
   */
  public static String getBackupDir() {
    final String dir = getDataDir() + File.separator + "backups";
    System.setProperty("app.backup", dir);
    return dir;
  }

  /**
   * 插件目录。
   *
   * @return 插件目录
   * @author hankai
   * @since Sep 30, 2016 3:37:19 PM
   */
  public static String getPluginsDir() {
    final String dir = getDataDir() + File.separator + "plugins";
    System.setProperty("app.plugins", dir);
    return dir;
  }

  /**
   * 依赖包目录。
   *
   * @return 依赖包目录
   * @author hankai
   * @since Oct 18, 2016 2:45:39 PM
   */
  public static String getLibsDir() {
    final String dir = getHomeDir() + File.separator + "libs";
    System.setProperty("app.libs", dir);
    return dir;
  }

  /**
   * 获取插件依赖的第三方包。
   *
   * @param extraUrls 额外补充的包地址，这些 URL 将会和依赖包的 URL 合并到一起返回
   * @return 依赖包 URL 集合
   * @author hankai
   * @since Oct 18, 2016 3:09:42 PM
   */
  public static URL[] getLibUrls(URL... extraUrls) {
    final List<URL> list = new ArrayList<>();
    if ((extraUrls != null) && (extraUrls.length > 0)) {
      list.addAll(Arrays.asList(extraUrls));
    }
    final File file = new File(getLibsDir());
    final File[] files = file.listFiles();
    if (files != null) {
      for (final File libFile : files) {
        if (FilenameUtils.isExtension(libFile.getName(), "jar")) {
          try {
            list.add(libFile.toURI().toURL());
          } catch (final MalformedURLException e) {
            throw new RuntimeException(
                String.format("Failed to get url from lib path: %s", libFile.getAbsolutePath()), e);
          }
        }
      }
    }
    if (list.size() > 0) {
      final URL[] urls = new URL[list.size()];
      list.toArray(urls);
      return urls;
    }
    return null;
  }

  /**
   * 获取系统秘钥（一般用于加密传输）。
   *
   * @return 秘钥字串
   * @author hankai
   * @since Jun 28, 2016 1:27:31 PM
   */
  public static String getSystemSk() {
    final Object obj = getSystemPrefs().get("systemSk");
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  /**
   * 获取数据传输秘钥。
   *
   * @return 用于数据传输完整性验证的秘钥
   * @author hankai
   * @since Jun 28, 2016 1:49:49 PM
   */
  public static String getTransferKey() {
    final Object obj = getSystemPrefs().get("transferKey");
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  /**
   * 获取 API 鉴权码有效时长（天）。
   *
   * @return 有效时长
   * @author hankai
   * @since Jun 29, 2016 9:48:43 PM
   */
  public static Integer getApiAccessTokenExpiry() {
    final Object obj = getSystemPrefs().get("apiAccessTokenExpiry");
    if (obj != null) {
      return Integer.parseInt(obj.toString());
    }
    return null;
  }
}
