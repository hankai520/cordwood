/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

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
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.logging.LogbackLevelFilter;
import ren.hankai.cordwood.core.test.CoreTestSupport;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Logback 工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:58:53 PM
 */
public class LogbackUtilTest extends CoreTestSupport {

  @Test
  public void testSetupConsoleLoggerForStringLevel() {
    final String name = "testSetupConsoleLoggerForStringLevel";
    LogbackUtil.setupConsoleLoggerFor(name, Level.WARN);
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final Logger logger = lc.getLogger(name);
    Assert.assertNotNull(logger);
    Assert.assertEquals(Level.WARN, logger.getLevel());
  }

  @Test
  public void testSetupConsoleLoggerForStringLevelString() {
    final String name = "testSetupConsoleLoggerForStringLevelString";
    final String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{12} - %msg%n";
    LogbackUtil.setupConsoleLoggerFor(name, Level.ERROR, pattern);
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final Logger logger = lc.getLogger(name);
    Assert.assertNotNull(logger);
    Assert.assertEquals(Level.ERROR, logger.getLevel());
    final ConsoleAppender<ILoggingEvent> appender =
        (ConsoleAppender<ILoggingEvent>) logger.getAppender("console");
    final LayoutWrappingEncoder<ILoggingEvent> encoder =
        (LayoutWrappingEncoder<ILoggingEvent>) appender.getEncoder();
    final PatternLayout layout = (PatternLayout) encoder.getLayout();
    Assert.assertEquals(pattern, layout.getPattern());
  }

  @Test
  public void testSetupConsoleLoggerForLevel() {
    LogbackUtil.setupConsoleLoggerFor(Level.INFO);
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    if (null != loggers) {
      for (final Logger logger : loggers) {
        Assert.assertEquals(Level.INFO, logger.getLevel());
        final Appender<ILoggingEvent> appender = logger.getAppender("console");
        final List<Filter<ILoggingEvent>> filters = appender.getCopyOfAttachedFiltersList();
        if ((null != filters) && (filters.size() > 0)) {
          LogbackLevelFilter expFilter = null;
          for (final Filter<ILoggingEvent> filter : filters) {
            if (filter instanceof LogbackLevelFilter) {
              expFilter = (LogbackLevelFilter) filter;
              break;
            }
          }
          Assert.assertNotNull(expFilter);
          Assert.assertTrue(expFilter.isStarted());
          Assert.assertEquals(Level.INFO, expFilter.getLevelToKeep());
        }
      }
    }
  }

  @Test
  public void testSetupConsoleLoggerForLevelString() {
    final String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} - %msg%n";
    LogbackUtil.setupConsoleLoggerFor(Level.DEBUG, pattern);

    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    if (null != loggers) {
      for (final Logger logger : loggers) {
        Assert.assertEquals(Level.DEBUG, logger.getLevel());
        final ConsoleAppender<ILoggingEvent> appender =
            (ConsoleAppender<ILoggingEvent>) logger.getAppender("console");

        final List<Filter<ILoggingEvent>> filters = appender.getCopyOfAttachedFiltersList();
        if ((null != filters) && (filters.size() > 0)) {
          LogbackLevelFilter expFilter = null;
          for (final Filter<ILoggingEvent> filter : filters) {
            if (filter instanceof LogbackLevelFilter) {
              expFilter = (LogbackLevelFilter) filter;
              break;
            }
          }

          // 只测试应用了 LogbackLevelFilter 过滤器的日志。
          final LayoutWrappingEncoder<ILoggingEvent> encoder =
              (LayoutWrappingEncoder<ILoggingEvent>) appender.getEncoder();
          final PatternLayout layout = (PatternLayout) encoder.getLayout();
          Assert.assertEquals(pattern, layout.getPattern());

          Assert.assertNotNull(expFilter);
          Assert.assertTrue(expFilter.isStarted());
          Assert.assertEquals(Level.DEBUG, expFilter.getLevelToKeep());
        }
      }
    }
  }

  @Test
  public void testSetupFileLoggerForStringLevelString() throws Exception {
    final String name = "testSetupFileLoggerForStringLevelString";
    LogbackUtil.setupFileLoggerFor(name, Level.INFO, name + ".txt");

    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final Logger logger = lc.getLogger(name);
    Assert.assertNotNull(logger);
    Assert.assertEquals(Level.INFO, logger.getLevel());

    final Appender<ILoggingEvent> objAppender = logger.getAppender(name + "-file");
    Assert.assertTrue(objAppender instanceof RollingFileAppender);
    final RollingFileAppender<ILoggingEvent> appender =
        (RollingFileAppender<ILoggingEvent>) objAppender;
    // 验证日志文件保存路径
    final String path = Preferences.getLogDir() + File.separator + name + ".txt";
    Assert.assertEquals(path, appender.getFile());
    logger.info(name);
    // 验证日志文件内容
    final String content = IOUtils.toString(new FileInputStream(path));
    Assert.assertTrue(content.contains(name));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSetupFileLoggerForStringLevelStringStringInt() throws Exception {
    final String name = "testSetupFileLoggerForStringLevelStringStringInt";
    final String pattern = "%msg%n";
    LogbackUtil.setupFileLoggerFor(name, Level.WARN, name + ".txt", pattern, 5);

    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final Logger logger = lc.getLogger(name);
    Assert.assertNotNull(logger);
    Assert.assertEquals(Level.WARN, logger.getLevel());

    final Appender<ILoggingEvent> objAppender = logger.getAppender(name + "-file");
    Assert.assertTrue(objAppender instanceof RollingFileAppender);
    final RollingFileAppender<ILoggingEvent> appender =
        (RollingFileAppender<ILoggingEvent>) objAppender;
    // 验证归档策略
    final TimeBasedRollingPolicy<ILoggingEvent> policy =
        (TimeBasedRollingPolicy<ILoggingEvent>) appender.getRollingPolicy();
    Assert.assertEquals(5, policy.getMaxHistory());

    // 验证日志格式
    final PatternLayoutEncoder encoder = (PatternLayoutEncoder) appender.getEncoder();
    Assert.assertEquals(pattern, encoder.getPattern());

    // 验证日志文件保存路径
    final String path = Preferences.getLogDir() + File.separator + name + ".txt";
    Assert.assertEquals(path, appender.getFile());
    logger.warn(name);
    // 验证日志文件内容
    final String content = IOUtils.toString(new FileInputStream(path));
    Assert.assertTrue(content.contains(name));
  }

  @Test
  public void testSetupFileLoggerForLevelString() throws Exception {
    final String loggerName = "testSetupFileLoggerForLevelString";
    LogbackUtil.setupFileLoggerFor(Level.WARN, loggerName + ".txt");

    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    for (final Logger logger : loggers) {
      if (!loggerName.equals(logger.getName())) {
        continue;
      }

      Assert.assertEquals(Level.WARN, logger.getLevel());

      final RollingFileAppender<ILoggingEvent> appender =
          (RollingFileAppender<ILoggingEvent>) logger.getAppender(logger.getName() + "-file");

      // 验证日志文件保存路径
      final String path = Preferences.getLogDir() + File.separator + loggerName + ".txt";
      Assert.assertEquals(path, appender.getFile());
      logger.error(loggerName);
      // 验证日志文件内容
      final String content = IOUtils.toString(new FileInputStream(path));
      Assert.assertTrue(content.contains(loggerName));
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSetupFileLoggerForLevelStringStringInt() throws Exception {
    final String loggerName = "testSetupFileLoggerForLevelStringStringInt";
    final String pattern = "%msg%n";
    LogbackUtil.setupFileLoggerFor(Level.WARN, loggerName + ".txt", pattern, 3);

    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    for (final Logger logger : loggers) {
      if (!loggerName.equals(logger.getName())) {
        continue;
      }
      Assert.assertEquals(Level.WARN, logger.getLevel());

      final RollingFileAppender<ILoggingEvent> appender =
          (RollingFileAppender<ILoggingEvent>) logger.getAppender(logger.getName() + "-file");

      // 验证归档策略
      final TimeBasedRollingPolicy<ILoggingEvent> policy =
          (TimeBasedRollingPolicy<ILoggingEvent>) appender.getRollingPolicy();
      Assert.assertEquals(3, policy.getMaxHistory());

      // 验证日志格式
      final PatternLayoutEncoder encoder = (PatternLayoutEncoder) appender.getEncoder();
      Assert.assertEquals(pattern, encoder.getPattern());

      // 验证日志文件保存路径
      final String path = Preferences.getLogDir() + File.separator + loggerName + ".txt";
      Assert.assertEquals(path, appender.getFile());
      logger.error(loggerName);
      // 验证日志文件内容
      final String content = IOUtils.toString(new FileInputStream(path));
      Assert.assertTrue(content.contains(loggerName));
    }
  }

}
