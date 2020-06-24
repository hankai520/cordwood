
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
