package ren.hankai.cordwood.plugin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * 插件配置基类，对于基于 spring 框架的插件来说，需要导入此配置。这又分为2种情况： 1. 插件运行于插件容器（即cordwood-console） 2. 插件脱离容器独立运行（即内嵌容器）
 * 情况1，cordwood-console 会导入此配置，因此插件配置类无需再导入 情况2，内嵌的容器实现需要导入此配置，否则插件依赖的一些部件无法组装。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 1, 2016 10:20:31 AM
 */
public class PluginConfig {

  /**
   * 类型转换服务。
   *
   * @return 类型转换服务实例
   * @author hankai
   * @since Oct 31, 2016 11:03:46 PM
   */
  @Bean
  @Primary
  public ConversionService getConversionService() {
    return new DefaultConversionService();
  }

}
