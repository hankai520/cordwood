
package ren.hankai.cordwood.core.util;

import org.springframework.util.Assert;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 算数运算助手类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 5, 2017 10:33:28 AM
 */
public final class MathUtil {

  /**
   * 将数字根据基数进行进位处理，得到基于最高单位的值，并返回阅读友好的格式。
   *
   * @param value 数值
   * @param radix 基数（例如：时间是60进制，基数为60，计算机存储为1024进制，则基数为1024）
   * @param units 单位数组（从小到大，例如：B, KB, MB），首元素为 value < radix时的单位
   * @return 阅读友好的数值字符串
   * @author hankai
   * @since Jan 5, 2017 10:42:31 AM
   */
  @Deprecated
  public static String toHumanReadableString(double value, long radix, String[] units) {
    return shortenNumber((long) value, radix, true, units);
  }

  /**
   * 将数字转换为带有单位的，可读性较强的形式。
   *
   * @param number 数字
   * @param radix 基数（例如：时间是60进制，基数为60，计算机存储为1024进制，则基数为1024）。
   *          不支持可变基数，例如时间是60进制，但大于24小时后的天、月，基数都会变动
   * @param useMaxUnit 是否只用最大单位来表示数字，useMaxUnit为true，返回形如 1.32GB，useMaxUnit为false，返回形如 1h30m28s
   * @param units 数字的单位（从小到大，例如：B, KB, MB），首元素为 value < radix时的单位
   * @return 格式化后的数字
   * @author hankai
   * @since Dec 1, 2018 9:59:08 PM
   */
  public static String shortenNumber(long number, long radix, boolean useMaxUnit,
      String... units) {
    Assert.notNull(units, "Units cannot be null!");
    Assert.notEmpty(units, "Units cannot be empty!");
    Assert.isTrue(radix > 0, "Radix must be greater than zero!");
    if (number > radix) {
      Assert.isTrue(units.length > 1,
          "Number of units must be greater than or equal to two when number greater than radix!");
    }
    if (useMaxUnit) {
      double value = number;
      int unitIndex = 0;
      while ((value >= radix) && (unitIndex < units.length)) {
        value /= radix;
        unitIndex++;
      }
      final DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(2);
      df.setMaximumIntegerDigits(Integer.MAX_VALUE);
      df.setRoundingMode(RoundingMode.HALF_UP);
      final String shortenNum = df.format(value) + units[unitIndex];
      return shortenNum;
    } else {
      final StringBuffer shortenNum = new StringBuffer();
      int unitIndex = 0;
      while ((number >= radix) && (unitIndex < (units.length - 1))) {
        final long remainder = number % radix;
        if (remainder > 0) {
          // 只有未整除的情况才需要插入当前单位的数字
          shortenNum.insert(0, remainder + units[unitIndex]);
        }
        unitIndex++;
        // 上升一位
        number /= radix;
      }
      shortenNum.insert(0, number + units[unitIndex]);
      return shortenNum.toString();
    }
  }

}
