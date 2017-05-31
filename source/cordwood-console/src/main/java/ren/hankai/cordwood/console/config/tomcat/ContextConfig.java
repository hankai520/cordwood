
package ren.hankai.cordwood.console.config.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;

/**
 * Tomcat Context 配置。
 *
 * @author hankai
 * @version 1.0
 * @since Apr 5, 2016 4:15:40 PM
 */
public class ContextConfig implements TomcatContextCustomizer {

  /*
   * 自定义 tomcat context 配置
   *
   * @see
   * org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer#customize(org.apache
   * .catalina.Context)
   */
  @Override
  public void customize(Context context) {
    final int cacheSize = 40 * 1024;
    final StandardRoot standardRoot = new StandardRoot(context);
    standardRoot.setCacheMaxSize(cacheSize);
    context.setResources(standardRoot);
  }
}
