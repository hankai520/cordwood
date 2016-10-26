
package com.demo.web;

import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.EscapeHtmlReference;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 示例插件。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 18, 2016 9:30:06 AM
 */
@Pluggable(name = DemoWeb.NAME, version = "1.0.0", description = "test only",
    readme = "http://www.baidu.com")
public class DemoWeb implements PluginLifeCycleAware, PluginResourceLoader {

  public static final String NAME = "spring_boot";

  private static final Logger logger = LoggerFactory.getLogger(DemoWeb.class);

  private static VelocityEngine engine = null;

  /**
   * 示例功能。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 网页内容
   * @author hankai
   * @since Oct 25, 2016 1:01:24 PM
   */
  @Functional(name = "hello", resultType = "text/html")
  public String sayHello(HttpServletRequest request, HttpServletResponse response) {
    VelocityContext vc = new VelocityContext();
    vc.put("baseUrl", String.format("/resources/%s", NAME));
    String name = request.getParameter("name");
    vc.put("name", name);
    Template tmpl = null;
    try {
      tmpl = engine.getTemplate("/content.html", "utf-8");
      StringWriter sw = new StringWriter();
      tmpl.merge(vc, sw);
      return sw.toString();
    } catch (Exception e) {
      logger.error("Failed to render template!", e);
    }
    return "";
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
    try {
      URL url = DemoWeb.class.getClassLoader().getResource("templates");
      String templatePath = url.toString();
      logger.info("Velocity template root: " + templatePath);
      if (engine == null) {
        engine = new VelocityEngine();
      }
      engine.setProperty("resource.loader", "url");
      engine.setProperty("url.resource.loader.root", templatePath);
      engine.setProperty("url.resource.loader.class", URLResourceLoader.class.getName());
      engine.setProperty("url.resource.loader.cache", "true");
      engine.setProperty("url.resource.loader.modificationCheckInterval", "60");
      String logDir = System.getProperty("app.log");
      if (!StringUtils.isEmpty(logDir)) {
        logger.info("Velocity run time log: " + logDir);
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new MyLogChute(DemoWeb.NAME));
      }
      // 必须设置该选项，因为velocity在生成html时，不会转义html实体（如 & <），需要通过该配置来自动转义
      engine.setProperty("eventhandler.referenceinsertion.class",
          EscapeHtmlReference.class.getName());
      // 模板中所有符号都原样输出
      engine.setProperty("eventhandler.escape.html.match", "/.*/");
      engine.init();
    } catch (Exception e) {
      logger.error("Failed to initialize velocity!", e);
    }
  }

  @Override
  public void pluginDidUnload() {
    // TODO Auto-generated method stub
  }

  @Override
  public InputStream getResource(String relativeUrl) {
    URL url = this.getClass().getResource("/static/" + relativeUrl);
    if (url != null) {
      try {
        return url.openStream();
      } catch (IOException e) {
        logger.error(String.format("Failed to open resource: \"%s\"", relativeUrl), e);
      }
    }
    return null;
  }
}
