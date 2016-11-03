package ren.hankai.cordwood.console.controller.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;

/**
 * 仪表盘。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 5:16:07 PM
 */
@Controller
public class DashboardController {

  @RequestMapping(Route.BACKGROUND_PREFIX)
  public ModelAndView index() {
    return new ModelAndView("admin/dashboard");
  }

}
