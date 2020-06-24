
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 操作系统工具类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:58:20 PM
 */
public class OsUtilTest {

  @Test
  public void testGetSystemTotalRamSize() {
    // 此case只适合在1GB内存以上机器中运行
    Assert.assertTrue(OsUtil.getSystemTotalRamSize() + "",
        OsUtil.getSystemTotalRamSize() > (1024 * 1024 * 1024));
  }

  @Test
  public void testGetSystemFreeRamSize() {
    // 此case只适合在空闲内存大于1MB的机器中运行
    Assert.assertTrue(OsUtil.getSystemFreeRamSize() + "",
        OsUtil.getSystemFreeRamSize() > (1024 * 1024 * 1));
  }

  @Test
  public void testGetSystemTotalHdSize() {
    // 此case只适合在硬盘大于1GB的机器中运行
    Assert.assertTrue(OsUtil.getSystemTotalHdSize() + "",
        OsUtil.getSystemTotalHdSize() > (1024 * 1024 * 1024));
  }

  @Test
  public void testGetSystemFreeHdSize() {
    // 此case只适合在硬盘空闲存储空间大于1GB的机器中运行
    Assert.assertTrue(OsUtil.getSystemFreeHdSize() + "",
        OsUtil.getSystemFreeHdSize() > (1024 * 1024 * 1024));
  }

}
