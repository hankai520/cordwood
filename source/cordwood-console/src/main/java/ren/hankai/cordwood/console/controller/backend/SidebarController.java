package ren.hankai.cordwood.console.controller.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.SidebarRepository;

import java.util.ArrayList;
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

  private static final Logger logger = LoggerFactory.getLogger(SidebarController.class);

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SidebarRepository sidebarRepository;

  /**
   * 渲染边栏菜单。
   *
   * @return 边栏菜单内容
   * @author hankai
   * @since Nov 2, 2016 10:37:29 AM
   */
  @GetMapping(Route.BG_SIDEBAR_JS)
  public ModelAndView sidebar() {
    final ModelAndView mav = new ModelAndView("admin_sidebar.js");
    final List<String> items = new ArrayList<>();
    items.add("dashboard");
    items.add("plugins");
    items.add("users");
    try {
      final String json = objectMapper.writeValueAsString(items);
      mav.addObject("visibleItems", json);
    } catch (final JsonProcessingException e) {
      logger.error("Failed to generate json for sidebar items.", e);
    }
    mav.addObject("selectedItem", "plugins");
    return mav;
  }

}
