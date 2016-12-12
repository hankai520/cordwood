
package ren.hankai.cordwood.console.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import ren.hankai.cordwood.console.persist.PluginPackageRepository;
import ren.hankai.cordwood.console.persist.PluginRepository;
import ren.hankai.cordwood.console.persist.PluginRequestRepository;
import ren.hankai.cordwood.console.persist.PluginRequestRepository.PluginRequestSpecs;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.console.persist.support.EntitySpecs;
import ren.hankai.cordwood.console.view.model.PluginRequestStatistics;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 插件业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 13, 2016 1:20:47 PM
 */
@Service
public class PluginService {

  private static final Logger logger = LoggerFactory.getLogger(PluginService.class);
  @Autowired
  private PluginRegistry pluginRegistry;
  @Autowired
  private PluginManager pluginManager;
  @Autowired
  private PluginPackageRepository pluginPackageRepo;
  @Autowired
  private PluginRepository pluginRepo;
  @Autowired
  private PluginRequestRepository pluginRequestRepo;

  /**
   * 将临时插件包文件复制到插件包安装路径，如果插件包已安装过了，则不会覆盖。
   *
   * @param tempUrl 临时插件包路径
   * @param overwrite 是否覆盖已存在的插件包
   * @return 安装后的本地插件包路径
   * @author hankai
   * @since Oct 13, 2016 1:59:07 PM
   */
  private URL copyPackage(URL tempUrl, boolean overwrite) {
    final String name = FilenameUtils.getName(tempUrl.getPath());
    final String localPath = Preferences.getPluginsDir() + File.separator + name;
    if (tempUrl.getPath().equals(localPath)) {
      return tempUrl;
    }
    InputStream is = null;
    OutputStream os = null;
    try {
      File localFile = new File(localPath);
      if (overwrite && localFile.exists()) {
        localFile.delete();
        localFile = new File(localPath);
      }
      if (!localFile.exists()) {
        is = tempUrl.openStream();
        os = new FileOutputStream(localFile);
        FileCopyUtils.copy(is, os);
      }
      return localFile.toURI().toURL();
    } catch (final MalformedURLException e) {
      logger.error(String.format("Failed to convert package url to local: %s", tempUrl.toString()),
          e);
    } catch (final IOException e) {
      logger.error(String.format("Failed to copy plugin package file: %s", tempUrl.toString()), e);
    } finally {
      IOUtils.closeQuietly(os);
      IOUtils.closeQuietly(is);
    }
    return null;
  }

  /**
   * 安装插件包（安装时，将会将指定 url 对应的插件包拷贝到插件安装目录）。
   *
   * @param url 插件包本地路径
   * @param overwrite 是否覆盖已有插件包
   * @return 是否安装成功
   * @author hankai
   * @since Oct 25, 2016 11:00:00 AM
   */
  @Transactional
  public boolean installPluginPackage(URL url, boolean overwrite) {
    try {
      final URL installUrl = copyPackage(url, overwrite);
      if (installUrl != null) {
        final PluginPackage pp = pluginRegistry.registerPackage(url, overwrite);
        if (pp != null) {
          PluginPackageBean ppb = pluginPackageRepo.findOne(pp.getIdentifier());
          if ((ppb == null) || overwrite) {
            if (ppb == null) {
              ppb = new PluginPackageBean();
            }
            ppb.setCreateTime(new Date());
            ppb.setFileName(pp.getFileName());
            ppb.setId(pp.getIdentifier());
            ppb.setFileName(pp.getFileName());
            for (final Plugin plugin : pp.getPlugins()) {
              final PluginBean pb = new PluginBean();
              pb.setPluginPackage(ppb);
              pb.setActive(plugin.isActive());
              pb.setDescription(plugin.getDescription());
              pb.setDeveloper(plugin.getDeveloper());
              pb.setName(plugin.getName());
              pb.setDisplayName(plugin.getDisplayName());
              pb.setVersion(plugin.getVersion());
              ppb.getPlugins().add(pb);
            }
            pluginPackageRepo.save(ppb);
          }
          return true;
        }
      }
    } catch (final Exception e) {
      logger.error(String.format("Failed to install plugin from: %s", url.toString()), e);
    }
    return false;
  }

  @Transactional
  public boolean uninstallPluginPackage(PluginPackageBean ppb) {
    if (pluginRegistry.unregisterPackage(ppb.getId())) {
      pluginPackageRepo.delete(ppb.getId());
      final String path = Preferences.getPluginsDir() + File.separator + ppb.getFileName();
      return FileUtils.deleteQuietly(new File(path));
    }
    return false;
  }

  /**
   * 按名称查找已安装的插件信息。
   *
   * @param name 插件名
   * @return 插件信息
   * @author hankai
   * @since Nov 10, 2016 11:40:21 AM
   */
  public PluginBean getInstalledPluginByName(String name) {
    return pluginRepo.findOne(EntitySpecs.field("name", name));
  }

  /**
   * 获取已安装的插件包。
   *
   * @return 插件包集合
   * @author hankai
   * @since Nov 8, 2016 10:13:18 AM
   */
  public List<PluginPackageBean> getInstalledPackages() {
    final Sort sort = new Sort(Direction.DESC, "fileName");
    final List<PluginPackageBean> list = pluginPackageRepo.findAll(sort);
    if (list != null) {
      for (final PluginPackageBean ppb : list) {
        for (final PluginBean pluginBean : ppb.getPlugins()) {
          final Plugin plugin = pluginManager.getPlugin(pluginBean.getName());
          if (plugin != null) {// 为空时可能正在加载
            pluginBean.setFeatures(new ArrayList<>(plugin.getFunctions().values()));
          }
        }
      }
    }
    return list;
  }

  /**
   * 根据文件名获取已安装的插件包。
   *
   * @param fileName 插件包文件名
   * @return 插件包
   * @author hankai
   * @since Nov 12, 2016 12:09:52 AM
   */
  public PluginPackageBean getInstalledPackageByFileName(String fileName) {
    return pluginPackageRepo.findOne(EntitySpecs.field("fileName", fileName));
  }

  public PluginPackageBean getInstalledPackageById(String packageId) {
    return pluginPackageRepo.findOne(packageId);
  }

  /**
   * 启用或禁用插件。
   *
   * @param name 插件名
   * @param enabled 是否启用
   * @author hankai
   * @since Nov 10, 2016 9:57:48 AM
   */
  public void disableOrEnablePlugin(String name, boolean enabled) {
    final PluginBean pb = pluginRepo.findOne(name);
    if (pb != null) {
      pb.setActive(enabled);
      pluginRepo.save(pb);
      if (enabled) {
        pluginManager.activatePlugin(pb.getName());
      } else {
        pluginManager.deactivatePlugin(pb.getName());
      }
    }
  }

  /**
   * 保存插件请求记录。
   *
   * @param request 插件请求
   * @author hankai
   * @since Dec 8, 2016 10:47:21 AM
   */
  @Transactional
  public void savePluginRequest(PluginRequestBean request) {
    pluginRequestRepo.save(request);
  }

  /**
   * 搜索插件访问记录。
   *
   * @param keyword 关键字
   * @param pageable 分页
   * @return 访问记录列表
   * @author hankai
   * @since Dec 8, 2016 3:25:23 PM
   */
  public Page<PluginRequestBean> searchPluginRequests(String keyword, Pageable pageable) {
    return pluginRequestRepo.findAll(PluginRequestSpecs.search(keyword), pageable);
  }

  /**
   * 对指定时间范围内的用户插件访问信息进行统计。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 统计信息
   * @author hankai
   * @since Dec 12, 2016 2:04:02 PM
   */
  public PluginRequestStatistics getUserPluginStatistics(String userEmail, Date beginTime,
      Date endTime) {
    final PluginRequestStatistics stats = new PluginRequestStatistics();

    final long userPluginAccessCount =
        pluginRequestRepo.getUserPluginAccessCount(userEmail, beginTime, endTime);
    stats.setAccessCount(userPluginAccessCount);

    final double avg = pluginRequestRepo.getUserPluginTimeUsageAvg(userEmail, beginTime, endTime);
    stats.setTimeUsageAvg(avg);

    final float failures =
        pluginRequestRepo.count(PluginRequestSpecs.userPluginRequests(userEmail, false));
    final float faultRate = failures / userPluginAccessCount;
    stats.setFaultRate((int) (faultRate * 100));

    final long totalCount = pluginRequestRepo.count();
    final float usageRage = ((float) userPluginAccessCount) / totalCount;
    stats.setUsageRage((int) (usageRage * 100));

    final long totalBytes = pluginRequestRepo.getPluginTotalDataBytes(beginTime, endTime);
    final long userPluginBytes =
        pluginRequestRepo.getUserPluginDataBytes(userEmail, beginTime, endTime);
    final float dataShare = ((float) userPluginBytes) / totalBytes;
    stats.setDataShare((int) (dataShare * 100));

    stats.setSummarizedRequests(
        pluginRequestRepo.getRequestsGroupByPlugin(userEmail, beginTime, endTime));

    return stats;
  }

}
