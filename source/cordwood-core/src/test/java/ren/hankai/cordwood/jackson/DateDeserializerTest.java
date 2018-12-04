
package ren.hankai.cordwood.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateDeserializerTest {

  @Test
  public void testDeserialize() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateDeserializer des = new DateDeserializer();
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"2018-09-01\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    final Date expDate = DateUtils.parseDate(jp.getText(), "yyyy-MM-dd");
    Assert.assertTrue(DateUtils.isSameDay(date, expDate));
  }

  @Test
  public void testDeserializeWithCustomFormat() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateDeserializer des = new DateDeserializer("yyyy|MM|dd");
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"2018|09|01\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    final Date expDate = DateUtils.parseDate(jp.getText(), "yyyy|MM|dd");
    Assert.assertTrue(DateUtils.isSameDay(date, expDate));
  }

  @Test
  public void testDeserializeInvalidDate() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateDeserializer des = new DateDeserializer();
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"20180901\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    Assert.assertNull(date);
  }

}
