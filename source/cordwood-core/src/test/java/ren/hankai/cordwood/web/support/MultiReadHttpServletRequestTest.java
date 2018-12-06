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
package ren.hankai.cordwood.web.support;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import ren.hankai.cordwood.web.support.MultiReadHttpServletRequest.CachedServletInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * 测试允许多次读取的 Servlet 请求。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 1:52:13 PM
 */
public class MultiReadHttpServletRequestTest {

  @Test
  public void testGetInputStream() throws Exception {
    final String requestBody = "{\"name\": \"张三\"}";

    final MockHttpServletRequest request = new MockHttpServletRequest();
    request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    request.setContent(requestBody.getBytes("UTF-8"));

    final MultiReadHttpServletRequest wr = new MultiReadHttpServletRequest(request);
    final InputStream inputStream = wr.getInputStream();
    Assert.assertNotNull(inputStream);
    ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, byteOutStream);
    Assert.assertEquals(requestBody, byteOutStream.toString("UTF-8"));
    Assert.assertTrue(inputStream instanceof CachedServletInputStream);
    final CachedServletInputStream cis = (CachedServletInputStream) inputStream;
    Assert.assertTrue(cis.isFinished());
    Assert.assertTrue(cis.isReady());
    cis.setReadListener(null);// 内部不实现此功能，调用仅检查是否报错

    /* 测试第二次读取请求内容 */
    // 字符流
    final BufferedReader reader = wr.getReader();
    final StringWriter writer = new StringWriter();
    IOUtils.copy(reader, writer);
    Assert.assertEquals(requestBody, byteOutStream.toString("UTF-8"));

    // 字节流
    byteOutStream = new ByteArrayOutputStream();
    IOUtils.copy(wr.getInputStream(), byteOutStream);
    Assert.assertEquals(requestBody, byteOutStream.toString("UTF-8"));
  }

}
