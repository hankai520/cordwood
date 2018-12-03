
package ren.hankai.cordwood.core.util;

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
    Assert.assertTrue((dates.length > 0) && (dates.length <= 31));
    final Calendar cal = Calendar.getInstance();
    final int thisMonth = cal.get(Calendar.MONTH);
    for (final Date date : dates) {
      cal.setTime(date);
      Assert.assertEquals(thisMonth, cal.get(Calendar.MONTH));
    }
  }

}
