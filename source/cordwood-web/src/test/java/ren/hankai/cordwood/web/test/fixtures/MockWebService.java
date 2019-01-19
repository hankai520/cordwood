/*******************************************************************************
 * Copyright (C) 2019 hankai
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

package ren.hankai.cordwood.web.test.fixtures;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.web.api.exception.AjaxException;
import ren.hankai.cordwood.web.api.exception.ApiException;
import ren.hankai.cordwood.web.api.support.WebServiceSupport;
import ren.hankai.cordwood.web.security.annotation.Stabilized;
import ren.hankai.cordwood.web.test.config.Route;

/**
 * 仅用于单元测试的 Web Service。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:36:21 PM
 */
@Controller
@Stabilized(maxQps = 1, fusingThreshold = 1, fusingInterval = 5)
@Profile(Preferences.PROFILE_TEST)
public class MockWebService extends WebServiceSupport {

  @RequestMapping(Route.S1)
  public String s1() throws Exception {
    throw new RuntimeException("expected");
  }

  @RequestMapping(Route.S2)
  public String s2() throws Exception {
    throw new ApiException("-1", "expected");
  }

  @RequestMapping(Route.S3)
  public String s3() throws Exception {
    throw new AjaxException("-2", "expected");
  }

  @Stabilized(maxQps = 1, fusingThreshold = 1, fusingInterval = 5)
  @RequestMapping(Route.S5_1)
  public String s5_1() throws Exception {
    return "";
  }

  @RequestMapping(Route.S5_2)
  public String s5_2() throws Exception {
    return "";
  }
}
