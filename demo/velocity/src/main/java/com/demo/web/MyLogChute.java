
package com.demo.web;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Velocity 日志。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 19, 2016 6:49:47 PM
 */
public class MyLogChute implements LogChute {

  private Logger logger = null;

  public MyLogChute() {
    this(null);
  }

  public MyLogChute(String loggerName) {
    loggerName = (loggerName == null) ? MyLogChute.class.getName() : loggerName;
    logger = LoggerFactory.getLogger(loggerName);
  }

  @Override
  public void init(RuntimeServices rs) throws Exception {}

  @Override
  public boolean isLevelEnabled(int level) {
    return true;
  }

  @Override
  public void log(int level, String message) {
    if (level == LogChute.TRACE_ID) {
      logger.trace(message);
    } else if (level == LogChute.DEBUG_ID) {
      logger.debug(message);
    } else if (level == LogChute.INFO_ID) {
      logger.info(message);
    } else if (level == LogChute.WARN_ID) {
      logger.warn(message);
    } else if (level == LogChute.ERROR_ID) {
      logger.error(message);
    }
  }

  @Override
  public void log(int level, String message, Throwable throwable) {
    if (level == LogChute.TRACE_ID) {
      logger.trace(message, throwable);
    } else if (level == LogChute.DEBUG_ID) {
      logger.debug(message, throwable);
    } else if (level == LogChute.INFO_ID) {
      logger.info(message, throwable);
    } else if (level == LogChute.WARN_ID) {
      logger.warn(message, throwable);
    } else if (level == LogChute.ERROR_ID) {
      logger.error(message, throwable);
    }
  }
}
