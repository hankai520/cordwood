
package ren.hankai.cordwood.console.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
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
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.model.UserBean.UserStatus;
import ren.hankai.cordwood.console.persist.util.PageUtil;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.console.service.UserService;
import ren.hankai.cordwood.console.view.model.BootstrapTableData;
import ren.hankai.cordwood.console.view.model.PluginRequestStatistics;
import ren.hankai.cordwood.console.view.model.SummarizedRequest;
import ren.hankai.cordwood.core.util.DateUtil;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

/**
 * 用户信息控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2016 8:51:34 AM
 */
@Controller
public class UserController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;
  @Autowired
  private PluginService pluginService;

  /**
   * 显示当前登录的用户的账号信息。
   *
   * @return 账号信息页面
   * @author hankai
   * @since Dec 26, 2016 11:12:42 AM
   */
  @NavigationItem(label = "nav.my.account")
  @GetMapping(Route.BG_MY_ACCOUNT)
  public ModelAndView myAccount() {
    final ModelAndView mav = new ModelAndView("admin_my_account.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    mav.addObject("user", user);
    final Date[] dateRange = DateUtil.thisMonth();
    final PluginRequestStatistics statistics =
        pluginService.getUserPluginStatistics(user.getEmail(), dateRange[0], dateRange[1]);
    mav.addObject("statistics", statistics);
    return mav;
  }

  /**
   * 获取当前登录的用户所开发的插件被访问的统计信息。
   *
   * @return 插件访问统计信息
   * @author hankai
   * @since Dec 26, 2016 11:12:22 AM
   */
  @RequestMapping(Route.BG_MY_PLUGIN_STATS)
  @ResponseBody
  public List<SummarizedRequest> myPluginStatistics() {
    try {
      final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      final UserBean user = (UserBean) auth.getPrincipal();
      final Date[] dateRange = DateUtil.thisMonth();
      final List<SummarizedRequest> list = pluginService
          .getUserPluginSummarizedRequests(user.getEmail(), dateRange[0], dateRange[1]);
      return list;
    } catch (final Exception ex) {
      logger.warn(Route.BG_MY_PLUGIN_STATS, ex);
    } catch (final Error ex) {
      logger.warn(Route.BG_MY_PLUGIN_STATS, ex);
    }
    return null;
  }

  /**
   * 显示当前登录的用户所开发的插件包。
   *
   * @return 插件列表页面
   * @author hankai
   * @since Dec 26, 2016 11:12:00 AM
   */
  @NavigationItem(label = "nav.my.plugins", parent = "nav.my.account")
  @GetMapping(Route.BG_MY_PLUGIN_PACKAGES)
  public ModelAndView myPluginPackages() {
    final ModelAndView mav = new ModelAndView("admin_my_plugin_packages.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    final List<PluginPackageBean> list = pluginService.getInstalledPackages(user.getEmail());
    mav.addObject("packages", list);
    return mav;
  }

  @NavigationItem(label = "nav.my.plugin.logs", parent = "nav.my.account")
  @GetMapping(Route.BG_MY_PLUGIN_LOGS)
  public ModelAndView myPluginLogs() {
    return new ModelAndView("admin_my_plugin_logs.html");
  }

  /**
   * 获取当前登录用户所开发的插件的访问日志。
   *
   * @param search 搜索关键字
   * @param order 排序字段
   * @param sort 升序/降序
   * @param limit 返回结果数
   * @param offset 从第几条开始返回
   * @return 访问日志JSON
   * @author hankai
   * @since Dec 26, 2016 11:10:59 AM
   */
  @RequestMapping(Route.BG_MY_PLUGIN_LOGS_JSON)
  @ResponseBody
  public BootstrapTableData getMyPluginLogsJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      final UserBean user = (UserBean) auth.getPrincipal();
      final Page<PluginRequestBean> logs =
          pluginService.searchPluginRequests(user.getEmail(), search, pageable);
      response = new BootstrapTableData();
      response.setTotal(logs.getTotalElements());
      response.setRows(logs.getContent());
    } catch (final Exception ex) {
      logger.error(Route.BG_MY_PLUGIN_LOGS_JSON, ex);
    } catch (final Error ex) {
      logger.error(Route.BG_MY_PLUGIN_LOGS_JSON, ex);
    }
    return response;
  }

  /**
   * 用户个人资料页面。
   *
   * @return 用户个人资料页面
   * @author hankai
   * @since Dec 26, 2016 11:10:51 AM
   */
  @NavigationItem(label = "nav.my.profile", parent = "nav.my.account")
  @GetMapping(Route.BG_MY_PROFILE)
  public ModelAndView myProfile() {
    final ModelAndView mav = new ModelAndView("admin_my_profile.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    mav.addObject("user", user);
    return mav;
  }

  /**
   * 更新当前登录用户的信息。
   *
   * @param user 用户信息
   * @param br 错误
   * @return 用户个人资料页面
   * @author hankai
   * @since Dec 26, 2016 11:09:39 AM
   */
  @NavigationItem(label = "nav.my.profile", parent = "nav.my.account")
  @PostMapping(Route.BG_MY_PROFILE)
  public ModelAndView updateMyProfile(@ModelAttribute("user") @Valid UserBean user,
      BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_my_profile.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean me = (UserBean) auth.getPrincipal();
    if (!br.hasErrors()) {
      me.setAvatarBase64Data(user.getAvatarBase64Data());
      me.setEmail(user.getEmail());
      me.setMobile(user.getMobile());
      me.setName(user.getName());
      me.setUpdateTime(new Date());
      try {
        user = userService.updateUserInfo(me);
        operationSucceeded(mav);
      } catch (final Exception ex) {
        logger.error(Route.BG_MY_PROFILE, ex);
        operationFailed(mav);
      }
    }
    mav.addObject("user", user);
    return mav;
  }

  /**
   * 获取当前登录用户的头像。
   *
   * @return 头像图片
   * @author hankai
   * @since Dec 26, 2016 11:09:21 AM
   */
  @GetMapping(Route.BG_MY_AVATAR)
  @ResponseBody
  public ResponseEntity<FileSystemResource> getMyAvatar() {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean me = (UserBean) auth.getPrincipal();
    File avatar = userService.getUserAvatar(me);
    if (avatar == null) {
      try {
        avatar = ResourceUtils.getFile("classpath:static/images/default_avatar.jpg");
      } catch (final FileNotFoundException ex) {
        logger.warn("Failed to get default avatar file.", ex);
      }
    }
    if (avatar != null) {
      return new ResponseEntity<>(new FileSystemResource(avatar), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @NavigationItem(label = "nav.users")
  @GetMapping(Route.BG_USERS)
  public ModelAndView users() {
    return new ModelAndView("admin_users.html");
  }

  /**
   * 获取用户列表AJAX接口。
   *
   * @param search 搜索关键字
   * @param order 排序字段
   * @param sort 升序/降序
   * @param limit 返回结果数
   * @param offset 从第几条开始返回
   * @return 用户列表JSON
   * @author hankai
   * @since Dec 26, 2016 11:08:41 AM
   */
  @RequestMapping(Route.BG_USERS_JSON)
  @ResponseBody
  public BootstrapTableData getUsersJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort, @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Page<UserBean> users = userService.search(search, pageable);
      if (users.hasContent()) {
        for (final UserBean user : users.getContent()) {
          user.setStatusName(messageSource.getMessage(user.getStatus().i18nKey(true), null, null));
        }
      }
      response = new BootstrapTableData();
      response.setTotal(users.getTotalElements());
      response.setRows(users.getContent());
    } catch (final Exception ex) {
      logger.error(Route.BG_USERS_JSON, ex);
    } catch (final Error ex) {
      logger.error(Route.BG_USERS_JSON, ex);
    }
    return response;
  }

  /**
   * 添加用户页面。
   *
   * @return 添加用户页面
   * @author hankai
   * @since Dec 26, 2016 11:08:31 AM
   */
  @NavigationItem(label = "nav.users.add", parent = "nav.users")
  @GetMapping(Route.BG_ADD_USER)
  public ModelAndView addUserForm() {
    final ModelAndView mav = new ModelAndView("admin_add_user.html");
    mav.addObject("user", new UserBean());
    mav.addObject("roles", userService.getAvailableRoles());
    return mav;
  }

  /**
   * 添加用户。
   *
   * @param user 用户信息
   * @param br 错误
   * @return 添加用户页面
   * @author hankai
   * @since Dec 26, 2016 11:08:12 AM
   */
  @NavigationItem(label = "nav.users.add", parent = "nav.users")
  @PostMapping(Route.BG_ADD_USER)
  public ModelAndView addUser(@ModelAttribute("user") @Valid UserBean user, BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_add_user.html");
    if (userService.isEmailDuplicated(user, false)) {
      br.rejectValue("email", "Duplicate.user.email");
    }
    if (userService.isMobileDuplicated(user, false)) {
      br.rejectValue("mobile", "Duplicate.user.mobile");
    }
    if ((user.getSelectedRoleIds() == null) || (user.getSelectedRoleIds().size() == 0)) {
      br.rejectValue("selectedRoleIds", "NotEmpty.user.selectedRoleIds");
    }
    if (!br.hasErrors()) {
      try {
        user.updateRolesWithSelectedIds();
        user.setStatus(UserStatus.Enabled);
        user.setCreateTime(new Date());
        userService.save(user);
        user = new UserBean();
        operationSucceeded(mav);
      } catch (final Exception ex) {
        operationFailed(mav);
        logger.error(Route.BG_ADD_USER, ex);
      }
    } else {
      user.setPassword(null);
    }
    mav.addObject("user", user);
    mav.addObject("roles", userService.getAvailableRoles());
    return mav;
  }

  /**
   * 编辑用户页面。
   *
   * @param uid 用户ID
   * @return 编辑用户页面
   * @author hankai
   * @since Dec 26, 2016 11:07:57 AM
   */
  @NavigationItem(label = "nav.users.edit", parent = "nav.users")
  @GetMapping(Route.BG_EDIT_USER)
  public ModelAndView editUserForm(@PathVariable("user_id") Integer uid) {
    final ModelAndView mav = new ModelAndView("admin_edit_user.html");
    final UserBean user = userService.getUserById(uid);
    if (user != null) {
      user.initSelectedRoleIds();
      mav.addObject("user", user);
      mav.addObject("roles", userService.getAvailableRoles());
    } else {
      mav.setViewName("redirect:/404.html");
    }
    return mav;
  }

  /**
   * 编辑用户。
   *
   * @param uid 用户ID
   * @param user 用户信息
   * @param br 错误
   * @return 编辑用户页面
   * @author hankai
   * @since Dec 26, 2016 11:07:37 AM
   */
  @NavigationItem(label = "nav.users.edit", parent = "nav.users")
  @PostMapping(Route.BG_EDIT_USER)
  public ModelAndView editUser(@PathVariable("user_id") Integer uid,
      @ModelAttribute("user") @Valid UserBean user, BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_edit_user.html");
    user.setId(uid);
    final UserBean existUser = userService.getUserById(uid);
    if (existUser != null) {
      if (userService.isEmailDuplicated(user, true)) {
        br.rejectValue("email", "Duplicate.user.email");
      }
      if (userService.isMobileDuplicated(user, true)) {
        br.rejectValue("mobile", "Duplicate.user.mobile");
      }
      if ((user.getSelectedRoleIds() == null) || (user.getSelectedRoleIds().size() == 0)) {
        br.rejectValue("selectedRoleIds", "NotEmpty.user.selectedRoleIds");
      }
      if (!br.hasErrors()) {
        try {
          user.setUpdateTime(new Date());
          user.setPassword(existUser.getPassword());
          user.setCreateTime(existUser.getCreateTime());
          user.updateRolesWithSelectedIds();
          user = userService.save(user);
          user.initSelectedRoleIds();
          operationSucceeded(mav);
        } catch (final Exception ex) {
          operationFailed(mav);
          logger.error(Route.BG_ADD_USER, ex);
        }
      }
      mav.addObject("user", user);
      mav.addObject("roles", userService.getAvailableRoles());
    } else {
      mav.setViewName("redirect:/404.html");
    }
    return mav;
  }

  /**
   * 删除用户。
   *
   * @param uid 用户ID
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 11:07:22 AM
   */
  @GetMapping(Route.BG_DELETE_USER)
  public ResponseEntity<String> deleteUser(@PathVariable("user_id") Integer uid) {
    final UserBean user = userService.getUserById(uid);
    if (user != null) {
      userService.deleteUser(uid);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
