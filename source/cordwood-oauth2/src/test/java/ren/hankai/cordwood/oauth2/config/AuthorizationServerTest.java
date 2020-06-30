
package ren.hankai.cordwood.oauth2.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ren.hankai.cordwood.oauth2.test.Oauth2TestSupport;
import ren.hankai.cordwood.oauth2.test.support.MockUserDetailsService;

/**
 * 测试AuthorizationServerBaseConfig配置类是否正确。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2020 10:30:20 AM
 */
public class AuthorizationServerTest extends Oauth2TestSupport {

  @Autowired
  private TokenStore tokenStore;

  @Test
  public void testAuthorizeViaPassword() throws Exception {
    final String tokenString = obtainAccessToken(MockUserDetailsService.USER_NAME, MockUserDetailsService.USER_PWD);
    assertNotNull(tokenString);
    final OAuth2AccessToken token = tokenStore.readAccessToken(tokenString);
    for (final String scope : token.getScope()) {
      assertTrue(super.testClient.getScopes().contains(scope));
    }
    assertEquals(tokenString, token.getValue());
    // 由于生成token本身耗时，到期事件和理论值存在误差，误差小于10s即认为相等
    assertTrue(Math.abs(604800 - token.getExpiresIn()) < 10);
    // 令牌内容的验签在tokenstore内部通过enhancer实现，无需断言
  }

  @Test
  public void testAccessResource() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post("/api/echo")
            .param("text", "notimportant")
            .accept("application/json;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    final String echoString = "hello";
    final String tokenString = obtainAccessToken(MockUserDetailsService.USER_NAME, MockUserDetailsService.USER_PWD);
    final ResultActions result = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/echo")
            .param("text", echoString)
            .header("Authorization", "Bearer " + tokenString)
            .accept("application/json;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
    final String resultString = result.andReturn().getResponse().getContentAsString();
    assertEquals(echoString, resultString);
  }
}
