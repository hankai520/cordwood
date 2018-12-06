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
