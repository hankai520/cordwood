/*******************************************************************************
 * Copyright (C) 2019 hankai
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

import com.jcabi.manifests.Manifests;
import org.junit.Assert;
import org.junit.Test;

/**
 * 运行时信息测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:59:25 PM
 */
public class RuntimeInfoTest {

  @Test
  public void testGetHostName() {
    Assert.assertNotNull(RuntimeInfo.hostName(), RuntimeInfo.hostName());
  }

  @Test
  public void testGetIpAddress() {
    Assert.assertNotNull(RuntimeInfo.ipAddress(), RuntimeInfo.ipAddress());
  }

  @Test
  public void testGetJvmTotalMemory() {
    Assert.assertNotNull(RuntimeInfo.jvmTotalMemory(), RuntimeInfo.jvmTotalMemory());
  }

  @Test
  public void testGetJvmFreeMemory() {
    Assert.assertNotNull(RuntimeInfo.jvmFreeMemory(), RuntimeInfo.jvmFreeMemory());
  }

  @Test
  public void testGetJvmAvailableProcessors() {
    Assert.assertNotNull(RuntimeInfo.jvmAvailableProcessors(),
        RuntimeInfo.jvmAvailableProcessors());
  }

  @Test
  public void testGetJavaVersion() {
    Assert.assertNotNull(RuntimeInfo.javaVersion(), RuntimeInfo.javaVersion());
  }

  @Test
  public void testGetJavaVender() {
    Assert.assertNotNull(RuntimeInfo.javaVender(), RuntimeInfo.javaVender());
  }

  @Test
  public void testGetJavaHome() {
    Assert.assertNotNull(RuntimeInfo.javaHome(), RuntimeInfo.javaHome());
  }

  @Test
  public void testGetJvmSpecVersion() {
    Assert.assertNotNull(RuntimeInfo.jvmSpecVersion(), RuntimeInfo.jvmSpecVersion());
  }

  @Test
  public void testGetJvmSpecVender() {
    Assert.assertNotNull(RuntimeInfo.jvmSpecVender(), RuntimeInfo.jvmSpecVender());
  }

  @Test
  public void testGetJvmSpecName() {
    Assert.assertNotNull(RuntimeInfo.jvmSpecName(), RuntimeInfo.jvmSpecName());
  }

  @Test
  public void testGetJvmVersion() {
    Assert.assertNotNull(RuntimeInfo.jvmVersion(), RuntimeInfo.jvmVersion());
  }

  @Test
  public void testGetJvmVender() {
    Assert.assertNotNull(RuntimeInfo.jvmVender(), RuntimeInfo.jvmVender());
  }

  @Test
  public void testGetJvmName() {
    Assert.assertNotNull(RuntimeInfo.jvmName(), RuntimeInfo.jvmName());
  }

  @Test
  public void testGetJreSpecVersion() {
    Assert.assertNotNull(RuntimeInfo.jreSpecVersion(), RuntimeInfo.jreSpecVersion());
  }

  @Test
  public void testGetJreSpecVender() {
    Assert.assertNotNull(RuntimeInfo.jreSpecVender(), RuntimeInfo.jreSpecVender());
  }

  @Test
  public void testGetJreSpecName() {
    Assert.assertNotNull(RuntimeInfo.jreSpecName(), RuntimeInfo.jreSpecName());
  }

  @Test
  public void testGetOsName() {
    Assert.assertNotNull(RuntimeInfo.osName(), RuntimeInfo.osName());
  }

  @Test
  public void testGetOsArchitecture() {
    Assert.assertNotNull(RuntimeInfo.osArchitecture(), RuntimeInfo.osArchitecture());
  }

  @Test
  public void testOsVersion() {
    Assert.assertNotNull(RuntimeInfo.osVersion());
  }

  @Test
  public void testPackageVersion() throws Exception {
    final String expVersion = "testonly";
    Manifests.DEFAULT.put("Implementation-Version", expVersion);
    final String version = RuntimeInfo.packageVersion();
    Assert.assertEquals(expVersion, version);
  }

}
