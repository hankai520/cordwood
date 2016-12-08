
package ren.hankai.cordwood.console.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ren.hankai.cordwood.console.PluginInitializer;
import ren.hankai.cordwood.console.config.Route;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.persist.model.PluginRequest;
import ren.hankai.cordwood.console.persist.util.PageUtil;
import ren.hankai.cordwood.console.service.PluginService;
import ren.hankai.cordwood.console.view.model.BootstrapTableData;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.web.breadcrumb.NavigationItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 插件管理控制器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 8, 2016 10:07:48 AM
 */
@Controller
public class PluginController extends BaseController {

  private static final Logger logger = LoggerFactory.getLogger(PluginController.class);

  @Autowired
  private PluginService pluginService;
  @Autowired
  private MessageSource messageSource;
  @Autowired
  private PluginInitializer pluginInitializer;

  /**
   * 查看插件集合。
   *
   * @return 插件列表页
   * @author hankai
   * @since Nov 8, 2016 10:14:55 AM
   */
  @NavigationItem(label = "nav.plugins")
  @GetMapping(Route.BG_PLUGINS)
  public ModelAndView pluginPackages() {
    final ModelAndView mav = new ModelAndView("admin_plugins.html");
    final List<PluginPackageBean> list = pluginService.getInstalledPackages();
    mav.addObject("packages", list);
    return mav;
  }

  @GetMapping(Route.BG_PLUGINS_UNINSTALL)
  public ModelAndView uninstallpackage(@RequestParam("package_id") String packageId) {
    final ModelAndView mav = new ModelAndView("redirect:" + Route.BG_PLUGINS);
    final PluginPackageBean ppb = pluginService.getInstalledPackageById(packageId);
    if (ppb == null) {
      mav.setViewName("redirect:/404.html");
    } else if (!pluginService.uninstallPluginPackage(ppb)) {
      mav.addObject("error", messageSource.getMessage("package.uninstall.failed", null, null));
      mav.setViewName("admin_failure.html");
    }
    return mav;
  }

  @GetMapping(Route.BG_PLUGINS_ON)
  @ResponseBody
  public ResponseEntity<String> enablePlugin(@PathVariable("plugin_name") String pluginName) {
    try {
      pluginService.disableOrEnablePlugin(pluginName, true);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(Route.BG_PLUGINS_OFF)
  @ResponseBody
  public ResponseEntity<String> disablePlugin(@PathVariable("plugin_name") String pluginName) {
    try {
      pluginService.disableOrEnablePlugin(pluginName, false);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception e) {
      logger.error(String.format("Failed to disable plugin \"%d\".", pluginName), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(Route.BG_PLUGINS_UPLOAD)
  @ResponseBody
  public ResponseEntity<String> uploadPlugins(@RequestParam("files[]") MultipartFile[] files) {
    try {
      pluginInitializer.suspend();
      if ((files != null) && (files.length > 0)) {
        String tempPath = null;
        OutputStream output = null;
        InputStream input = null;
        final List<File> copiedFiles = new ArrayList<>(files.length);
        for (final MultipartFile file : files) {
          try {
            tempPath = Preferences.getTempDir() + File.separator + file.getOriginalFilename();
            FileUtils.deleteQuietly(new File(tempPath));
            input = file.getInputStream();
            output = new FileOutputStream(tempPath);
            IOUtils.copy(input, output);
            copiedFiles.add(new File(tempPath));
          } catch (final Exception e) {
            throw e;
          } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
          }
        }
        if (copiedFiles.size() > 0) {
          for (final File file : copiedFiles) {
            if (pluginService.installPluginPackage(file.toURI().toURL(), true)) {
              FileUtils.deleteQuietly(file);
            }
          }
        }
      }
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception e) {
      logger.error("Failed to upload plugins.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error e) {
      logger.error("Failed to upload plugins.", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      pluginInitializer.resume();
    }
  }

  @NavigationItem(label = "nav.plugins.logs", parent = "nav.plugins")
  @GetMapping(Route.BG_PLUGIN_LOGS)
  public ModelAndView pluginLogs() {
    return new ModelAndView("admin_plugin_logs.html");
  }

  @RequestMapping(Route.BG_PLUGIN_LOGS_JSON)
  @ResponseBody
  public BootstrapTableData getPluginLogsJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Page<PluginRequest> logs = pluginService.searchPluginRequests(search, pageable);
      response = new BootstrapTableData();
      response.setTotal(logs.getTotalElements());
      response.setRows(logs.getContent());
    } catch (final Exception e) {
      logger.error(Route.BG_PLUGIN_LOGS_JSON, e);
    } catch (final Error e) {
      logger.error(Route.BG_PLUGIN_LOGS_JSON, e);
    }
    return response;
  }
}
