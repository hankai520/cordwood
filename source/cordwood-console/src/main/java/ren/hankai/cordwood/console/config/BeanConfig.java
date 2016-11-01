package ren.hankai.cordwood.console.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.config.PluginConfig;

/**
 * Spring Bean 配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 31, 2016 10:45:47 PM
 *
 */
@Configuration
@Import(PluginConfig.class)
public class BeanConfig extends CoreSpringConfig {

}
