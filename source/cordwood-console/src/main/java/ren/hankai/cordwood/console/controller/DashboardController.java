package ren.hankai.cordwood.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;

/**
 * 仪表盘。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 5:16:07 PM
 */
@Controller
public class DashboardController extends BaseController {

  @NavigationItem(label = "nav.dashboard")
  @GetMapping({Route.BACKGROUND_PREFIX, Route.BG_DASHBOARD})
  public ModelAndView index() {
    return new ModelAndView("admin_dashboard.html");
  }

}
