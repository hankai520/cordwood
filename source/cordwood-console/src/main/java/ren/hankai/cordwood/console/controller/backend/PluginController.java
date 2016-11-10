
package ren.hankai.cordwood.console.controller.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.service.PluginService;

import java.util.List;

/**
 * 插件管理控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 8, 2016 10:07:48 AM
 */
@Controller
public class PluginController {

  private static final Logger logger = LoggerFactory.getLogger(PluginController.class);

  @Autowired
  private PluginService pluginService;

  /**
   * 查看插件集合。
   *
   * @return 插件列表页
   * @author hankai
   * @since Nov 8, 2016 10:14:55 AM
   */
  @GetMapping(Route.BG_PLUGINS)
  public ModelAndView plugins() {
    final ModelAndView mav = new ModelAndView("admin_plugins.html");
    final List<PluginBean> list = pluginService.getInstalledPlugins();
    mav.addObject("plugins", list);
    return mav;
  }

  @GetMapping(Route.BG_PLUGINS_ON)
  @ResponseBody
  public ResponseEntity<String> enablePlugin(@PathVariable("plugin_id") Integer pluginId) {
    try {
      pluginService.disableOrEnablePlugin(pluginId, true);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginId), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginId), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(Route.BG_PLUGINS_OFF)
  @ResponseBody
  public ResponseEntity<String> disablePlugin(@PathVariable("plugin_id") Integer pluginId) {
    try {
      pluginService.disableOrEnablePlugin(pluginId, false);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception e) {
      logger.error(String.format("Failed to disable plugin \"%d\".", pluginId), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginId), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
