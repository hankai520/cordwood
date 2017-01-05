
package ren.hankai.cordwood.core.util;

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
   * @param radixes 基数数组（例如：时间是60进制，基数为60，计算机存储为1024进制，则基数为1024）
   * @param units 单位数组（从小到大，例如：b, kb, mb, gb, tb）
   * @return 阅读友好的数值字符串
   * @author hankai
   * @since Jan 5, 2017 10:42:31 AM
   */
  public static String toHumanReadableString(double value, long[] radixes, String[] units) {
    int unitIndex = 0;
    int radixIndex = 0;
    while ((value >= radixes[radixIndex]) && (unitIndex < units.length)) {
      value /= radixes[radixIndex];
      if (radixIndex < radixes.length) {
        radixIndex++;
      }
      unitIndex++;
    }
    final DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMaximumIntegerDigits(10);
    df.setRoundingMode(RoundingMode.HALF_UP);
    final String readableValue = df.format(value) + units[unitIndex];
    return readableValue;
  }

}
