
package com.demo.plugins;

import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;

import com.demo.plugins.persist.MyTbl1Repository;
import com.demo.plugins.persist.model.MyTbl1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 示例插件，将插件访问时间插入数据库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 3:51:07 PM
 */
@Component
@Pluggable(name = "hello", version = "1.0.0", description = "test only",
    readme = "http://www.baidu.com")
public class Hello implements PluginLifeCycleAware {

  private static final Logger logger = LoggerFactory.getLogger(Hello.class);

  @Autowired
  private MyTbl1Repository tbl1Repo;

  /**
   * 求和。
   *
   * @param request HTTP请求
   * @param response HTTP响应
   * @return 和
   * @author hankai
   * @since Oct 25, 2016 1:04:07 PM
   */
  @Functional(name = "add", resultType = "text/plain")
  public String add(HttpServletRequest request, HttpServletResponse response) {
    MyTbl1 mt = new MyTbl1();
    mt.setName(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    tbl1Repo.save(mt);
    logger.warn("feature \"add\" called");
    int op1 = Integer.parseInt(request.getParameter("op1"));
    int op2 = Integer.parseInt(request.getParameter("op2"));
    return op1 + op2 + "";
  }

  @Override
  public void pluginDidActivated() {
    // TODO Auto-generated method stub
  }

  @Override
  public void pluginDidDeactivated() {
    // TODO Auto-generated method stub
  }

  @Override
  public void pluginDidLoad() {
    // TODO Auto-generated method stub
  }

  @Override
  public void pluginDidUnload() {
    // TODO Auto-generated method stub
  }
}
