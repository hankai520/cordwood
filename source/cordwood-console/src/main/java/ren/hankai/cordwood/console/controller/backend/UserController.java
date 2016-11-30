
package ren.hankai.cordwood.console.controller.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.controller.BaseController;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.service.UserService;

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

  @GetMapping(Route.BG_MY_ACCOUNT)
  public ModelAndView myAccount() {
    final ModelAndView mav = new ModelAndView("admin_my_account.html");
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final UserBean user = (UserBean) auth.getPrincipal();
    mav.addObject("user", user);
    return mav;
  }

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

}
