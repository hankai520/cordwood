
package ren.hankai.cordwood.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Date;

public class TimestampSerializerTest {

  @Test
  public void testSerialize() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final StringWriter sw = new StringWriter();
    final JsonGenerator jgen = om.getFactory().createGenerator(sw);

    final Date date = new Date();
    final TimestampSerializer ser = new TimestampSerializer();
    ser.serialize(date, jgen, om.getSerializerProvider());
    jgen.flush();

    Assert.assertEquals((date.getTime() / 1000) + "", sw.toString());
  }

}
