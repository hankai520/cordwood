
package ren.hankai.cordwood.core.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.logging.LogbackLevelFilter;

import java.io.File;
import java.util.List;



/**
 * Logback 日志助手类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 10, 2016 2:22:42 PM
 */
public final class LogbackUtil {

  /**
   * 获取一个控制台日志追加器。
   *
   * @param lc 日志上下文
   * @return 追加器
   * @author hankai
   * @since Oct 13, 2016 10:20:37 AM
   */
  private static ConsoleAppender<ILoggingEvent> getConsoleAppender(LoggerContext lc,
      String pattern) {
    final ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<>();
    ca.setContext(lc);
    ca.setName("console");
    final LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
    encoder.setContext(lc);
    final PatternLayout layout = new PatternLayout();
    layout.setContext(lc);
    if (StringUtils.isEmpty(pattern)) {
      pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    }
    layout.setPattern(pattern);
    layout.start();
    encoder.setLayout(layout);
    ca.setEncoder(encoder);
    ca.start();
    return ca;
  }

  /**
   * 获取一个可自动归档的文件日志追加器。
   *
   * @param loggerName 日志器名（即要记录日志的类名）
   * @param lc 日志上下文
   * @param fileName 日志物理文件名
   * @param pattern 日志格式
   * @param maxHistory 最多保留多少份归档的日志
   * @return 日志追加器
   * @author hankai
   * @since Oct 13, 2016 10:21:07 AM
   */
  private static RollingFileAppender<ILoggingEvent> getFileAppender(String loggerName,
      LoggerContext lc, String fileName, String pattern, int maxHistory) {
    final String logDir = Preferences.getLogDir();
    final RollingFileAppender<ILoggingEvent> rfa = new RollingFileAppender<>();
    rfa.setContext(lc);
    rfa.setName(loggerName + "-file");
    rfa.setFile(logDir + File.separator + fileName);
    final TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
    final String namePattern = String.format("%s/%s.%%d{yyyy-MM-dd}.zip", logDir, fileName);
    policy.setFileNamePattern(namePattern);
    policy.setMaxHistory(maxHistory);
    policy.setContext(lc);
    policy.setParent(rfa);
    policy.start();
    rfa.setRollingPolicy(policy);
    final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setImmediateFlush(true);
    encoder.setContext(lc);
    if (StringUtils.isEmpty(pattern)) {
      pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    }
    encoder.setPattern(pattern);
    encoder.start();
    rfa.setEncoder(encoder);
    rfa.start();
    return rfa;
  }

  /**
   * 为指定的类或包添加控制台日志器。
   *
   * @param name 类或包名
   * @param level 保留的最低日志级别
   * @author hankai
   * @since Oct 13, 2016 10:22:28 AM
   */
  public static void setupConsoleLoggerFor(String name, Level level) {
    setupConsoleLoggerFor(name, level, null);
  }

  /**
   * 为指定的类或包添加控制台日志器。
   *
   * @param name 类或包名
   * @param level 保留的最低日志级别
   * @param pattern 日志格式
   * @author hankai
   * @since Oct 29, 2018 11:53:27 AM
   */
  public static void setupConsoleLoggerFor(String name, Level level, String pattern) {
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    if (StringUtils.isEmpty(name)) {
      name = Logger.ROOT_LOGGER_NAME;
    }
    final Logger logger = lc.getLogger(name);
    final ConsoleAppender<ILoggingEvent> appender = getConsoleAppender(lc, pattern);
    final LogbackLevelFilter filter = new LogbackLevelFilter(level);
    appender.addFilter(filter);
    filter.start();
    logger.addAppender(appender);
  }

  /**
   * 为所有的类或包添加控制台日志器。
   *
   * @param level 保留的最低日志级别
   * @author hankai
   * @since Apr 28, 2018 1:48:47 PM
   */
  public static void setupConsoleLoggerFor(Level level) {
    setupConsoleLoggerFor(level, null);
  }

  /**
   * 为所有的类或包添加控制台日志器。
   *
   * @param level 保留的最低日志级别
   * @param pattern 日志格式
   * @author hankai
   * @since Oct 29, 2018 11:54:38 AM
   */
  public static void setupConsoleLoggerFor(Level level, String pattern) {
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    if (null != loggers) {
      for (final Logger logger : loggers) {
        setupConsoleLoggerFor(logger.getName(), level, pattern);
      }
    }
  }

  /**
   * 为指定的类或包添加文件日志器（默认保留最多7份归档的日志）。
   *
   * @param name 类或包名（传入空，将使用 ROOT）
   * @param level 保留的最低日志级别
   * @param logFileName 日志物理文件名
   * @author hankai
   * @since Oct 13, 2016 10:23:04 AM
   */
  public static void setupFileLoggerFor(String name, Level level, String logFileName) {
    setupFileLoggerFor(name, level, logFileName, null, 7);
  }

  /**
   * 为指定的类或包添加文件日志器。
   *
   * @param name 类或包名（传入空，将使用 ROOT）
   * @param level 保留的最低日志级别
   * @param logFileName 日志物理文件名
   * @param pattern 日志格式
   * @param maxHistory 最多保留几份归档的日志
   * @author hankai
   * @since Oct 29, 2018 11:56:16 AM
   */
  public static void setupFileLoggerFor(String name, Level level, String logFileName,
      String pattern, int maxHistory) {
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    if (StringUtils.isEmpty(name)) {
      name = Logger.ROOT_LOGGER_NAME;
    }
    final Logger logger = lc.getLogger(name);
    final Appender<ILoggingEvent> appender =
        getFileAppender(name, lc, logFileName, pattern, maxHistory);
    final LogbackLevelFilter filter = new LogbackLevelFilter(level);
    appender.addFilter(filter);
    filter.start();
    logger.addAppender(appender);
  }

  /**
   * 为所有的类或包添加文件日志器（默认最多保留7份归档的日志）。
   *
   * @param level 保留的最低日志级别
   * @param logFileName 日志物理文件名
   * @author hankai
   * @since Apr 28, 2018 1:45:43 PM
   */
  public static void setupFileLoggerFor(Level level, String logFileName) {
    setupFileLoggerFor(level, logFileName, null, 7);
  }

  /**
   * 为所有的类或包添加文件日志器。
   *
   * @param level 保留的最低日志级别
   * @param logFileName 日志物理文件名
   * @param pattern 日志格式
   * @param maxHistory 最多保留几份归档的日志
   * @author hankai
   * @since Oct 29, 2018 11:57:19 AM
   */
  public static void setupFileLoggerFor(Level level, String logFileName, String pattern,
      int maxHistory) {
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    if (null != loggers) {
      for (final Logger logger : loggers) {
        setupFileLoggerFor(logger.getName(), level, logFileName, pattern, maxHistory);
      }
    }
  }
}
