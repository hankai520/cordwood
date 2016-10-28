
package ren.hankai.cordwood.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

/**
 * 默认控制器。
 *
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:19:57 AM
 */
@Controller
public class DefaultController {

  /**
   * 控制台首页。
   * 
   * @param session 会话
   * @return 页面内容
   * @throws Exception 异常
   * @author hankai
   * @since Oct 28, 2016 11:25:31 AM
   */
  @RequestMapping("/")
  @ResponseBody
  public String home(HttpSession session) throws Exception {
    Object obj = session.getAttribute("time");
    if (obj == null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      obj = sdf.format(new Date());
      session.setAttribute("time", obj);
    }
    return (String) obj;
  }
}
