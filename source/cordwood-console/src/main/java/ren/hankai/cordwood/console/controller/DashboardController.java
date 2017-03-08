package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.console.view.model.DashboardData;
import ren.hankai.cordwood.core.util.OsUtil;
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

  @Autowired
  private PluginService pluginService;

  @GetMapping(Route.BACKGROUND_PREFIX)
  public String redirectToIndex() {
    return "redirect:" + Route.BG_DASHBOARD;
  }

  /**
   * 仪表盘首页。
   *
   * @return 模型视图
   * @author hankai
   * @since Mar 8, 2017 10:14:15 PM
   */
  @NavigationItem(label = "nav.dashboard")
  @GetMapping(Route.BG_DASHBOARD)
  public ModelAndView index() {
    final ModelAndView mav = new ModelAndView("admin_dashboard.html");
    final DashboardData dd = new DashboardData();
    dd.setNumberOfPlugins(pluginService.getNumberOfPlugins());
    dd.setNumberOfDevelopers(pluginService.getNumberOfDevelopers());
    dd.setResponseTime(pluginService.getResponseTimeAverage());
    dd.setFaultRate(pluginService.getRequestFaultRate());
    dd.setTotalMemory(OsUtil.getSystemTotalRamSize());
    dd.setUsedMemory(dd.getTotalMemory() - OsUtil.getSystemFreeRamSize());
    dd.setTotalStorage(OsUtil.getSystemTotalHdSize());
    dd.setUsedStorage(dd.getTotalStorage() - OsUtil.getSystemFreeHdSize());
    dd.setDataVolume(pluginService.getDataVolumeDailyAvg());
    mav.addObject("data", dd);
    return mav;
  }
}
