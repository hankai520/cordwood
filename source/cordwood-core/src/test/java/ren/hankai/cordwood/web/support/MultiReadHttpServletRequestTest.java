
package ren.hankai.cordwood.web.support;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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

    // 测试第二次读取请求内容
    byteOutStream = new ByteArrayOutputStream();
    IOUtils.copy(wr.getInputStream(), byteOutStream);
    Assert.assertEquals(requestBody, byteOutStream.toString("UTF-8"));
  }

}
