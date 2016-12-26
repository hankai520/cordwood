
package ren.hankai.cordwood.console.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
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
  private PluginInitializer pluginInitializer;

  /**
   * 查看插件集合。
   *
   * @return 插件列表页
   * @author hankai
   * @since Nov 8, 2016 10:14:55 AM
   */
  @NavigationItem(label = "nav.plugins")
  @GetMapping(Route.BG_PLUGIN_PACKAGES)
  public ModelAndView pluginPackages() {
    final ModelAndView mav = new ModelAndView("admin_plugin_packages.html");
    return mav;
  }

  /**
   * 获取插件包列表AJAX接口。
   *
   * @param search 搜索关键字
   * @param order 排序字段
   * @param sort 升序/降序
   * @param limit 返回结果数
   * @param offset 从第几条开始返回
   * @return 插件包列表
   * @author hankai
   * @since Dec 26, 2016 11:01:45 AM
   */
  @RequestMapping(Route.BG_PLUGIN_PACKAGES_JSON)
  @ResponseBody
  public BootstrapTableData getPluginPackagesJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort, @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Page<PluginPackageBean> packages =
          pluginService.searchInstalledPackages(search, pageable);
      response = new BootstrapTableData();
      response.setTotal(packages.getTotalElements());
      response.setRows(packages.getContent());
    } catch (final Exception ex) {
      logger.error(Route.BG_PLUGIN_PACKAGES_JSON, ex);
    } catch (final Error ex) {
      logger.error(Route.BG_PLUGIN_PACKAGES_JSON, ex);
    }
    return response;
  }

  /**
   * 插件包详情页面。
   *
   * @param packageId 插件包标识
   * @return 插件包详情页面
   * @author hankai
   * @since Dec 26, 2016 11:05:42 AM
   */
  @NavigationItem(label = "nav.plugin.details", parent = "nav.plugins")
  @GetMapping(Route.BG_PLUGIN_PACKAGE_DETAILS)
  public ModelAndView pluginPackageDetails(@RequestParam("package_id") String packageId) {
    final ModelAndView mav = new ModelAndView("admin_plugin_package_details.html");
    final PluginPackageBean ppb = pluginService.getInstalledPackageById(packageId);
    if (ppb == null) {
      mav.setViewName("redirect:/404.html");
    } else {
      mav.addObject("pluginPackage", ppb);
    }
    return mav;
  }

  /**
   * 卸载插件包AJAX接口。
   *
   * @param packageId 插件包标识
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 11:04:22 AM
   */
  @GetMapping(Route.BG_PLUGIN_PACKAGES_UNINSTALL)
  public ResponseEntity<String> uninstallPackage(@RequestParam("package_id") String packageId) {
    final PluginPackageBean ppb = pluginService.getInstalledPackageById(packageId);
    if (ppb != null) {
      if (!pluginService.uninstallPluginPackage(ppb)) {
        logger.warn("Failed to uninstall plugin package: " + packageId);
      }
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 启用插件功能AJAX接口。
   *
   * @param pluginName 插件名称
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 11:04:06 AM
   */
  @GetMapping(Route.BG_PLUGINS_ON)
  @ResponseBody
  public ResponseEntity<String> enablePlugin(@PathVariable("plugin_name") String pluginName) {
    try {
      pluginService.disableOrEnablePlugin(pluginName, true);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception ex) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error ex) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 禁用插件功能AJAX接口。
   *
   * @param pluginName 插件名
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 11:03:49 AM
   */
  @GetMapping(Route.BG_PLUGINS_OFF)
  @ResponseBody
  public ResponseEntity<String> disablePlugin(@PathVariable("plugin_name") String pluginName) {
    try {
      pluginService.disableOrEnablePlugin(pluginName, false);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (final Exception ex) {
      logger.error(String.format("Failed to disable plugin \"%d\".", pluginName), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error ex) {
      logger.error(String.format("Failed to enable plugin \"%d\".", pluginName), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * 上传插件包AJAX接口。
   *
   * @param files 插件包文件
   * @return 是否成功
   * @author hankai
   * @since Dec 26, 2016 11:03:26 AM
   */
  @PostMapping(Route.BG_PLUGIN_PACKAGES_UPLOAD)
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
          } catch (final Exception ex) {
            throw ex;
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
    } catch (final Exception ex) {
      logger.error("Failed to upload plugins.", ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (final Error ex) {
      logger.error("Failed to upload plugins.", ex);
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

  /**
   * 获取插件访问日志列表AJAX接口。
   *
   * @param search 搜索关键字
   * @param order 排序字段
   * @param sort 升序/降序
   * @param limit 返回结果数
   * @param offset 从第几条开始返回
   * @return 插件访问日志JSON
   * @author hankai
   * @since Dec 26, 2016 11:02:25 AM
   */
  @RequestMapping(Route.BG_PLUGIN_LOGS_JSON)
  @ResponseBody
  public BootstrapTableData getPluginLogsJson(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "order", required = false) String order,
      @RequestParam(value = "sort", required = false) String sort, @RequestParam("limit") int limit,
      @RequestParam("offset") int offset) {
    BootstrapTableData response = null;
    try {
      final boolean asc = "asc".equalsIgnoreCase(order);
      final Pageable pageable = PageUtil.pageWithOffsetAndCount(offset, limit, sort, asc);
      final Page<PluginRequestBean> logs = pluginService.searchPluginRequests(search, pageable);
      response = new BootstrapTableData();
      response.setTotal(logs.getTotalElements());
      response.setRows(logs.getContent());
    } catch (final Exception ex) {
      logger.error(Route.BG_PLUGIN_LOGS_JSON, ex);
    } catch (final Error ex) {
      logger.error(Route.BG_PLUGIN_LOGS_JSON, ex);
    }
    return response;
  }
}
