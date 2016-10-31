
package ren.hankai.cordwood.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

  private RuntimeVariables() {}

  private static Map<String, String> getVariables() {
    if (variables == null) {
      variables = new HashMap<>();
      try {
        final DefaultPropertiesPersister dpp = new DefaultPropertiesPersister();
        final Properties props = new Properties();
        dpp.load(props, new FileReader(getVariablesFile()));
        final Set<String> keyset = props.stringPropertyNames();
        for (final String key : keyset) {
          variables.put(key, props.getProperty(key));
        }
      } catch (final FileNotFoundException e) {
        logger.error(String.format("Runtime variables file \"%s\" not found!", savePath), e);
      } catch (final IOException e) {
        logger.error(String.format("Failed to load runtime variables from file \"%s\"!", savePath),
            e);
      }
    }
    return variables;
  }

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
    try {
      final DefaultPropertiesPersister dpp = new DefaultPropertiesPersister();
      final String header =
          "These are the runtime variables for the application, do not change it manually!";
      final Properties props = new Properties();
      props.putAll(variables);
      dpp.store(props, new FileWriter(savePath), header);
    } catch (final IOException e) {
      logger.error(String.format("Failed to save runtime variables to file \"%s\"!", savePath), e);
    }
  }

  public static void setVariable(String key, String value) {
    getVariables().put(key, value);
  }

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
    } catch (final Exception e) {
      logger.warn(String.format("Failed to get boolean variable \"%s\"", key), e);
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
    } catch (final Exception e) {
      logger.warn(String.format("Failed to get int variable \"%s\"", key), e);
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

  public static void reloadVariables() {
    variables = null;
    getVariables();
  }
}
