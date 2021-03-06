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

package ren.hankai.cordwood.web.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 增强 Spring 控制器类或方法的访问稳定性，支持限流。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 11, 2018 6:48:30 PM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Stabilized {

  /**
   * 最大QPS（Query per second）。
   *
   * @return 最大QPS
   * @author hankai
   * @since Oct 11, 2018 6:54:37 PM
   */
  double maxQps() default 100.0f;

  /**
   * 响应超时时间。并非表示单个请求的执行时间，而是受理请求前的等待时间。例如，在高并发场景下，触发限流机制，导致后续请求排队， 则排队需要设定超时时间，否则容易引发雪崩。
   *
   * @return 请求受理超时时间（毫秒）
   * @author hankai
   * @since Oct 12, 2018 9:06:14 AM
   */
  long timeout() default 1000;

  /**
   * 热身周期，该周期内，请求会匀速到达，逐步达到最大qps，之后进入正常工作状态。
   *
   * @return 热身周期（毫秒）
   * @author hankai
   * @since Oct 12, 2018 10:16:01 AM
   */
  long warmupPeriod() default 1000 * 2;

  /**
   * 熔断阈值，连续超时或无法受理请求的次数超过此限制，则触发熔断机制。
   *
   * @return 熔断阈值
   * @author hankai
   * @since Oct 12, 2018 9:12:16 AM
   */
  int fusingThreshold() default 3;

  /**
   * 熔断恢复间隔（毫秒）。
   *
   * @return 熔断恢复间隔
   * @author hankai
   * @since Oct 12, 2018 9:39:06 AM
   */
  long fusingInterval() default 1000 * 5;
}
