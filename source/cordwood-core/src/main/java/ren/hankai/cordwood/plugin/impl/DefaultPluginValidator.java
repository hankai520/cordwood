
package ren.hankai.cordwood.plugin.impl;

import org.springframework.stereotype.Component;

import ren.hankai.cordwood.plugin.PluginValidator;

import java.net.URL;

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
  public boolean validatePackage(URL jarUrl) {
    return true;
  }
}
