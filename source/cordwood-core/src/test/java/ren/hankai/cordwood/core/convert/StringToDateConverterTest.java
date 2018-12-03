
package ren.hankai.cordwood.core.convert;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class StringToDateConverterTest {

  private final int testDays = 368;// 测试一个完整周期（一年）

  @Test
  public void testConvertWithformat() throws Exception {
    final String format = "yyyy|MM|dd";
    final StringToDateConverter converter = new StringToDateConverter(format);

    final Date now = new Date();
    // 测试3个月左右的时间
    for (int i = 1; i <= testDays; i++) {
      final Date expDate = DateUtils.addDays(now, i);
      final String strDate = DateFormatUtils.format(expDate, format);
      final Date actual = converter.convert(strDate);
      Assert.assertTrue(DateUtils.isSameDay(actual, expDate));
    }
  }

  @Test
  public void testConvertWithoutformat() throws Exception {
    final StringToDateConverter converter = new StringToDateConverter(null);

    final Date now = new Date();
    // 测试3个月左右的时间
    for (int i = 1; i <= testDays; i++) {
      final Date expDate = DateUtils.addDays(now, i);
      String strDate = null;
      if ((i % 2) == 0) {
        strDate = DateFormatUtils.format(expDate, "yyyy-MM-dd");
        final Date actual = converter.convert(strDate);
        Assert.assertTrue(DateUtils.isSameDay(actual, expDate));
      } else {
        strDate = DateFormatUtils.format(expDate, "yyyy-MM-dd HH:mm:ss");
        final Date actual = converter.convert(strDate);
        Assert.assertTrue(DateUtils.truncatedEquals(actual, expDate, Calendar.SECOND));
      }
    }
  }

}
