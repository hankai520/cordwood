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

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:59:10 PM
 */
public class DateUtilTest {

  @Test
  public void testThisMonth() {
    final Date[] dates = DateUtil.thisMonth();
    Assert.assertNotNull(dates);
    Assert.assertEquals(2, dates.length);
    final Calendar cal = Calendar.getInstance();
    final int thisMonth = cal.get(Calendar.MONTH);
    for (final Date date : dates) {
      cal.setTime(date);
      Assert.assertEquals(thisMonth, cal.get(Calendar.MONTH));
    }
  }

  @Test
  public void testThisMonthWithDate() {
    Date now = new Date();
    final int testDays = 365 * 100;
    for (int i = 1; i <= testDays; i++) {
      now = DateUtils.addDays(now, i);
      final Date[] dates = DateUtil.thisMonth(now);
      Assert.assertNotNull(dates);
      Assert.assertEquals(2, dates.length);
      final Calendar cal = Calendar.getInstance();
      cal.setTime(now);
      final int thisMonth = cal.get(Calendar.MONTH);
      for (final Date date : dates) {
        cal.setTime(date);
        Assert.assertEquals(thisMonth, cal.get(Calendar.MONTH));
      }
    }
  }

}
