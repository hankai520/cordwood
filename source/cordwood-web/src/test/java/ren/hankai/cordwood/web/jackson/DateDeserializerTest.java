/*******************************************************************************
 * Copyright (C) 2019 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package ren.hankai.cordwood.web.jackson;

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
