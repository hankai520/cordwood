
package ren.hankai.cordwood.console.service;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.core.domain.PluginPackage;
import ren.hankai.cordwood.persist.PluginPackageRepository;
import ren.hankai.cordwood.persist.model.PluginBean;
import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.plugin.PluginRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * 插件业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 13, 2016 1:20:47 PM
 */
@Component
public class PluginService {

  private static final Logger logger = LoggerFactory.getLogger(PluginService.class);
  @Autowired
  private PluginRegistry pluginRegistry;
  @Autowired
  private PluginPackageRepository pluginPackageRepository;

  /**
   * 安装插件包。
   *
   * @param url 插件包本地路径
   * @return 是否安装成功
   * @author hankai
   * @since Oct 25, 2016 11:00:00 AM
   */
  public boolean installPlugin(URL url) {
    try {
      PluginPackage pp = pluginRegistry.register(url);
      PluginPackageBean ppb = new PluginPackageBean();
      ppb.setChecksum(pp.getIdentifier());
      ppb.setFileName(pp.getFileName());
      for (Plugin plugin : pp.getPlugins()) {
        PluginBean pb = new PluginBean();
        pb.setActive(plugin.isActive());
        pb.setDescription(plugin.getDescription());
        pb.setName(plugin.getName());
        pb.setVersion(plugin.getVersion());
        pb.setPluginPackage(ppb);
        ppb.getPlugins().add(pb);
      }
      pluginPackageRepository.save(ppb);
    } catch (Exception e) {
      logger.error(String.format("Failed to install plugin from: %s", url.toString()), e);
    }
    return false;
  }
}