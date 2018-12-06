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

package ren.hankai.cordwood.web.security.support;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ren.hankai.cordwood.web.security.annotation.Stabilized;

/**
 * 限流信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 12, 2018 9:13:44 AM
 */
public class RateLimitInfo {

  private static final Logger logger = LoggerFactory.getLogger(RateLimitInfo.class);

  private final RateLimiter limiter;// 限流器

  private int failures;// 失败次数

  private long lastFailureTime;// 上次失败的时间

  /**
   * 用限流器来构造限流信息。
   *
   * @param limiter 限流器
   */
  public RateLimitInfo(RateLimiter limiter) {
    this.limiter = limiter;
  }

  /**
   * 获取限流器。
   *
   * @return 限流器
   * @author hankai
   * @since Oct 12, 2018 9:33:30 AM
   */
  public RateLimiter getLimiter() {
    return limiter;
  }

  /**
   * 获取失败次数。
   *
   * @return 失败次数
   * @author hankai
   * @since Oct 12, 2018 9:33:24 AM
   */
  public int getFailures() {
    return failures;
  }

  /**
   * 添加失败次数。
   *
   * @author hankai
   * @since Oct 12, 2018 9:33:22 AM
   */
  public void addFailures() {
    this.failures += 1;
    this.lastFailureTime = System.currentTimeMillis();
  }

  /**
   * 重置失败次数（用于解除熔断）。
   *
   * @author hankai
   * @since Oct 12, 2018 9:33:20 AM
   */
  public void resetFailures() {
    this.failures = 0;
  }

  /**
   * 判断是否已触发熔断。
   *
   * @param stabilize 稳定器注解
   * @return 是否已熔断
   * @author hankai
   * @since Oct 12, 2018 9:33:17 AM
   */
  public boolean isFused(Stabilized stabilize) {
    if (stabilize.fusingThreshold() < failures) {
      final long timespan = System.currentTimeMillis() - lastFailureTime;
      logger.debug(String.format(
          "Failures=%d, threshold=%d; timespan=%dms, interval=%dms", failures,
          stabilize.fusingThreshold(), timespan, stabilize.fusingInterval()));
      if (timespan <= stabilize.fusingInterval()) {
        return true;
      }
    }
    return false;
  }
}
