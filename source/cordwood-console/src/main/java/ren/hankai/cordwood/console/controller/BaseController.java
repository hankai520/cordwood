
package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.WebConfig;

/**
 * 控制器基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2016 8:00:08 PM
 */
public abstract class BaseController {

  @Autowired
  protected MessageSource messageSource;

  protected void operationSucceeded(ModelAndView mav) {
    mav.addObject(WebConfig.WEB_PAGE_MESSAGE,
        messageSource.getMessage("operation.succeeded", null, null));
  }

  protected void operationFailed(ModelAndView mav) {
    mav.addObject(WebConfig.WEB_PAGE_MESSAGE,
        messageSource.getMessage("operation.failed", null, null));
  }

}
