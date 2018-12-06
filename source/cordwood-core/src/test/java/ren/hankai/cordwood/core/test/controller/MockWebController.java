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
package ren.hankai.cordwood.core.test.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.test.Route;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;
import ren.hankai.cordwood.web.exception.NotFoundException;
import ren.hankai.cordwood.web.support.BaseViewController;

/**
 * 仅用于单元测试的控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:36:21 PM
 */
@Controller
@Profile(Preferences.PROFILE_TEST)
public class MockWebController extends BaseViewController {

  @Override
  protected String getNotFoundViewName() {
    return "redirect:/404";
  }

  @Override
  protected String getErrorViewName() {
    return "redirect:/error";
  }

  /**
   * 内部抛出不同异常，辅助测试视图控制器基类的错误处理功能。
   *
   * @param p 错误指示参数
   * @return 视图
   * @throws Exception 异常
   * @author hankai
   * @since Nov 23, 2018 5:24:31 PM
   */
  @RequestMapping(Route.S4)
  public String s4(@RequestParam("p") String p) throws Exception {
    if ("1".equals(p)) {
      throw new NotFoundException("expected", null);
    }
    throw new RuntimeException("expected");
  }

  @NavigationItem(family = "test", label = "s6_1")
  @RequestMapping(Route.S6_1)
  public String s6_1() throws Exception {
    return "";
  }

  @NavigationItem(family = "test", label = "s6_2", parent = "s6_1")
  @RequestMapping(Route.S6_2)
  public String s6_2() throws Exception {
    return "";
  }
}
