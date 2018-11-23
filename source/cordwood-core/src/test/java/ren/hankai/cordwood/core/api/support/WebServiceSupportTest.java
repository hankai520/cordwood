
package ren.hankai.cordwood.core.api.support;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import ren.hankai.cordwood.core.test.CoreTestSupport;
import ren.hankai.cordwood.core.test.Route;

public class WebServiceSupportTest extends CoreTestSupport {

  @Test
  public void testHandleInternalError() throws Exception {
    mockMvc
        .perform(
            post(Route.S1).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("p1", "123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(ApiCode.InternalError.value())))
        .andExpect(jsonPath("$.body.success", is(false)))
        .andExpect(jsonPath("$.message", is("expected")))
        .andDo(print());
  }

  @Test
  public void testHandleBusinessError() throws Exception {
    mockMvc
        .perform(
            post(Route.S2).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("p1", "123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(ApiCode.Success.value())))
        .andExpect(jsonPath("$.body.success", is(false)))
        .andExpect(jsonPath("$.body.error", is("-1")))
        .andExpect(jsonPath("$.body.message", is("expected")))
        .andDo(print());

    mockMvc
        .perform(
            post(Route.S3).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("p1", "123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(ApiCode.Success.value())))
        .andExpect(jsonPath("$.body.success", is(false)))
        .andExpect(jsonPath("$.body.error", is("-2")))
        .andExpect(jsonPath("$.body.message", is("expected")))
        .andDo(print());
  }

}
