
package ren.hankai.cordwood.core.domain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装插件包信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 10:17:19 AM
 */
public final class PluginPackage {

  private String identifier;
  private String fileName;
  private URL installUrl;
  private final List<Plugin> plugins = new ArrayList<>();

  /**
   * 获取插件包唯一标识符（SHA1 校验和）。
   *
   * @return 插件包ID
   * @author hankai
   * @since Oct 13, 2016 10:17:06 AM
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * 设置插件包唯一标识符。
   *
   * @author hankai
   * @since Oct 13, 2016 10:17:33 AM
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * 获取插件包对应的物理文件名。
   *
   * @return 文件名
   * @author hankai
   * @since Oct 13, 2016 10:17:58 AM
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 设置插件包对应的物理文件名。
   *
   * @param fileName 文件名
   * @author hankai
   * @since Oct 13, 2016 10:18:17 AM
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * 获取插件包安装后的本地路径。
   *
   * @return 插件包本地路径
   * @author hankai
   * @since Oct 13, 2016 10:18:30 AM
   */
  public URL getInstallUrl() {
    return installUrl;
  }

  /**
   * 设置插件包安装后的本地路径。
   *
   * @author hankai
   * @since Oct 13, 2016 10:18:56 AM
   */
  public void setInstallUrl(URL installUrl) {
    this.installUrl = installUrl;
  }

  /**
   * 添加插件。
   *
   * @param plugin 插件
   * @author hankai
   * @since Oct 13, 2016 10:19:09 AM
   */
  public void addPlugin(Plugin plugin) {
    if (plugin != null) {
      plugin.setPackageId(identifier);
      plugins.add(plugin);
    }
  }

  /**
   * 移除插件。
   *
   * @param plugin 插件
   * @author hankai
   * @since Oct 13, 2016 10:19:23 AM
   */
  public void removePlugin(Plugin plugin) {
    if (plugin != null) {
      plugins.remove(plugin);
    }
  }

  /**
   * 获取插件包内所有插件，此方法返回一个插件集合的副本，修改此副本不会影响 原插件集合。
   *
   * @return 插件集合
   * @author hankai
   * @since Oct 13, 2016 10:19:41 AM
   */
  public List<Plugin> getPlugins() {
    return new ArrayList<>(plugins);
  }
}
