
package ren.hankai.cordwood.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Date;

public class DateSerializerTest {

  @Test
  public void testSerialize() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final StringWriter sw = new StringWriter();
    final JsonGenerator jgen = om.getFactory().createGenerator(sw);

    final Date date = DateUtils.parseDate("2018-11-12", "yyyy-MM-dd");
    final DateSerializer ser = new DateSerializer();
    ser.serialize(date, jgen, om.getSerializerProvider());
    jgen.flush();

    Assert.assertEquals("\"2018-11-12\"", sw.toString());
  }

  @Test
  public void testSerializeWithCustomFormat() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final StringWriter sw = new StringWriter();
    final JsonGenerator jgen = om.getFactory().createGenerator(sw);

    final Date date = DateUtils.parseDate("2018-11-12", "yyyy-MM-dd");
    final DateSerializer ser = new DateSerializer("yyyy|MM|dd");
    ser.serialize(date, jgen, om.getSerializerProvider());
    jgen.flush();

    Assert.assertEquals("\"2018|11|12\"", sw.toString());
  }

}
