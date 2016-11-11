
package ren.hankai.cordwood.console.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import ren.hankai.cordwood.console.persist.PluginPackageRepository;
import ren.hankai.cordwood.console.persist.PluginRepository;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.console.persist.util.EntitySpecs;
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
   * 安装插件包。
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
      final PluginPackage pp = pluginRegistry.registerPackage(url, overwrite);
      if (pp != null) {
        final URL installUrl = copyPackage(url, overwrite);
        if (installUrl == null) {
          pluginRegistry.unregisterPackage(pp.getIdentifier());
        } else {
          pp.setInstallUrl(installUrl);
          final PluginPackageBean ppb = getInstalledPackage(pp.getIdentifier(), pp.getFileName());
          // 如果覆盖，
          if ((ppb != null) && overwrite) {
            pluginPackageRepo.delete(ppb.getChecksum());
          } else {

          }
          if (ppb == null) {

          }
          ppb.setChecksum(pp.getIdentifier());
          ppb.setFileName(pp.getFileName());
          ppb.getPlugins().clear();
          for (final Plugin plugin : pp.getPlugins()) {
            PluginBean pb = pluginRepo.findOne(plugin.getName());
            if (pb == null) {
              pb = new PluginBean();
            }
            pb.setActive(plugin.isActive());
            pb.setDescription(plugin.getDescription());
            pb.setDeveloper(plugin.getDeveloper());
            pb.setName(plugin.getName());
            pb.setDisplayName(plugin.getDisplayName());
            pb.setVersion(plugin.getVersion());
            pb.setPluginPackage(ppb);
            pb.setCreateTime(new Date());
            ppb.getPlugins().add(pb);
          }
          pluginPackageRepo.save(ppb);
          return true;
        }
      } else {
        logger.error("Plugin package installation failed due to file copy error.");
      }
    } catch (final Exception e) {
      logger.error(String.format("Failed to install plugin from: %s", url.toString()), e);
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
    return pluginPackageRepo.findAll(sort);
  }

  /**
   * 根据文件校验和查找插件包。
   *
   * @param checksum 插件包校验和
   * @return 插件包
   * @author hankai
   * @since Nov 10, 2016 11:32:33 AM
   */
  public PluginPackageBean getInstalledPackage(String checksum, String fileName) {
    PluginPackageBean ppb = pluginPackageRepo.findOne(EntitySpecs.field("checksum", checksum));
    if (ppb == null) {
      ppb = pluginPackageRepo.findOne(EntitySpecs.field("fileName", fileName));
    }
    return ppb;
  }

  /**
   * 根据校验和删除插件包信息。
   *
   * @param checksum 插件包ID
   * @author hankai
   * @since Nov 10, 2016 11:36:32 AM
   */
  public void deletePackageByChecksum(String checksum) {
    pluginPackageRepo.delete(checksum);
  }

  /**
   * 获取已安装的插件。
   *
   * @return 插件集合
   * @author hankai
   * @since Nov 8, 2016 3:28:11 PM
   */
  public List<PluginBean> getInstalledPlugins() {
    final Sort sort = new Sort(Direction.ASC, "name");
    final List<PluginBean> list = pluginRepo.findAll(sort);
    if (list != null) {
      Plugin plugin = null;
      for (final PluginBean pluginBean : list) {
        plugin = pluginManager.getPlugin(pluginBean.getName());
        if (plugin != null) {// 可能正在加载
          pluginBean.setFeatures(new ArrayList<>(plugin.getFunctions().values()));
        }
      }
    }
    return list;
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
}
