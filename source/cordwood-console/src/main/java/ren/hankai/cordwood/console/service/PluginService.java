
package ren.hankai.cordwood.console.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.PluginPackageRepository;
import ren.hankai.cordwood.console.persist.PluginRepository;
import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginManager;
import ren.hankai.cordwood.plugin.api.PluginRegistry;

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
  private PluginRepository PluginRepo;

  /**
   * 安装插件包。
   *
   * @param url 插件包本地路径
   * @return 是否安装成功
   * @author hankai
   * @since Oct 25, 2016 11:00:00 AM
   */
  @Transactional
  public boolean installPlugin(URL url) {
    try {
      final PluginPackage pp = pluginRegistry.registerPackage(url);
      final PluginPackageBean ppb = new PluginPackageBean();
      ppb.setChecksum(pp.getIdentifier());
      ppb.setFileName(pp.getFileName());
      for (final Plugin plugin : pp.getPlugins()) {
        final PluginBean pb = new PluginBean();
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
    } catch (final Exception e) {
      logger.error(String.format("Failed to install plugin from: %s", url.toString()), e);
    }
    return false;
  }

  /**
   * 获取已安装的插件包。
   *
   * @return 插件包集合
   * @author hankai
   * @since Nov 8, 2016 10:13:18 AM
   */
  public List<PluginPackageBean> getInstalledPluginPackages() {
    final Sort sort = new Sort(Direction.DESC, "id");
    return pluginPackageRepo.findAll(sort);
  }

  /**
   * 获取已安装的插件。
   *
   * @return 插件集合
   * @author hankai
   * @since Nov 8, 2016 3:28:11 PM
   */
  public List<PluginBean> getInstalledPlugins() {
    final Sort sort = new Sort(Direction.DESC, "createTime");
    final List<PluginBean> list = PluginRepo.findAll(sort);
    if (list != null) {
      Plugin plugin = null;
      for (final PluginBean pluginBean : list) {
        plugin = pluginManager.getPlugin(pluginBean.getName());
        pluginBean.setFeatures(new ArrayList<>(plugin.getFunctions().values()));
      }
    }
    return list;
  }
}
