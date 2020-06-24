
package ren.hankai.cordwood.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 将日期序列化为以毫秒为单位的时间戳。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 3, 2017 9:55:15 PM
 */
public class TimeInMillisecondSerializer extends JsonSerializer<Date> {

  @Override
  public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    jgen.writeNumber(value.getTime());
  }
}
