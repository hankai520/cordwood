
package ren.hankai.cordwood.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Date;

public class DateTimeSerializerTest {

  @Test
  public void testSerialize() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final StringWriter sw = new StringWriter();
    final JsonGenerator jgen = om.getFactory().createGenerator(sw);

    final Date date = DateUtils.parseDate("2018-11-12 13:22:22", "yyyy-MM-dd HH:mm:ss");
    final DateTimeSerializer ser = new DateTimeSerializer();
    ser.serialize(date, jgen, om.getSerializerProvider());
    jgen.flush();

    Assert.assertEquals("\"2018-11-12 13:22:22\"", sw.toString());
  }

  @Test
  public void testSerializeWithCustomFormat() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final StringWriter sw = new StringWriter();
    final JsonGenerator jgen = om.getFactory().createGenerator(sw);

    final Date date = DateUtils.parseDate("2018-11-12 11:11:11", "yyyy-MM-dd HH:mm:ss");
    final DateTimeSerializer ser = new DateTimeSerializer("yyyy|MM|dd HH|mm|ss");
    ser.serialize(date, jgen, om.getSerializerProvider());
    jgen.flush();

    Assert.assertEquals("\"2018|11|12 11|11|11\"", sw.toString());
  }

}
