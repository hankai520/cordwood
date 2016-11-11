
package ren.hankai.cordwood.plugin.support;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ren.hankai.cordwood.plugin.PluginPackage;
import ren.hankai.cordwood.plugin.api.PluginValidator;

/**
 * 默认插件文件验证器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 4:07:10 PM
 */
@Component
public class DefaultPluginValidator implements PluginValidator {

  @Override
  public boolean validatePackage(PluginPackage pluginPackage) {
    if (pluginPackage == null) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getGroupId())) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getArtifactId())) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getVersion())) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getIdentifier())) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getFileName())) {
      return false;
    } else if (StringUtils.isEmpty(pluginPackage.getSourceUrl())) {
      return false;
    }
    return true;
  }
}
