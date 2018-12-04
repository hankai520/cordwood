
package ren.hankai.cordwood.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateTimeDeserializerTest {

  @Test
  public void testDeserialize() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateTimeDeserializer des = new DateTimeDeserializer();
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"2018-09-01 13:23:12\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    final Date expDate = DateUtils.parseDate(jp.getText(), "yyyy-MM-dd HH:mm:ss");
    Assert.assertTrue(DateUtils.truncatedEquals(date, expDate, Calendar.SECOND));
  }

  @Test
  public void testDeserializeWithCustomFormat() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateTimeDeserializer des = new DateTimeDeserializer("yyyy|MM|dd HH|mm|ss");
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"2018|09|01 13|23|12\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    final Date expDate = DateUtils.parseDate(jp.getText(), "yyyy|MM|dd HH|mm|ss");
    Assert.assertTrue(DateUtils.truncatedEquals(date, expDate, Calendar.SECOND));
  }

  @Test
  public void testDeserializeInvalidDate() throws Exception {
    final ObjectMapper om = new ObjectMapper();
    final DateTimeDeserializer des = new DateTimeDeserializer();
    final JsonParser jp = om.getFactory().createParser("{\"date\": \"20180901 13:23|12\"}");

    String val = null;
    do {
      val = jp.nextTextValue();
    } while (val == null);

    final Date date = des.deserialize(jp, om.getDeserializationContext());
    Assert.assertNull(date);
  }

}
