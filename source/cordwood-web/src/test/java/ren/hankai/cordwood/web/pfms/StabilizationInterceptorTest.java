
package ren.hankai.cordwood.web.pfms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import ren.hankai.cordwood.web.test.WebTestSupport;
import ren.hankai.cordwood.web.test.config.Route;

/**
 * 稳定器测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2018 1:47:25 PM
 */
public class StabilizationInterceptorTest extends WebTestSupport {

  @Test
  public void testStabilizationMaxQps() throws Exception {
    mockMvc
        .perform(
            post(Route.S5_1).contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk());
  }

}
