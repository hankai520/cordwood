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

package ren.hankai.cordwood.core.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期助手类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 2:12:34 PM
 */
public final class DateUtil {

  /**
   * 获取本月的第一天和最后1天。
   *
   * @param now 当前时间
   * @return 数组第一个元素为本月第一天(00:00:00.000)，第二个为本月最后1天(23:59:59:999)
   * @author hankai
   * @since Dec 5, 2018 3:41:37 PM
   */
  public static Date[] thisMonth(Date now) {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    final Date beginTime = cal.getTime();
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    final Date endTime = cal.getTime();
    return new Date[] {beginTime, endTime};
  }

  /**
   * 获取本月的第一天和最后1天。
   *
   * @return 数组第一个元素为本月第一天(00:00:00.000)，第二个为本月最后1天(23:59:59:999)
   * @author hankai
   * @since Dec 12, 2016 2:13:36 PM
   */
  public static Date[] thisMonth() {
    return thisMonth(new Date());
  }

}
