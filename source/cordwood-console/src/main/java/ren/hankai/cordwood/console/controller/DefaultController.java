
package ren.hankai.cordwood.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:19:57 AM
 */
@Controller
public class DefaultController {

  @RequestMapping("/")
  @ResponseBody
  public String home() throws Exception {
    return "hello";
  }
}
