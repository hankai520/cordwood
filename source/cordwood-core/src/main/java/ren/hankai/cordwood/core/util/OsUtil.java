
package ren.hankai.cordwood.core.util;

import java.io.File;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * 操作系统工具类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jan 5, 2017 11:48:19 AM
 */
public final class OsUtil {

  /**
   * 获取系统物理内存总大小（字节）。
   *
   * @return 内存总大小
   * @author hankai
   * @since Jan 5, 2017 11:48:46 AM
   */
  public static long getSystemTotalRamSize() {
    final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    try {
      final Object attribute = mBeanServer.getAttribute(
          new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
      return (Long) attribute;
    } catch (final Exception ex) {
      return 0L;
    }
  }

  /**
   * 获取系统空闲内存大小。
   *
   * @return 空闲内存大小
   * @author hankai
   * @since Jan 5, 2017 1:07:34 PM
   */
  public static long getSystemFreeRamSize() {
    final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    try {
      final Object attribute = mBeanServer.getAttribute(
          new ObjectName("java.lang", "type", "OperatingSystem"), "FreePhysicalMemorySize");
      return (Long) attribute;
    } catch (final Exception ex) {
      return 0L;
    }
  }

  /**
   * 获取系统总存储大小（字节）。
   *
   * @return 系统总存储大小（字节）
   * @author hankai
   * @since Jan 5, 2017 1:44:16 PM
   */
  public static long getSystemTotalHdSize() {
    long total = 0L;
    final File[] roots = File.listRoots();
    if (roots != null) {
      for (final File root : roots) {
        total += root.getTotalSpace();
      }
    }
    return total;
  }

  /**
   * 获取系统剩余存储空间大小（字节）。
   *
   * @return 系统剩余存储空间大小（字节）
   * @author hankai
   * @since Jan 5, 2017 1:45:22 PM
   */
  public static long getSystemFreeHdSize() {
    long free = 0L;
    final File[] roots = File.listRoots();
    if (roots != null) {
      for (final File root : roots) {
        free += root.getFreeSpace();
      }
    }
    return free;
  }

}
