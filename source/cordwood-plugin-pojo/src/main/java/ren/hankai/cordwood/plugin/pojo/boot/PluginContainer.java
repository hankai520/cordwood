package ren.hankai.cordwood.plugin.pojo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import ren.hankai.cordwood.core.config.CoreSpringConfig;
import ren.hankai.cordwood.plugin.api.PluginRegistry;
import ren.hankai.cordwood.plugin.pojo.PojoPlugin;
import ren.hankai.cordwood.plugin.support.PluginRequestDispatcher;

/**
 * 插件容器（用于自启动插件），此嵌入式容器仅供调试使用。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 26, 2016 9:36:52 AM
 */
/*
 * 将此配置标记为仅在单机容器模式下载入，因为 @EnableAutoConfiguration 注解会尝试从类路径中包含的类来自动 配置需要的
 * bean，而这一过程所使用的类加载器并不是由插件容器提供的，因此无法加载到插件所依赖的 jar 包，因而会 导致类加载问题。
 */
@Profile("ignored")
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@Controller
public class PluginContainer extends PluginRequestDispatcher {

  /**
   * 要调试的插件类。
   */
  private static final Class<?>[] pluginClasses = new Class<?>[] {PojoPlugin.class};

  /**
   * 嵌入式插件容器是否以调试模式启动。
   */
  private static final boolean enableDebug = false;
  /**
   * 嵌入式插件容器监听本地什么 TCP 端口。
   */
  private static final String serverPort = "8000";

  /**
   * 插件容器启动入口。
   *
   * @param args 命令行参数
   * @throws Exception 异常
   * @author hankai
   * @since Nov 8, 2016 8:49:59 AM
   */
  public static void main(String[] args) throws Exception {
    System.setProperty("server.port", serverPort);
    if (enableDebug) {
      System.setProperty("debug", "true");
    }
    final SpringApplication app = new SpringApplication(PluginContainer.class);
    app.setAdditionalProfiles("ignored");
    final ApplicationContext context = app.run(args);
    final PluginRegistry pluginRegistry = context.getBean(PluginRegistry.class);
    // 注册插件
    for (final Class<?> clazz : pluginClasses) {
      pluginRegistry.registerTransientPlugin(clazz.newInstance(), true);
    }
  }

  @Configuration
  public static class InternalConfig extends CoreSpringConfig {
  }
}
