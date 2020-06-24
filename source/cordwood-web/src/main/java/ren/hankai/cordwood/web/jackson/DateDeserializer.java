
package ren.hankai.cordwood.web.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期到字符串序列化（用于jackson）。
 *
 * @author hankai
 * @version 1.0
 * @since Jul 27, 2015 11:34:49 AM
 */
public class DateDeserializer extends JsonDeserializer<Date> {

  private static final Logger logger = LoggerFactory.getLogger(DateDeserializer.class);

  private String format = "yyyy-MM-dd";

  public DateDeserializer() {}

  public DateDeserializer(String format) {
    this.format = format;
  }

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    try {
      final SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.parse(jp.getText());
    } catch (final Exception ex) {
      logger.warn(String.format("Failed to parse date from json string %s", jp.getText()), ex);
      return null;
    }
  }
}
