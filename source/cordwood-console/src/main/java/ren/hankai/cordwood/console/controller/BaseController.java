
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

  /**
   * 添加操作成功的提示消息。
   *
   * @param mav 视图模型
   * @author hankai
   * @since Dec 7, 2016 3:46:05 PM
   */
  protected void operationSucceeded(ModelAndView mav) {
    mav.addObject(WebConfig.WEB_PAGE_MESSAGE,
        messageSource.getMessage("operation.succeeded", null, null));
  }

  /**
   * 添加操作失败的提示消息。
   *
   * @param mav 视图模型
   * @author hankai
   * @since Dec 7, 2016 3:46:29 PM
   */
  protected void operationFailed(ModelAndView mav) {
    mav.addObject(WebConfig.WEB_PAGE_ERROR,
        messageSource.getMessage("operation.failed", null, null));
  }
}
