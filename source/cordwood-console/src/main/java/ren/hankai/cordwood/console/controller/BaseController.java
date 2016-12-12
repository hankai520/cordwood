
package ren.hankai.cordwood.console.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.WebConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器基类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2016 8:00:08 PM
 */
public abstract class BaseController {

  private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

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
    mav.addObject(WebConfig.WEB_PAGE_MESSAGE,
        messageSource.getMessage("operation.failed", null, null));
  }

  /**
   * 处理控制器中产生的异常。
   *
   * @param request HTTP 请求
   * @param exception 异常信息
   * @return 错误页面
   * @author hankai
   * @since Dec 7, 2016 3:45:39 PM
   */
  @ExceptionHandler(Exception.class)
  public ModelAndView handleException(HttpServletRequest request, Exception exception) {
    final ModelAndView mav = new ModelAndView("error.html");
    mav.addObject("exception", exception);
    mav.addObject("errors", exception.toString());
    logger.warn(request.getRequestURL().toString(), exception);
    return mav;
  }

}
