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
