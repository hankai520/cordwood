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
 * 友好的错误页面控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 1, 2016 11:14:31 AM
 */
@Controller
public class FriendlyErrorController implements ErrorController {
  private static final String PATH = "/error";

  @Value("${debug}")
  private boolean debug;

  @Autowired
  private ErrorAttributes errorAttributes;

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
