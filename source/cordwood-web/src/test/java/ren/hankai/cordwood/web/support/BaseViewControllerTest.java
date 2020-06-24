
package ren.hankai.cordwood.web.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ren.hankai.cordwood.web.test.WebTestSupport;
import ren.hankai.cordwood.web.test.config.Route;

public class BaseViewControllerTest extends WebTestSupport {

  @Test
  public void testHandleException() throws Exception {
    mockMvc
        .perform(
            post(Route.S4).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("p", "1"))
        .andExpect(status().is(HttpStatus.FOUND.value()))
        .andExpect(redirectedUrl("/404"))
        .andDo(print());

    mockMvc
        .perform(
            post(Route.S4).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("p", "2"))
        .andExpect(status().is(HttpStatus.FOUND.value()))
        .andExpect(redirectedUrl("/error"))
        .andDo(print());
  }

}
