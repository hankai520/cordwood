package ren.hankai.cordwood.console.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前台网站用户认证控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 3, 2016 11:23:14 AM
 */
@Controller
public class DemoController {

  @GetMapping("/home/demo")
  @ResponseBody
  public String deleteme() {
    return "demo only";
  }

}
