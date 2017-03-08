
package ren.hankai.cordwood.console.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.model.AppBean;
import ren.hankai.cordwood.console.persist.model.AppBean.AppPlatform;
import ren.hankai.cordwood.console.persist.model.AppBean.AppStatus;
import ren.hankai.cordwood.console.persist.util.PageUtil;
import ren.hankai.cordwood.console.service.AppService;
import ren.hankai.cordwood.console.view.model.BootstrapTableData;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;

import java.util.Date;

import javax.validation.Valid;

/**
 * 应用管理控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 23, 2016 11:28:42 AM
 */
@Controller
public class AppController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(AppController.class);

  @Autowired
  private AppService appService;

  @NavigationItem(label = "nav.apps")
  @GetMapping(Route.BG_APPS)
  public ModelAndView apps() {
    return new ModelAndView("admin_apps.html");
  }

  /**
   * 获取应用列表AJAX接口。
   * 
   * @param search 搜索关键字
   * @param order 排序字段
   * @param sort 升序/降序
   * @param limit 返回结果数
   * @param offset 从第几条开始返回
   * @return 应用列表JSON
   * @author hankai
   * @since Dec 26, 2016 11:00:30 AM
   */
  @RequestMapping(Route.BG_APPS_JSON)
  @ResponseBody
  public BootstrapTableData getAppsJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort, @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Page<AppBean> apps = appService.search(search, pageable);
      if (apps.hasContent()) {
        for (final AppBean app : apps.getContent()) {
          app.setStatusName(messageSource.getMessage(app.getStatus().i18nKey(true), null, null));
          app.setPlatformName(
              messageSource.getMessage(app.getPlatform().i18nKey(true), null, null));
        }
      }
      response = new BootstrapTableData();
      response.setTotal(apps.getTotalElements());
      response.setRows(apps.getContent());
    } catch (final Exception ex) {
      logger.error(Route.BG_APPS_JSON, ex);
    } catch (final Error ex) {
      logger.error(Route.BG_APPS_JSON, ex);
    }
    return response;
  }

  /**
   * 添加应用页面。
   * 
   * @return 添加应用页面
   * @author hankai
   * @since Dec 26, 2016 11:00:14 AM
   */
  @NavigationItem(label = "nav.apps.add", parent = "nav.apps")
  @GetMapping(Route.BG_ADD_APP)
  public ModelAndView addAppForm() {
    final ModelAndView mav = new ModelAndView("admin_add_app.html");
    final AppBean app = new AppBean();
    app.setPlatform(AppPlatform.Android);
    app.setAppKey(appService.generateAppKey());
    app.setSecretKey(appService.generateSecretKey());
    mav.addObject("app", app);
    return mav;
  }

  /**
   * 添加应用。
   * 
   * @param app 应用信息
   * @param br 错误
   * @return 添加应用页面
   * @author hankai
   * @since Dec 26, 2016 10:59:57 AM
   */
  @NavigationItem(label = "nav.apps.add", parent = "nav.apps")
  @PostMapping(Route.BG_ADD_APP)
  public ModelAndView addApp(@ModelAttribute("app") @Valid AppBean app, BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_add_app.html");
    if (appService.isNameDuplicated(app, false)) {
      br.rejectValue("name", "Duplicate.app.name");
    }
    if (!br.hasErrors()) {
      try {
        app.setStatus(AppStatus.Enabled);
        app.setCreateTime(new Date());
        appService.save(app);
        app = new AppBean();
        app.setAppKey(appService.generateAppKey());
        app.setSecretKey(appService.generateSecretKey());
        operationSucceeded(mav);
      } catch (final Exception ex) {
        operationFailed(mav);
        logger.error(Route.BG_ADD_APP, ex);
      }
    }
    mav.addObject("app", app);
    return mav;
  }

  /**
   * 编辑应用页面。
   * 
   * @param appId 应用ID
   * @return 编辑应用页面
   * @author hankai
   * @since Dec 26, 2016 10:59:37 AM
   */
  @NavigationItem(label = "nav.apps.edit", parent = "nav.apps")
  @GetMapping(Route.BG_EDIT_APP)
  public ModelAndView editAppForm(@PathVariable("app_id") Integer appId) {
    final ModelAndView mav = new ModelAndView("admin_edit_app.html");
    final AppBean app = appService.getAppById(appId);
    if (app != null) {
      mav.addObject("app", app);
    } else {
      mav.setViewName("redirect:/404.html");
    }
    return mav;
  }

  /**
   * 编辑应用信息。
   * 
   * @param appId 应用ID
   * @param app 应用信息
   * @param br 错误
   * @return 编辑应用页面
   * @author hankai
   * @since Dec 26, 2016 10:59:00 AM
   */
  @NavigationItem(label = "nav.apps.edit", parent = "nav.apps")
  @PostMapping(Route.BG_EDIT_APP)
  public ModelAndView editApp(@PathVariable("app_id") Integer appId,
      @ModelAttribute("app") @Valid AppBean app, BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_edit_app.html");
    app.setId(appId);
    final AppBean existApp = appService.getAppById(appId);
    if (existApp != null) {
      if (appService.isNameDuplicated(app, true)) {
        br.rejectValue("name", "Duplicate.app.name");
      }
      if (!br.hasErrors()) {
        try {
          app.setCreateTime(existApp.getCreateTime());
          app.setAppKey(existApp.getAppKey());
          app.setSecretKey(existApp.getSecretKey());
          app.setUpdateTime(new Date());
          app = appService.save(app);
          operationSucceeded(mav);
        } catch (final Exception ex) {
          operationFailed(mav);
          logger.error(Route.BG_EDIT_APP, ex);
        }
      }
      mav.addObject("app", app);
    } else {
      mav.setViewName("redirect:/404.html");
    }
    return mav;
  }

  /**
   * 删除应用。
   *
   * @param appId 应用ID
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 10:58:30 AM
   */
  @GetMapping(Route.BG_DELETE_APP)
  public ResponseEntity<String> deleteApp(@PathVariable("app_id") Integer appId) {
    final AppBean app = appService.getAppById(appId);
    if (app != null) {
      appService.deleteAppById(appId);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
