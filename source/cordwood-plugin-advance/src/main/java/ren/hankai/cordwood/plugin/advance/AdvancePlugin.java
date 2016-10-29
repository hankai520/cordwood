
package ren.hankai.cordwood.plugin.advance;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.EscapeHtmlReference;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.plugin.advance.model.MyTbl1;
import ren.hankai.cordwood.plugin.advance.persist.MyTbl1Repository;
import ren.hankai.cordwood.plugin.advance.util.MyLogChute;
import ren.hankai.cordwood.plugin.api.Functional;
import ren.hankai.cordwood.plugin.api.Pluggable;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 示例插件，将插件访问时间插入数据库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 3:51:07 PM
 */
@Component
@Pluggable(name = AdvancePlugin.NAME, version = "1.0.0", description = "advance plugin")
public class AdvancePlugin implements PluginLifeCycleAware, PluginResourceLoader {

  private static final Logger logger = LoggerFactory.getLogger(AdvancePlugin.class);

  public static final String NAME = "advance";

  private static VelocityEngine engine = null;

  @Autowired
  private MyTbl1Repository tbl1Repo;

  private VelocityContext buildVelcityContext() {
    VelocityContext vc = new VelocityContext();
    vc.put("pluginBaseUrl", String.format("%s/%s", Pluggable.PLUGIN_BASE_URL, NAME));
    vc.put("resourceBaseUrl", String.format("%s/%s", Pluggable.PLUGIN_RESOURCE_BASE_URL, NAME));
    return vc;
  }

  /**
   * 求和。
   *
   * @param request HTTP请求
   * @param response HTTP响应
   * @return 和
   * @author hankai
   * @since Oct 25, 2016 1:04:07 PM
   */
  @Functional(name = "add", resultType = "text/html")
  public String add(Integer op1, Integer op2) {
    MyTbl1 mt = new MyTbl1();
    mt.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    int sum = op1 + op2;
    mt.setExp(op1 + " + " + op2 + " = " + sum);
    tbl1Repo.save(mt);
    VelocityContext vc = buildVelcityContext();
    List<MyTbl1> results = tbl1Repo.findAll();
    vc.put("results", results);
    Template tmpl = null;
    try {
      tmpl = engine.getTemplate("/content.html", "utf-8");
      StringWriter sw = new StringWriter();
      tmpl.merge(vc, sw);
      return sw.toString();
    } catch (Exception e) {
      logger.error("Failed to render template!", e);
    }
    return "error";
  }

  @Override
  public void pluginDidActivated() {
    logger.info("Plugin \"" + AdvancePlugin.NAME + "\" activated");
  }

  @Override
  public void pluginDidDeactivated() {
    logger.info("Plugin \"" + AdvancePlugin.NAME + "\" deactivated");
  }

  @Override
  public void pluginDidLoad() {
    try {
      URL url = AdvancePlugin.class.getClassLoader().getResource("templates");
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
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,
            new MyLogChute(AdvancePlugin.NAME));
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
    logger.info("Plugin \"" + AdvancePlugin.NAME + "\" did unload");
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
