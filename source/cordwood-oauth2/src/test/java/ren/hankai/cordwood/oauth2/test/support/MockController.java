
package ren.hankai.cordwood.oauth2.test.support;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用于单元测试中扮演OAuth2资源的接口。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 28, 2020 10:21:23 AM
 */
@Controller
public class MockController {

  @RequestMapping("/api/echo")
  @ResponseBody
  public String echo(@RequestParam("text") final String text) {
    return text;
  }

}
