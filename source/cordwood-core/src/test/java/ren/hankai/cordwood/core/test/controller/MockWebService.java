
package ren.hankai.cordwood.core.test.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.api.exception.AjaxException;
import ren.hankai.cordwood.core.api.exception.ApiException;
import ren.hankai.cordwood.core.api.support.WebServiceSupport;
import ren.hankai.cordwood.core.test.Route;
import ren.hankai.cordwood.web.security.annotation.Stabilized;

/**
 * 仅用于单元测试的 Web Service。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:36:21 PM
 */
@Controller
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
  @RequestMapping(Route.S5)
  public String s5() throws Exception {
    return "";
  }
}
