
package ren.hankai.cordwood.core.util;

import com.jcabi.manifests.Manifests;

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

  private static final Properties props = new Properties();

  static {
    props.putAll(System.getProperties());
    props.putAll(System.getenv());
    try {
      final InetAddress addr = InetAddress.getLocalHost();
      props.put("hostName", addr.getHostName());
      props.put("hostIp", addr.getHostAddress());
    } catch (final UnknownHostException ex) {
      throw new RuntimeException("Failed to get local host info.", ex);
    }
  }

  /**
   * 获取主机信息。
   *
   * @return 主机名
   * @author hankai
   * @since Oct 13, 2016 10:23:53 AM
   */
  @Deprecated
  public String getHostName() {
    return RuntimeInfo.hostName();
  }

  /**
   * 获取主机信息。
   *
   * @return 主机名
   * @author hankai
   * @since Dec 1, 2018 4:29:51 PM
   */
  public static String hostName() {
    return props.getProperty("hostName");
  }

  /**
   * 获取IP地址。
   *
   * @return IP地址
   * @author hankai
   * @since Oct 13, 2016 10:24:07 AM
   * @deprecated 请使用
   */
  @Deprecated
  public String getIpAddress() {
    return RuntimeInfo.ipAddress();
  }

  /**
   * 获取IP地址。
   *
   * @return IP地址
   * @author hankai
   * @since Dec 1, 2018 4:30:15 PM
   */
  public static String ipAddress() {
    return props.getProperty("hostIp");
  }

  /**
   * 获取 JVM 总内存大小。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Oct 13, 2016 10:24:24 AM
   */
  @Deprecated
  public String getJvmTotalMemory() {
    return RuntimeInfo.jvmTotalMemory();
  }

  /**
   * 获取 JVM 总内存大小。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Dec 1, 2018 4:39:07 PM
   */
  public static String jvmTotalMemory() {
    final long bytes = Runtime.getRuntime().totalMemory();
    final String memory =
        MathUtil.toHumanReadableString(bytes, 1024, new String[] {" B", " KB", " MB"});
    return memory;
  }

  /**
   * JVM 剩余可用内存。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Oct 13, 2016 10:25:12 AM
   */
  @Deprecated
  public String getJvmFreeMemory() {
    return RuntimeInfo.jvmFreeMemory();
  }

  /**
   * JVM 剩余可用内存。
   *
   * @return 格式化后的大小信息（例如：100 MB）
   * @author hankai
   * @since Dec 1, 2018 4:39:51 PM
   */
  public static String jvmFreeMemory() {
    final long bytes = Runtime.getRuntime().freeMemory();
    final String memory =
        MathUtil.toHumanReadableString(bytes, 1024, new String[] {" B", " KB", " MB"});
    return memory;
  }

  /**
   * JVM 可用处理器数量。
   *
   * @return 处理器数量
   * @author hankai
   * @since Oct 13, 2016 10:25:28 AM
   */
  public String getJvmAvailableProcessors() {
    return RuntimeInfo.jvmAvailableProcessors();
  }

  /**
   * JVM 可用处理器数量。
   *
   * @return 处理器数量
   * @author hankai
   * @since Dec 1, 2018 4:40:47 PM
   */
  public static String jvmAvailableProcessors() {
    return Runtime.getRuntime().availableProcessors() + "";
  }

  /**
   * 获取 Java 版本。
   *
   * @return JAVA版本
   * @author hankai
   * @since Oct 13, 2016 10:25:58 AM
   */
  @Deprecated
  public String getJavaVersion() {
    return RuntimeInfo.javaVersion();
  }

  /**
   * 获取 Java 版本。
   *
   * @return JAVA版本
   * @author hankai
   * @since Dec 1, 2018 4:41:11 PM
   */
  public static String javaVersion() {
    return props.getProperty("java.version");
  }

  /**
   * 获取 Java 实现（供应商）。
   *
   * @return JAVA实现
   * @author hankai
   * @since Oct 13, 2016 10:26:10 AM
   */
  @Deprecated
  public String getJavaVender() {
    return RuntimeInfo.javaVender();
  }

  /**
   * 获取 Java 实现（供应商）。
   *
   * @return JAVA实现
   * @author hankai
   * @since Dec 1, 2018 4:41:48 PM
   */
  public static String javaVender() {
    return props.getProperty("java.vendor");
  }

  /**
   * 获取 java home。
   *
   * @return java home
   * @author hankai
   * @since Oct 13, 2016 10:26:39 AM
   */
  @Deprecated
  public String getJavaHome() {
    return RuntimeInfo.javaHome();
  }

  /**
   * 获取 java home。
   *
   * @return java home
   * @author hankai
   * @since Dec 1, 2018 4:43:15 PM
   */
  public static String javaHome() {
    return props.getProperty("java.home");
  }

  /**
   * 获取 JVM 规范版本号。
   *
   * @return JVM规范版本
   * @author hankai
   * @since Oct 13, 2016 10:26:49 AM
   */
  @Deprecated
  public String getJvmSpecVersion() {
    return RuntimeInfo.jvmSpecVersion();
  }

  /**
   * 获取 JVM 规范版本号。
   *
   * @return JVM规范版本
   * @author hankai
   * @since Dec 1, 2018 4:43:54 PM
   */
  public static String jvmSpecVersion() {
    return props.getProperty("java.vm.specification.version");
  }

  /**
   * 获取 JVM 规范的实现（供应商）。
   *
   * @return JVM规范实现
   * @author hankai
   * @since Oct 13, 2016 10:27:01 AM
   */
  @Deprecated
  public String getJvmSpecVender() {
    return RuntimeInfo.jvmSpecVender();
  }

  /**
   * 获取 JVM 规范的实现（供应商）。
   *
   * @return JVM规范实现
   * @author hankai
   * @since Dec 1, 2018 4:44:25 PM
   */
  public static String jvmSpecVender() {
    return props.getProperty("java.vm.specification.vendor");
  }

  /**
   * 获取 JVM 规范的名称。
   *
   * @return JVM规范名
   * @author hankai
   * @since Oct 13, 2016 10:27:20 AM
   */
  @Deprecated
  public String getJvmSpecName() {
    return RuntimeInfo.jvmSpecName();
  }

  /**
   * 获取 JVM 规范的名称。
   *
   * @return JVM规范名
   * @author hankai
   * @since Dec 1, 2018 4:45:19 PM
   */
  public static String jvmSpecName() {
    return props.getProperty("java.vm.specification.name");
  }

  /**
   * 获取 JVM 版本。
   *
   * @return JVM版本
   * @author hankai
   * @since Oct 13, 2016 10:27:44 AM
   */
  @Deprecated
  public String getJvmVersion() {
    return RuntimeInfo.jvmVersion();
  }

  /**
   * 获取 JVM 版本。
   *
   * @return JVM版本
   * @author hankai
   * @since Dec 1, 2018 4:45:46 PM
   */
  public static String jvmVersion() {
    return props.getProperty("java.vm.version");
  }

  /**
   * 获取 JVM 实现（供应商）。
   *
   * @return JVM实现
   * @author hankai
   * @since Oct 13, 2016 10:27:53 AM
   */
  @Deprecated
  public String getJvmVender() {
    return RuntimeInfo.jvmVender();
  }

  /**
   * 获取 JVM 实现（供应商）。
   *
   * @return JVM实现
   * @author hankai
   * @since Dec 1, 2018 4:46:36 PM
   */
  public static String jvmVender() {
    return props.getProperty("java.vm.vendor");
  }

  /**
   * 获取 JVM 名称。
   *
   * @return JVM名
   * @author hankai
   * @since Oct 13, 2016 10:28:04 AM
   */
  @Deprecated
  public String getJvmName() {
    return RuntimeInfo.jvmName();
  }

  /**
   * 获取 JVM 名称。
   *
   * @return JVM名
   * @author hankai
   * @since Dec 1, 2018 4:47:55 PM
   */
  public static String jvmName() {
    return props.getProperty("java.vm.name");
  }

  /**
   * 获取 JAVA 运行时规范版本。
   *
   * @return JRE规范版本
   * @author hankai
   * @since Oct 13, 2016 10:28:13 AM
   */
  @Deprecated
  public String getJreSpecVersion() {
    return RuntimeInfo.jreSpecVersion();
  }

  /**
   * 获取 JAVA 运行时规范版本。
   *
   * @return JRE规范版本
   * @author hankai
   * @since Dec 1, 2018 4:48:23 PM
   */
  public static String jreSpecVersion() {
    return props.getProperty("java.specification.version");
  }

  /**
   * 获取 JAVA 运行时规范的实现。
   *
   * @return JRE规范实现
   * @author hankai
   * @since Oct 13, 2016 10:28:27 AM
   */
  @Deprecated
  public String getJreSpecVender() {
    return RuntimeInfo.jreSpecVender();
  }

  /**
   * 获取 JAVA 运行时规范的实现。
   *
   * @return JRE规范实现
   * @author hankai
   * @since Dec 1, 2018 4:49:15 PM
   */
  public static String jreSpecVender() {
    return props.getProperty("java.specification.vendor");
  }

  /**
   * 获取 JAVA 运行时规范的名称。
   *
   * @return JRE规范名
   * @author hankai
   * @since Oct 13, 2016 10:28:39 AM
   */
  @Deprecated
  public String getJreSpecName() {
    return RuntimeInfo.jreSpecName();
  }

  /**
   * 获取 JAVA 运行时规范的名称。
   *
   * @return JRE规范名
   * @author hankai
   * @since Dec 1, 2018 4:49:49 PM
   */
  public static String jreSpecName() {
    return props.getProperty("java.specification.name");
  }

  /**
   * 获取操作系统名称。
   *
   * @return 操作系统名
   * @author hankai
   * @since Oct 13, 2016 10:28:54 AM
   */
  @Deprecated
  public String getOsName() {
    return RuntimeInfo.osName();
  }

  /**
   * 获取操作系统名称。
   *
   * @return 操作系统名
   * @author hankai
   * @since Dec 1, 2018 4:50:17 PM
   */
  public static String osName() {
    return props.getProperty("os.name");
  }

  /**
   * 获取操作系统架构。
   *
   * @return 操作系统架构
   * @author hankai
   * @since Oct 13, 2016 10:29:07 AM
   */
  @Deprecated
  public String getOsArchitecture() {
    return RuntimeInfo.osArchitecture();
  }

  /**
   * 获取操作系统架构。
   *
   * @return 操作系统架构
   * @author hankai
   * @since Dec 1, 2018 4:50:49 PM
   */
  public static String osArchitecture() {
    return props.getProperty("os.arch");
  }

  /**
   * 获取操作系统版本。
   *
   * @return 操作系统版本
   * @author hankai
   * @since Oct 13, 2016 10:29:15 AM
   */
  @Deprecated
  public String getOsVersion() {
    return RuntimeInfo.osVersion();
  }

  /**
   * 获取操作系统版本。
   *
   * @return 操作系统版本
   * @author hankai
   * @since Dec 1, 2018 4:51:32 PM
   */
  public static String osVersion() {
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
  @Deprecated
  public String getWarPackageVersion(Class<?> clazz, HttpServletRequest request) {
    return RuntimeInfo.packageVersion();
  }

  /**
   * 获取当前应用包（jar/war等）的实现版本（Implementation-Version）。
   *
   * @return 版本号
   * @author hankai
   * @since Jul 11, 2017 9:43:25 AM
   */
  public static String packageVersion() {
    return Manifests.DEFAULT.get("Implementation-Version");
  }

}
