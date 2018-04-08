
package ren.hankai.cordwood.data.jpa.logging;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Eclipselink 到 SLF4J 日志适配器。 以下是 eclipse link 和 SLF4J 日志级别的对应关系
 * <ul>
 * <li>ALL,FINER,FINEST -> TRACE
 * <li>FINE -> DEBUG
 * <li>CONFIG,INFO -> INFO
 * <li>WARNING -> WARN
 * <li>SEVERE -> ERROR
 * </ul>
 *
 * @author hankai
 * @version 1.0.0
 * @since Mar 29, 2018 8:54:47 PM
 */
public class Slf4jSessionLogger extends AbstractSessionLog {

  /**
   * SLF4J 日志级别。
   */
  private enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, OFF
  }

  public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence.logging";
  public static final String DEFAULT_CATEGORY = "default";
  public static final String DEFAULT_ECLIPSELINK_NAMESPACE =
      ECLIPSELINK_NAMESPACE + "." + DEFAULT_CATEGORY;
  private Map<Integer, LogLevel> mapLevels;
  private final Map<String, Logger> categoryLoggers = new HashMap<>();

  /**
   * 将日志器加到日志类别中。
   *
   * @param loggerCategory 日志类别
   * @param loggerNameSpace 日志命名空间
   * @author hankai
   * @since Mar 29, 2018 9:00:13 PM
   */
  private void addLogger(String loggerCategory, String loggerNameSpace) {
    categoryLoggers.put(loggerCategory, LoggerFactory.getLogger(loggerNameSpace));
  }

  /**
   * 主动初始化日志器。
   *
   * @author hankai
   * @since Mar 29, 2018 9:00:01 PM
   */
  private void createCategoryLoggers() {
    for (final String category : SessionLog.loggerCatagories) {
      addLogger(category, ECLIPSELINK_NAMESPACE + "." + category);
    }
    addLogger(DEFAULT_CATEGORY, DEFAULT_ECLIPSELINK_NAMESPACE);
  }

  /**
   * 为指定的类别生成日志器。
   *
   * @param category 类别
   * @return 日志器
   * @author hankai
   * @since Mar 29, 2018 8:59:25 PM
   */
  private Logger getLogger(String category) {
    if (StringUtils.isEmpty(category) || !categoryLoggers.containsKey(category)) {
      category = DEFAULT_CATEGORY;
    }
    return categoryLoggers.get(category);
  }

  /**
   * 为 eclipse link 的日志级别找到对应的 SLF4J 日志级别。
   *
   * @param level eclipselink 日志级别
   * @return SLF4J 日志级别
   * @author hankai
   * @since Mar 29, 2018 8:58:38 PM
   */
  private LogLevel getLogLevel(Integer level) {
    LogLevel logLevel = mapLevels.get(level);
    if (logLevel == null) {
      logLevel = LogLevel.OFF;
    }
    return logLevel;
  }

  /**
   * 构造 SLF4J 和 Eclipselink 的日志级别对应关系。
   *
   * @author hankai
   * @since Mar 29, 2018 8:58:21 PM
   */
  private void initMapLevels() {
    mapLevels = new HashMap<>();
    mapLevels.put(SessionLog.ALL, LogLevel.TRACE);
    mapLevels.put(SessionLog.FINEST, LogLevel.TRACE);
    mapLevels.put(SessionLog.FINER, LogLevel.DEBUG);
    mapLevels.put(SessionLog.FINE, LogLevel.INFO);
    mapLevels.put(SessionLog.CONFIG, LogLevel.INFO);
    mapLevels.put(SessionLog.INFO, LogLevel.INFO);
    mapLevels.put(SessionLog.WARNING, LogLevel.WARN);
    mapLevels.put(SessionLog.SEVERE, LogLevel.ERROR);
  }

  /**
   * 初始化日志器和日志级别映射。
   */
  public Slf4jSessionLogger() {
    super();
    createCategoryLoggers();
    initMapLevels();
  }

  @Override
  public void log(SessionLogEntry entry) {
    if (!shouldLog(entry.getLevel(), entry.getNameSpace())) {
      return;
    }
    final Logger logger = getLogger(entry.getNameSpace());
    final LogLevel logLevel = getLogLevel(entry.getLevel());
    final StringBuilder message = new StringBuilder();
    message.append(getSupplementDetailString(entry));
    message.append(formatMessage(entry));
    switch (logLevel) {
      case TRACE:
        logger.trace(message.toString());
        break;
      case DEBUG:
        logger.debug(message.toString());
        break;
      case INFO:
        logger.info(message.toString());
        break;
      case WARN:
        logger.warn(message.toString());
        break;
      case ERROR:
        logger.error(message.toString());
        break;
      default:
        break;
    }
  }

  @Override
  public boolean shouldDisplayData() {
    if (shouldDisplayData != null) {
      return shouldDisplayData.booleanValue();
    } else {
      return false;
    }
  }

  @Override
  public boolean shouldLog(int level) {
    return shouldLog(level, "default");
  }

  @Override
  public boolean shouldLog(int level, String category) {
    final Logger logger = getLogger(category);
    boolean resp = false;
    final LogLevel logLevel = getLogLevel(level);
    switch (logLevel) {
      case TRACE:
        resp = logger.isTraceEnabled();
        break;
      case DEBUG:
        resp = logger.isDebugEnabled();
        break;
      case INFO:
        resp = logger.isInfoEnabled();
        break;
      case WARN:
        resp = logger.isWarnEnabled();
        break;
      case ERROR:
        resp = logger.isErrorEnabled();
        break;
      default:
        break;
    }
    return resp;
  }
}
