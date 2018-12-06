/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
