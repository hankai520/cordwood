package ren.hankai.cordwood.console.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 24, 2016 11:28:48 AM
 */
@Controller
public class CustomErrorController implements ErrorController {

  private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

  @Override
  public String getErrorPath() {
    return Route.ERROR_PREFIX;
  }

  /**
   * 处理HTTP错误页面（403, 404等）。
   *
   * @param errorCode http错误代码
   * @return 错误页面
   * @author hankai
   * @since Dec 24, 2016 11:36:55 AM
   */
  @RequestMapping(Route.ERROR_HTTP)
  public ModelAndView handleHttpErrors(@PathVariable("error_code") String errorCode) {
    final ModelAndView mav = new ModelAndView("error.html");
    final String codeRegex = "400|403|404|500";
    if (StringUtils.isNotEmpty(errorCode) && errorCode.matches(codeRegex)) {
      mav.setViewName("error_" + errorCode + ".html");
    }
    return mav;
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
  @ExceptionHandler({Exception.class, Error.class})
  @RequestMapping(Route.ERROR_PREFIX)
  public ModelAndView handleException(HttpServletRequest request, Exception exception) {
    final ModelAndView mav = new ModelAndView();
    if (exception instanceof CookieTheftException) {
      mav.setViewName("redirect:" + Route.BG_LOGIN);
    } else {
      mav.setViewName("error.html");
      mav.addObject("exception", exception);
      mav.addObject("errors", exception.toString());
    }
    logger.warn(request.getRequestURL().toString(), exception);
    return mav;
  }

}
