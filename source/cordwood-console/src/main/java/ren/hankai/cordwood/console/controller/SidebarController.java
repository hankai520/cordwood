package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.model.SidebarItemBean;
import ren.hankai.cordwood.console.service.SidebarService;

import java.util.List;

/**
 * 边栏菜单控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 1, 2016 1:22:05 PM
 */
@Controller
public class SidebarController {

  @Autowired
  private SidebarService sidebarService;

  /**
   * 获取边栏菜单项JSON。
   *
   * @return 边栏菜单内容
   * @author hankai
   * @since Nov 2, 2016 10:37:29 AM
   */
  @GetMapping(Route.BG_SIDEBAR_JSON)
  @ResponseBody
  public List<SidebarItemBean> sidebar() {
    final List<SidebarItemBean> items = sidebarService.getAvailableBarItems();
    return items;
  }

}
