
package ren.hankai.cordwood.core.util;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 运行时变量，用于在程序运行期间读写一些配置，并可以将这些配置持久化。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 4:12:14 PM
 */
public final class RuntimeVariables {

  private static final Logger logger = LoggerFactory.getLogger(RuntimeVariables.class);
  private static Map<String, String> variables = null;
  // 运行时变量保存路径
  private static final String savePath = Preferences.getDataDir() + "/runtime.properties";
  // 缓存时长（秒）
  private static long cacheSeconds = 60 * 2;
  // 上次从文件中加载变量的时间戳
  private static long lastScanTimestamp = -1;

  private RuntimeVariables() {}

  // 获取所有运行时变量字典
  private static Map<String, String> getVariables() {
    // 如果缓存的变量过期了，则清空
    if (cacheSeconds > 0) {
      final long timestamp = System.currentTimeMillis() / 1000;
      if ((timestamp - lastScanTimestamp) > cacheSeconds) {
        variables = null;
      }
    }
    if (variables == null) {
      variables = new HashMap<>();
      try {
        final Properties props = new Properties();
        final File file = getVariablesFile();
        if (file.exists()) {
          props.load(new FileReader(file));
          final Set<String> keyset = props.stringPropertyNames();
          for (final String key : keyset) {
            variables.put(key, props.getProperty(key));
          }
        }
        lastScanTimestamp = System.currentTimeMillis() / 1000;
      } catch (final FileNotFoundException ex) {
        logger.error(String.format("Runtime variables file \"%s\" not found!", savePath), ex);
      } catch (final IOException ex) {
        logger.error(String.format("Failed to load runtime variables from file \"%s\"!", savePath),
            ex);
      }
    }
    return variables;
  }

  /**
   * 设置从文件中读取变量后，缓存的时长（秒）。
   *
   * @param seconds 缓存时长
   * @author hankai
   * @since Apr 25, 2017 5:13:45 PM
   */
  public static void setCacheSeconds(long seconds) {
    seconds = (seconds > (60 * 60 * 24)) ? 60 * 60 * 24 : seconds; // 最多缓存1天
    cacheSeconds = seconds;
  }

  /**
   * 获取保存运行时变量的文件。
   *
   * @return 变量文件
   * @author hankai
   * @since Apr 25, 2017 5:11:37 PM
   */
  public static File getVariablesFile() {
    return new File(savePath);
  }

  /**
   * 持久化运行时变量。
   *
   * @author hankai
   * @since Oct 25, 2016 10:45:14 AM
   */
  public static void saveVariables() {
    if ((variables == null) || variables.isEmpty()) {
      return;
    }
    try {
      final String header =
          "These are the runtime variables for the application, do not change it manually!";
      final Properties props = new Properties();
      props.putAll(variables);
      props.store(new FileWriterWithEncoding(savePath, "UTF-8"), header);
    } catch (final IOException ex) {
      logger.error(String.format("Failed to save runtime variables to file \"%s\"!", savePath), ex);
    }
  }

  public static void setVariable(String key, String value) {
    getVariables().put(key, value);
  }

  /**
   * 根据键获取变量值。
   *
   * @param key 变量的键
   * @return 变量值
   * @author hankai
   * @since Apr 28, 2017 7:16:18 PM
   */
  public static String getVariable(String key) {
    return getVariables().get(key);
  }

  /**
   * 获取 boolean 类型的变量。
   *
   * @param key 变量名
   * @return 变量值
   * @author hankai
   * @since Oct 25, 2016 10:45:33 AM
   */
  public static boolean getBool(String key) {
    final String value = getVariable(key);
    try {
      return Boolean.parseBoolean(value);
    } catch (final Exception ex) {
      logger.warn(String.format("Failed to get boolean variable \"%s\"", key), ex);
    }
    return false;
  }

  /**
   * 获取整型变量。
   *
   * @param key 变量名
   * @return 变量值
   * @author hankai
   * @since Oct 25, 2016 10:46:11 AM
   */
  public static int getInt(String key) {
    final String value = getVariable(key);
    try {
      return Integer.parseInt(value);
    } catch (final Exception ex) {
      logger.warn(String.format("Failed to get int variable \"%s\"", key), ex);
    }
    return 0;
  }

  /**
   * 获取所有变量，这将会返回变量集合的副本，因此修改返回值中的内容不会影响实际的变量集合。
   *
   * @return 所有变量
   * @author hankai
   * @since Oct 25, 2016 10:46:40 AM
   */
  public static Map<String, String> getAllVariables() {
    final Map<String, String> map = new HashMap<>();
    map.putAll(variables);
    return map;
  }

  /**
   * 设置所有变量。
   *
   * @param map 所有变量
   * @author hankai
   * @since Oct 25, 2016 10:46:54 AM
   */
  public static void setAllVariables(Map<String, String> map) {
    if ((map != null) && (map.size() > 0)) {
      variables = map;
    }
  }

  /**
   * 强制重新从文件中载入运行时变量。
   *
   * @author hankai
   * @since Apr 28, 2017 7:17:20 PM
   */
  public static void reloadVariables() {
    variables = null;
    getVariables();
  }

  /**
   * 删除运行时变量。
   *
   * @param key 变量名
   * @author hankai
   * @since Sep 5, 2017 5:45:44 PM
   */
  public static void removeVariable(String key) {
    variables.remove(key);
  }
}
