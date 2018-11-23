
package ren.hankai.cordwood.core.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Logback 日志级别过滤器，可按需要过滤日志。
 *
 * @author hankai
 * @version 1.0
 * @since Apr 28, 2018 12:39:51 PM
 */
public class LogbackLevelFilter extends AbstractMatcherFilter<ILoggingEvent> {

  private final List<Level> levelsToKeep = new ArrayList<>();

  private LogbackLevelFilter() {}

  /**
   * 构造过滤器，只保留特定级别的日志。
   *
   * @param levels 需保留的日志级别（除此以外的级别将不被记录）
   */
  public LogbackLevelFilter(Level... levels) {
    super();
    if ((levels != null) && (levels.length > 0)) {
      for (final Level level : levels) {
        levelsToKeep.add(level);
      }
    }
  }

  /**
   * 构造过滤器，保留指定级别或更高级别的日志。
   *
   * @param level 需要保留的级别（包含更高级别）
   */
  public LogbackLevelFilter(Level level) {
    super();
    if (null != level) {
      levelsToKeep.add(level);
    }
  }

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (!isStarted()) {
      return FilterReply.NEUTRAL;
    }
    final LoggingEvent loggingEvent = (LoggingEvent) event;
    if (levelsToKeep.size() == 1) {
      final Level level = levelsToKeep.get(0);
      if (loggingEvent.getLevel().isGreaterOrEqual(level)) {
        return FilterReply.NEUTRAL;
      }
    } else {
      if (levelsToKeep.contains(loggingEvent.getLevel())) {
        return FilterReply.NEUTRAL;
      }
    }
    return FilterReply.DENY;
  }

  /**
   * 获取被保留的日志级别。
   *
   * @return 被保留的日志级别
   * @author hankai
   * @since Nov 22, 2018 5:27:56 PM
   */
  public List<Level> getLevelsToKeep() {
    return levelsToKeep;
  }

}
