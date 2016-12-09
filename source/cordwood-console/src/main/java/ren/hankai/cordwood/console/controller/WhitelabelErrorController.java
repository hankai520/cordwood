package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 白板错误页面控制器，一般用于显示通过了身份认证的用户在使用过程中产生的系统错误。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 1, 2016 11:14:31 AM
 */
@Controller
public class WhitelabelErrorController implements ErrorController {
  private static final String PATH = "/error";

  @Value("${debug}")
  private boolean debug;

  @Autowired
  private ErrorAttributes errorAttributes;

  /**
   * 处理 HTTP 错误。
   *
   * @param request HTTP请求
   * @param response HTTP响应
   * @return 错误页面
   * @author hankai
   * @since Nov 8, 2016 8:47:46 AM
   */
  @RequestMapping(value = PATH)
  public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {
    final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
    final Map<String, Object> attrs = errorAttributes.getErrorAttributes(requestAttributes, debug);
    final ModelAndView mav = new ModelAndView("error.html");
    mav.addObject("errors", attrs);
    return mav;
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }
}