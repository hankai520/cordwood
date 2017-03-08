
package ren.hankai.cordwood.console.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.test.ConsoleTestSupport;
import ren.hankai.cordwood.core.api.support.ApiCode;
import ren.hankai.cordwood.core.api.support.ApiResponse;

import java.util.HashMap;

/**
 * 插件访问授权测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Mar 7, 2017 9:07:50 AM
 */
public class AuthenticationApiTest extends ConsoleTestSupport {

  @Autowired
  private ObjectMapper om;

  @SuppressWarnings("unchecked")
  @Test
  public void testAuthenticate() throws Exception {
    final MvcResult result = mockMvc
        .perform(post(Route.API_AUTHENTICATE).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("app_key", appBean.getAppKey())
            .param("app_sk", appBean.getSecretKey())
            .param("interval", "30"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.code", is(ApiCode.Success.value())))
        .andExpect(jsonPath("$.body.success", is(true))).andDo(print()).andReturn();
    final String content = result.getResponse().getContentAsString();
    final ApiResponse response = om.readValue(content, ApiResponse.class);
    final HashMap<String, Object> data = (HashMap<String, Object>) response.getBody().getData();
    Assert.assertNotNull(data.get("accessToken"));
    Assert.assertNotNull(data.get("expiry"));
  }

}
