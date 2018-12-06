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
public class DateTimeDeserializer extends JsonDeserializer<Date> {

  private static final Logger logger = LoggerFactory.getLogger(DateTimeDeserializer.class);

  private String format = "yyyy-MM-dd HH:mm:ss";

  public DateTimeDeserializer() {}

  public DateTimeDeserializer(String format) {
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
