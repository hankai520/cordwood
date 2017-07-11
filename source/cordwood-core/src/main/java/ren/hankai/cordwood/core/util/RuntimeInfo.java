
package ren.hankai.cordwood.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统运行时信息。
 *
 * @author hankai
 * @version 1.0
 * @since Aug 12, 2016 10:27:55 AM
 */
public final class RuntimeInfo {

  private static final Logger logger = LoggerFactory.getLogger(RuntimeInfo.class);

  private final Properties props;

  /**
   * 初始化运行时信息。
   */
  public RuntimeInfo() {
    props = System.getProperties();
    props.putAll(System.getenv());
    try {
      final InetAddress addr = InetAddress.getLocalHost();
      props.put("hostName", addr.getHostName());
      props.put("hostIp", addr.getHostAddress());
    } catch (final UnknownHostException ex) {
      throw new RuntimeException("Failed to initialize runtime info.", ex);
    }
  }

  /**
   * 获取主机信息。
   *
   * @return 主机名
   * @author hankai
   * @since Oct 13, 2016 10:23:53 AM
   */
  public String getHostName() {
    return props.getProperty("hostName");
  }

  /**
   * 获取IP地址。
   *
   * @return IP地址
   * @author hankai
   * @since Oct 13, 2016 10:24:07 AM
   */
  public String getIpAddress() {
    return props.getProperty("hostIp");
  }

  /**
   * 获取 JVM 总内存大小。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Oct 13, 2016 10:24:24 AM
   */
  public String getJvmTotalMemory() {
    long mem = Runtime.getRuntime().totalMemory();
    mem = mem / 1024 / 1024;
    return String.format("%d MB", mem);
  }

  /**
   * JVM 剩余可用内存。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Oct 13, 2016 10:25:12 AM
   */
  public String getJvmFreeMemory() {
    long mem = Runtime.getRuntime().freeMemory();
    mem = mem / 1024 / 1024;
    return String.format("%d MB", mem);
  }

  /**
   * JVM 可用处理器数量。
   *
   * @return 处理器数量
   * @author hankai
   * @since Oct 13, 2016 10:25:28 AM
   */
  public String getJvmAvailableProcessors() {
    return Runtime.getRuntime().availableProcessors() + "";
  }

  /**
   * 获取 Java 版本。
   *
   * @return JAVA版本
   * @author hankai
   * @since Oct 13, 2016 10:25:58 AM
   */
  public String getJavaVersion() {
    return props.getProperty("java.version");
  }

  /**
   * 获取 Java 实现（供应商）。
   *
   * @return JAVA实现
   * @author hankai
   * @since Oct 13, 2016 10:26:10 AM
   */
  public String getJavaVender() {
    return props.getProperty("java.vendor");
  }

  /**
   * 获取 java home。
   *
   * @return java home
   * @author hankai
   * @since Oct 13, 2016 10:26:39 AM
   */
  public String getJavaHome() {
    return props.getProperty("java.home");
  }

  /**
   * 获取 JVM 规范版本号。
   *
   * @return JVM规范版本
   * @author hankai
   * @since Oct 13, 2016 10:26:49 AM
   */
  public String getJvmSpecVersion() {
    return props.getProperty("java.vm.specification.version");
  }

  /**
   * 获取 JVM 规范的实现（供应商）。
   *
   * @return JVM规范实现
   * @author hankai
   * @since Oct 13, 2016 10:27:01 AM
   */
  public String getJvmSpecVender() {
    return props.getProperty("java.vm.specification.vendor");
  }

  /**
   * 获取 JVM 规范的名称。
   *
   * @return JVM规范名
   * @author hankai
   * @since Oct 13, 2016 10:27:20 AM
   */
  public String getJvmSpecName() {
    return props.getProperty("java.vm.specification.name");
  }

  /**
   * 获取 JVM 版本。
   *
   * @return JVM版本
   * @author hankai
   * @since Oct 13, 2016 10:27:44 AM
   */
  public String getJvmVersion() {
    return props.getProperty("java.vm.version");
  }

  /**
   * 获取 JVM 实现（供应商）。
   *
   * @return JVM实现
   * @author hankai
   * @since Oct 13, 2016 10:27:53 AM
   */
  public String getJvmVender() {
    return props.getProperty("java.vm.vendor");
  }

  /**
   * 获取 JVM 名称。
   *
   * @return JVM名
   * @author hankai
   * @since Oct 13, 2016 10:28:04 AM
   */
  public String getJvmName() {
    return props.getProperty("java.vm.name");
  }

  /**
   * 获取 JAVA 运行时规范版本。
   *
   * @return JRE规范版本
   * @author hankai
   * @since Oct 13, 2016 10:28:13 AM
   */
  public String getJreSpecVersion() {
    return props.getProperty("java.specification.version");
  }

  /**
   * 获取 JAVA 运行时规范的实现。
   *
   * @return JRE规范实现
   * @author hankai
   * @since Oct 13, 2016 10:28:27 AM
   */
  public String getJreSpecVender() {
    return props.getProperty("java.specification.vender");
  }

  /**
   * 获取 JAVA 运行时规范的名称。
   *
   * @return JRE规范名
   * @author hankai
   * @since Oct 13, 2016 10:28:39 AM
   */
  public String getJreSpecName() {
    return props.getProperty("java.specification.name");
  }

  /**
   * 获取操作系统名称。
   *
   * @return 操作系统名
   * @author hankai
   * @since Oct 13, 2016 10:28:54 AM
   */
  public String getOsName() {
    return props.getProperty("os.name");
  }

  /**
   * 获取操作系统架构。
   *
   * @return 操作系统架构
   * @author hankai
   * @since Oct 13, 2016 10:29:07 AM
   */
  public String getOsArchitecture() {
    return props.getProperty("os.arch");
  }

  /**
   * 获取操作系统版本。
   *
   * @return 操作系统版本
   * @author hankai
   * @since Oct 13, 2016 10:29:15 AM
   */
  public String getOsVersion() {
    return props.getProperty("os.version");
  }

  /**
   * 获取当前web程序包的版本。
   *
   * @param clazz 程序包中的任意类
   * @param request servlet请求
   * @return 版本号
   * @author hankai
   * @since Jul 11, 2017 9:43:25 AM
   */
  public String getWarPackageVersion(Class<?> clazz, HttpServletRequest request) {
    String version = clazz.getPackage().getImplementationVersion();
    if (version == null) {
      final Properties prop = new Properties();
      try {
        prop.load(request.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
        version = prop.getProperty("Implementation-Version");
      } catch (final Exception e) {
        logger.trace("", e);
      }
    }
    return version;
  }
}
