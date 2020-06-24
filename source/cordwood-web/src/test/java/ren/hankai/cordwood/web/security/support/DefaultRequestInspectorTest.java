
package ren.hankai.cordwood.web.security.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import ren.hankai.cordwood.web.security.RequestInspector;
import ren.hankai.cordwood.web.test.WebTestSupport;

import java.util.HashMap;
import java.util.Map;

public class DefaultRequestInspectorTest extends WebTestSupport {

  private final DefaultRequestInspector inspector = new DefaultRequestInspector();

  @Test
  public void testBuildSignText() {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("p1", "hello");
    parameters.put("p2", "中文");
    parameters.put("p3", "%8E");
    final String sk = "1234";
    final String sign = inspector.buildSignText(parameters, sk);
    Assert.assertEquals("p1=hello&p2=%E4%B8%AD%E6%96%87&p3=%258E" + sk, sign);
  }

  @Test
  public void testSignRequestParametersMapOfStringQ() {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("p1", "hello");
    parameters.put("p2", "中文");
    parameters.put("p3", "%8E");
    final String sign = inspector.signRequestParameters(parameters);
    Assert.assertEquals("57cbfbccaea63df05f4bc4624525d908d3052148", sign);
  }

  @Test
  public void testSignRequestParametersMapOfStringQString() {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("p1", "hello");
    parameters.put("p2", "中文");
    parameters.put("p3", "%8E");
    final String sk = "1234";
    final String sign = inspector.signRequestParameters(parameters, sk);
    Assert.assertEquals("a63ef18bb282489a59736091bd042440a0a11138", sign);
  }

  @Test
  public void testSignRequestBodyString() {
    final String sign = inspector.signRequestBody("body");
    Assert.assertEquals("a5ca80d6fdb018c33cdd6c87001f2b697bc147f5", sign);
  }

  @Test
  public void testSignRequestBodyStringString() {
    final String sign = inspector.signRequestBody("body", "1234");
    Assert.assertEquals("f3628fd1ee3ddecfa4f0affb805f2c1f605e6b07", sign);
  }

  @Test
  public void testVerifyRequestParametersMapOfStringQ() {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("p1", "hello");
    parameters.put("p2", "中文");
    parameters.put("p3", "%8E");
    parameters.put(RequestInspector.REQUEST_SIGN, "57cbfbccaea63df05f4bc4624525d908d3052148");
    final boolean result = inspector.verifyRequestParameters(parameters);
    Assert.assertTrue(result);
  }

  @Test
  public void testVerifyRequestParametersMapOfStringQString() {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("p1", "hello");
    parameters.put("p2", "中文");
    parameters.put("p3", "%8E");
    parameters.put(RequestInspector.REQUEST_SIGN, "a63ef18bb282489a59736091bd042440a0a11138");
    final String sk = "1234";
    final boolean result = inspector.verifyRequestParameters(parameters, sk);
    Assert.assertTrue(result);
  }

  @Test
  public void testVerifyRequestParametersHttpServletRequest() throws Exception {
    final String url = "http://unittest.org/m1";

    MockHttpServletRequest request = new MockHttpServletRequest("POST", url);
    request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    request.setParameter("p1", "hello");
    request.setParameter("p2", "中文");
    request.setParameter("p3", "%8E");
    request.setParameter(RequestInspector.REQUEST_SIGN, "57cbfbccaea63df05f4bc4624525d908d3052148");
    boolean result = inspector.verifyRequestParameters(request);
    Assert.assertTrue(result);

    request = new MockHttpServletRequest("POST", url);
    request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    request.setContent("{\"name\":\"你好\",\"age\":25}".getBytes("UTF-8"));
    request.setParameter(RequestInspector.REQUEST_SIGN, "fd19a98c6a44d7119ac9eb57a610699a7ddaebfc");
    result = inspector.verifyRequestParameters(request);
    Assert.assertTrue(result);
  }

  @Test
  public void testVerifyRequestParametersHttpServletRequestString() throws Exception {
    final String url = "http://unittest.org/m1";

    MockHttpServletRequest request = new MockHttpServletRequest("POST", url);
    request.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    request.setParameter("p1", "hello");
    request.setParameter("p2", "中文");
    request.setParameter("p3", "%8E");
    request.setParameter(RequestInspector.REQUEST_SIGN, "a63ef18bb282489a59736091bd042440a0a11138");
    final String sk = "1234";
    boolean result = inspector.verifyRequestParameters(request, sk);
    Assert.assertTrue(result);

    request = new MockHttpServletRequest("POST", url);
    request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    request.setContent("{\"name\":\"你好\",\"age\":25}".getBytes("UTF-8"));
    request.setParameter(RequestInspector.REQUEST_SIGN, "7ac6f883da5791a4b5a78ad23ea51b2be5dc0a6d");
    result = inspector.verifyRequestParameters(request, sk);
    Assert.assertTrue(result);
  }

}
