
package ren.hankai.cordwood.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

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
public class DateSerializer extends JsonSerializer<Date> {

  private String format = "yyyy-MM-dd";

  public DateSerializer() {}

  public DateSerializer(String format) {
    this.format = format;
  }

  @Override
  public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    final SimpleDateFormat sdf = new SimpleDateFormat(format);
    jgen.writeString(sdf.format(value));
  }
}
