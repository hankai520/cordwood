
package ren.hankai.cordwood.plugin;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import ren.hankai.cordwood.core.Preferences;
import ren.hankai.cordwood.plugin.api.PluginLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 封装插件包信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 10:17:19 AM
 */
public final class PluginPackage {

  private String identifier;
  private String groupId;
  private String artifactId;
  private String version;
  private String developer;
  private String description;
  private String[] basePackages;
  private String configClass;
  private Manifest manifest;
  private String fileName;
  private URL sourceUrl; // 源插件包文件 URL
  private URL installationUrl; // 插件包文件安装后的 URL
  private ClassLoader classLoader;
  private final List<Plugin> plugins = new ArrayList<>();

  private void escapeSpecialCharacters() {
    if (StringUtils.isNotEmpty(identifier)) {
      identifier = StringEscapeUtils.escapeHtml4(identifier);
    }
    if (StringUtils.isNotEmpty(groupId)) {
      groupId = StringEscapeUtils.escapeHtml4(groupId);
    }
    if (StringUtils.isNotEmpty(artifactId)) {
      artifactId = StringEscapeUtils.escapeHtml4(artifactId);
    }
    if (StringUtils.isNotEmpty(version)) {
      version = StringEscapeUtils.escapeHtml4(version);
    }
    if (StringUtils.isNotEmpty(developer)) {
      developer = StringEscapeUtils.escapeHtml4(developer);
    }
    if (StringUtils.isNotEmpty(description)) {
      description = StringEscapeUtils.escapeHtml4(description);
    }
  }

  public PluginPackage() {}

  /**
   * 用本地文件 URL 构造插件包模型。
   *
   * @param url 插件包文件本地 URL
   */
  public PluginPackage(URL url) {
    manifest = parseJarManifest(url);
    sourceUrl = url;
    fileName = FilenameUtils.getName(url.getPath());
    final String dest = Preferences.getPluginsDir() + File.separator + fileName;
    try {
      installationUrl = new File(dest).toURI().toURL();
    } catch (final MalformedURLException e) {
      throw new RuntimeException(
          String.format("Failed to create installation url for package \"%s\"", url.toString()), e);
    }
    final Attributes attributes = manifest.getMainAttributes();
    groupId = attributes.getValue(PluginLoader.GROUP_ID);
    artifactId = attributes.getValue(PluginLoader.ARTIFACT_ID);
    version = attributes.getValue(PluginLoader.PACKAGE_VERSION);
    developer = attributes.getValue(PluginLoader.DEVELOPER);
    description = attributes.getValue(PluginLoader.DESCRIPTION);
    identifier = String.format("%s:%s:%s", groupId, artifactId, version);
    final String basePackages = attributes.getValue(PluginLoader.PLUGIN_BASE_PACKAGES);
    if (StringUtils.isEmpty(basePackages)) {
      throw new RuntimeException("Invalid plugin package: no base packages specified in manifest!");
    }
    configClass = attributes.getValue(PluginLoader.PLUGIN_SPRING_CONFIG_CLASS);
    this.basePackages = basePackages.split(",");
    escapeSpecialCharacters();
  }

  /**
   * 插件包唯一标识。
   *
   * @return 标识
   * @author hankai
   * @since Nov 11, 2016 10:23:33 PM
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * 解析插件包文件的 manifest。
   *
   * @param url 插件包本地URL
   * @return 插件包manifest
   * @author hankai
   * @since Nov 11, 2016 10:20:13 PM
   */
  private Manifest parseJarManifest(URL url) {
    JarInputStream jarStream = null;
    try {
      final InputStream is = url.openStream();
      jarStream = new JarInputStream(is);
      final Manifest manifest = jarStream.getManifest();
      return manifest;
    } catch (final IOException e) {
      throw new RuntimeException(
          String.format("Failed to parse manifest of package \"%s\".", url.getPath()), e);
    } finally {
      IOUtils.closeQuietly(jarStream);
    }
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
      plugin.setPackageId(getIdentifier());
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

  /**
   * 检查插件包文件是否已安装到插件目录。
   *
   * @return 是否已安装
   * @author hankai
   * @since Nov 11, 2016 10:54:04 PM
   */
  public boolean isInstalled() {
    try {
      return new File(installationUrl.toURI()).exists();
    } catch (final URISyntaxException e) {
      throw new RuntimeException("Failed to get installation uri.", e);
    }
  }

  /**
   * 获取 groupId 字段的值。
   *
   * @return groupId 字段值
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * 获取 artifactId 字段的值。
   *
   * @return artifactId 字段值
   */
  public String getArtifactId() {
    return artifactId;
  }

  /**
   * 获取 version 字段的值。
   *
   * @return version 字段值
   */
  public String getVersion() {
    return version;
  }

  /**
   * 获取 developer 字段的值。
   *
   * @return developer 字段值
   */
  public String getDeveloper() {
    return developer;
  }

  /**
   * 获取 description 字段的值。
   *
   * @return description 字段值
   */
  public String getDescription() {
    return description;
  }

  /**
   * 获取 manifest 字段的值。
   *
   * @return manifest 字段值
   */
  public Manifest getManifest() {
    return manifest;
  }

  /**
   * 获取 fileName 字段的值。
   *
   * @return fileName 字段值
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 获取 sourceUrl 字段的值。
   *
   * @return sourceUrl 字段值
   */
  public URL getSourceUrl() {
    return sourceUrl;
  }

  /**
   * 获取 installationUrl 字段的值。
   *
   * @return installationUrl 字段值
   */
  public URL getInstallationUrl() {
    return installationUrl;
  }

  /**
   * 获取 classLoader 字段的值。
   *
   * @return classLoader 字段值
   */
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  /**
   * 设置 classLoader 字段的值。
   *
   * @param classLoader classLoader 字段的值
   */
  public void setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  /**
   * 获取 basePackages 字段的值。
   *
   * @return basePackages 字段值
   */
  public String[] getBasePackages() {
    return basePackages;
  }

  /**
   * 获取 configClass 字段的值。
   *
   * @return configClass 字段值
   */
  public String getConfigClass() {
    return configClass;
  }

}
