
package ren.hankai.cordwood.plugin.advance;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ren.hankai.cordwood.plugin.advance.model.MyTbl1;
import ren.hankai.cordwood.plugin.advance.persist.MyTbl1Repository;
import ren.hankai.cordwood.plugin.api.PluginLifeCycleAware;
import ren.hankai.cordwood.plugin.api.PluginResourceLoader;
import ren.hankai.cordwood.plugin.api.annotation.Functional;
import ren.hankai.cordwood.plugin.api.annotation.LightWeight;
import ren.hankai.cordwood.plugin.api.annotation.Optional;
import ren.hankai.cordwood.plugin.api.annotation.Pluggable;
import ren.hankai.cordwood.plugin.api.annotation.Secure;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例插件，将插件访问时间插入数据库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 3:51:07 PM
 */
@Component
@Pluggable(name = AdvancePlugin.NAME, version = "1.0.0")
public class AdvancePlugin implements PluginLifeCycleAware, PluginResourceLoader {

  private static final Logger logger = LoggerFactory.getLogger(AdvancePlugin.class);
  public static final String NAME = "advance";
  @Autowired
  private MyTbl1Repository tbl1Repo;
  @Autowired
  private Configuration freeMarkerConfig;

  private Map<String, Object> buildModel() {
    final Map<String, Object> model = new HashMap<>();
    model.put("pluginBaseUrl", String.format("%s/%s", Pluggable.PLUGIN_BASE_URL, NAME));
    model.put("resourceBaseUrl",
        String.format("%s/%s", Pluggable.PLUGIN_RESOURCE_BASE_URL, NAME));
    return model;
  }

  /**
   * 求和。
   *
   * @param op1 左操作数
   * @param op2 右操作数
   * @param echo 回显信息
   * @return 和
   * @author hankai
   * @since Nov 8, 2016 8:55:38 AM
   */
  @Secure(checkParameterIntegrity = false, checkAccessToken = false)
  @Functional(name = "add", resultType = "text/html")
  public String add(Integer op1, Integer op2, @Optional String echo) {
    final MyTbl1 mt = new MyTbl1();
    mt.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    final int sum = op1 + op2;
    mt.setExp(op1 + " + " + op2 + " = " + sum);
    tbl1Repo.save(mt);
    final Map<String, Object> model = buildModel();
    final List<MyTbl1> results = tbl1Repo.findAll();
    model.put("results", results);
    try {
      final Template template = freeMarkerConfig.getTemplate("content.fm");
      final String tmpl = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
      return tmpl;
    } catch (final Exception ex) {
      logger.error("Failed to render template!", ex);
    }
    return "error";
  }

  /**
   * 求两个数字的差。
   *
   * @param op1 左操作数
   * @param op2 右操作数
   * @return 差
   * @author hankai
   * @since Nov 8, 2016 5:30:30 PM
   */
  @LightWeight
  @Functional(name = "subtract", resultType = "text/plain")
  public String subtract(Integer op1, Integer op2) {
    logger.error("Not from cache");
    return (op1 - op2) + "";
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
    // final URL url = PathUtil.getFileUrlInPluginJar(this.getClass(), "templates");
    logger.info("Plugin \"" + AdvancePlugin.NAME + "\" did load");
  }

  @Override
  public void pluginDidUnload() {
    logger.info("Plugin \"" + AdvancePlugin.NAME + "\" did unload");
  }

  @Override
  public InputStream getResource(String relativeUrl) {
    final URL url = this.getClass().getResource("/static/" + relativeUrl);
    if (url != null) {
      try {
        return url.openStream();
      } catch (final IOException ex) {
        logger.error(String.format("Failed to open resource: \"%s\"", relativeUrl), ex);
      }
    }
    return null;
  }
}
