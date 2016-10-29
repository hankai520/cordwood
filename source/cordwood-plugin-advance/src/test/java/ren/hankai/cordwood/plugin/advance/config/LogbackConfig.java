
package ren.hankai.cordwood.plugin.advance.config;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import java.io.File;

/**
 * Logback 配置类，用于取代 XML 配置文件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 10, 2016 11:00:14 AM
 */
public class LogbackConfig extends BasicConfigurator implements Configurator {

  static {
    String logDir = System.getProperty("user.dir") + File.separator + "logs";
    File file = new File(logDir);
    if (!file.exists() || !file.isDirectory()) {
      file.mkdirs();
    }
    System.setProperty("app.log", logDir);
  }

  private ConsoleAppender<ILoggingEvent> getConsoleAppender(LoggerContext lc) {
    ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<>();
    ca.setContext(lc);
    ca.setName("console");
    LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
    encoder.setContext(lc);
    PatternLayout layout = new PatternLayout();
    layout.setContext(lc);
    layout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
    layout.start();
    encoder.setLayout(layout);
    ca.setEncoder(encoder);
    ca.start();
    return ca;
  }

  private RollingFileAppender<ILoggingEvent> getFileAppender(LoggerContext lc) {
    String logDir = System.getProperty("app.log");
    RollingFileAppender<ILoggingEvent> rfa = new RollingFileAppender<>();
    rfa.setContext(lc);
    rfa.setName("plugin-file");
    String file = String.format("%s/errors.txt", logDir);
    rfa.setFile(file);
    TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
    String namePattern = String.format("%s/errors.txt.%%d{yyyy-MM-dd}.zip ", logDir);
    policy.setFileNamePattern(namePattern);
    policy.setMaxHistory(7);
    policy.setContext(lc);
    policy.setParent(rfa);
    policy.start();
    rfa.setRollingPolicy(policy);
    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(lc);
    encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
    encoder.start();
    rfa.setEncoder(encoder);
    rfa.start();
    return rfa;
  }

  @Override
  public void configure(LoggerContext lc) {
    Logger rootLogger = lc.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    rootLogger.addAppender(getConsoleAppender(lc));
    rootLogger.setLevel(Level.INFO);
    Logger appLogger = lc.getLogger("ren.hankai.cordwood.plugin");
    appLogger.addAppender(getFileAppender(lc));
    appLogger.setLevel(Level.WARN);
  }
}
