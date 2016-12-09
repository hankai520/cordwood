
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
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.model.UserBean.UserStatus;
import ren.hankai.cordwood.console.persist.util.PageUtil;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.console.service.UserService;
import ren.hankai.cordwood.console.view.model.BootstrapTableData;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

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

  @NavigationItem(label = "nav.my.account")
  @GetMapping(Route.BG_MY_ACCOUNT)
  public ModelAndView myAccount() {
    final ModelAndView mav = new ModelAndView("admin_my_account.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    mav.addObject("user", user);
    final long pluginAccesses = pluginService.getUserPluginAccessCount(user.getEmail());
    mav.addObject("pluginAccesses", pluginAccesses);
    final float timeUsageAvg = pluginService.getUserPluginTimeAverage(user.getEmail());
    mav.addObject("pluginTimeUsageAvg", timeUsageAvg);
    final float faultRate = pluginService.getUserPluginFaultRate(user.getEmail());
    mav.addObject("pluginFaultRate", faultRate * 100);
    final float pluginUsage = pluginService.getUserPluginUsage(user.getEmail());
    mav.addObject("pluginUsage", pluginUsage * 100);
    final float pluginDataShare = pluginService.getUserPluginDataShare(user.getEmail());
    mav.addObject("pluginDataShare", pluginDataShare * 100);
    return mav;
  }

  @NavigationItem(label = "nav.my.profile", parent = "nav.my.account")
  @GetMapping(Route.BG_MY_PROFILE)
  public ModelAndView myProfile() {
    final ModelAndView mav = new ModelAndView("admin_my_profile.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    mav.addObject("user", user);
    return mav;
  }

  @PostMapping(Route.BG_MY_PROFILE)
  public ModelAndView updateMyProfile(@ModelAttribute("user") @Valid UserBean user,
      BindingResult br) {
    final ModelAndView mav = new ModelAndView("admin_my_profile.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean me = (UserBean) auth.getPrincipal();
    if (!br.hasErrors()) {
      me.setEmail(user.getEmail());
      me.setMobile(user.getMobile());
      me.setName(user.getName());
      me.setUpdateTime(new Date());
      try {
        user = userService.updateUserInfo(me);
        operationSucceeded(mav);
      } catch (final Exception e) {
        logger.error(Route.BG_MY_PROFILE, e);
        operationFailed(mav);
      }
    }
    mav.addObject("user", user);
    return mav;
  }

  @GetMapping(Route.BG_MY_AVATAR)
  @ResponseBody
  public ResponseEntity<FileSystemResource> getMyAvatar() {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean me = (UserBean) auth.getPrincipal();
    File avatar = userService.getUserAvatar(me);
    if (avatar == null) {
      try {
        avatar = ResourceUtils.getFile("classpath:static/images/default_avatar.jpg");
      } catch (final FileNotFoundException e) {
        logger.warn("Failed to get default avatar file.", e);
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
    } catch (final Exception e) {
      logger.error(Route.BG_USERS_JSON, e);
    } catch (final Error e) {
      logger.error(Route.BG_USERS_JSON, e);
    }
    return response;
  }

  @NavigationItem(label = "nav.users.add", parent = "nav.users")
  @GetMapping(Route.BG_ADD_USER)
  public ModelAndView addUserForm() {
    final ModelAndView mav = new ModelAndView("admin_add_user.html");
    mav.addObject("user", new UserBean());
    mav.addObject("roles", userService.getAvailableRoles());
    return mav;
  }

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
      } catch (final Exception e) {
        operationFailed(mav);
        logger.error(Route.BG_ADD_USER, e);
      }
    } else {
      user.setPassword(null);
    }
    mav.addObject("user", user);
    mav.addObject("roles", userService.getAvailableRoles());
    return mav;
  }

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
        } catch (final Exception e) {
          operationFailed(mav);
          logger.error(Route.BG_ADD_USER, e);
        }
      }
      mav.addObject("user", user);
      mav.addObject("roles", userService.getAvailableRoles());
    } else {
      mav.setViewName("redirect:/404.html");
    }
    return mav;
  }

  @GetMapping(Route.BG_DELETE_USER)
  public ResponseEntity<String> deleteUser(@PathVariable("user_id") Integer uid) {
    final UserBean user = userService.getUserById(uid);
    if (user != null) {
      userService.deleteUser(uid);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}