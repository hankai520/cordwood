/*******************************************************************************
 * Copyright (C) 2018 hankai
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

package ren.hankai.cordwood.jackson;

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
