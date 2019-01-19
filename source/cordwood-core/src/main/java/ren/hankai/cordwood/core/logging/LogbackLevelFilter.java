/*******************************************************************************
 * Copyright (C) 2019 hankai
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

package ren.hankai.cordwood.core.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.springframework.util.Assert;

/**
 * Logback 日志级别过滤器，可按需要过滤日志。
 *
 * @author hankai
 * @version 1.0
 * @since Apr 28, 2018 12:39:51 PM
 */
public class LogbackLevelFilter extends AbstractMatcherFilter<ILoggingEvent> {

  private Level levelToKeep = null;

  /**
   * 构造过滤器，只保留特定级别的日志。
   *
   * @param level 需保留的日志级别（除此以外的级别将不被记录）
   */
  public LogbackLevelFilter(Level level) {
    super();
    Assert.notNull(level, "Level to keep must not be null!");
    levelToKeep = level;
  }

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (!isStarted()) {
      return FilterReply.NEUTRAL;
    }
    final LoggingEvent loggingEvent = (LoggingEvent) event;
    if (loggingEvent.getLevel().isGreaterOrEqual(levelToKeep)) {
      return FilterReply.NEUTRAL;
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
  public Level getLevelToKeep() {
    return levelToKeep;
  }

}
