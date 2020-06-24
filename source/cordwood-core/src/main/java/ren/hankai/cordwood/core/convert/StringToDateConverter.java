
package ren.hankai.cordwood.core.convert;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 字符串到日期转换器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 1:47:13 PM
 */
public class StringToDateConverter implements Converter<String, Date> {

  private static final Logger logger = LoggerFactory.getLogger(StringToDateConverter.class);

  /**
   * 数据交换时，默认的日期格式。
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  /**
   * 数据交换式时，默认的日期时间格式。
   */
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final List<SimpleDateFormat> formatters = new ArrayList<SimpleDateFormat>(3);

  /**
   * 构造日期转换器。
   *
   * @param format 自定义日期格式
   */
  public StringToDateConverter(String format) {
    if (StringUtils.isNotEmpty(format)) {
      formatters.add(new SimpleDateFormat(format));
    }
    formatters.add(new SimpleDateFormat(DATE_TIME_FORMAT));
    formatters.add(new SimpleDateFormat(DATE_FORMAT));
  }

  @Override
  public Date convert(String source) {
    for (final SimpleDateFormat sdf : formatters) {
      try {
        final Date date = sdf.parse(source);
        if (null != date) {
          return date;
        }
      } catch (final ParseException expected) {
      }
    }
    logger.debug(String.format("Failed to parse input date string \"%s\".", source));
    return null;
  }

}
