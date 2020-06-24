
package ren.hankai.cordwood.core.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.core.config.CoreSpringConfig;

/**
 * 用于测试的 Spring 核心配置。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 3:47:40 PM
 */
@Configuration
@Profile(Preferences.PROFILE_TEST)
public class BeanConfig extends CoreSpringConfig {

}
