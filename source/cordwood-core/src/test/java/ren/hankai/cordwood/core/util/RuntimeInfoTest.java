
package ren.hankai.cordwood.core.util;

import com.jcabi.manifests.Manifests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 运行时信息测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 30, 2018 4:59:25 PM
 */
@SuppressWarnings("deprecation")
public class RuntimeInfoTest {

  @Test
  public void testGetHostName() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getHostName(), ri.getHostName());
    Assert.assertNotNull(RuntimeInfo.hostName(), RuntimeInfo.hostName());
  }

  @Test
  public void testGetIpAddress() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getIpAddress(), ri.getIpAddress());
    Assert.assertNotNull(RuntimeInfo.ipAddress(), RuntimeInfo.ipAddress());
  }

  @Test
  public void testGetJvmTotalMemory() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmTotalMemory(), ri.getJvmTotalMemory());
    Assert.assertNotNull(RuntimeInfo.jvmTotalMemory(), RuntimeInfo.jvmTotalMemory());
  }

  @Test
  public void testGetJvmFreeMemory() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmFreeMemory(), ri.getJvmFreeMemory());
    Assert.assertNotNull(RuntimeInfo.jvmFreeMemory(), RuntimeInfo.jvmFreeMemory());
  }

  @Test
  public void testGetJvmAvailableProcessors() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmAvailableProcessors(), ri.getJvmAvailableProcessors());
    Assert.assertNotNull(RuntimeInfo.jvmAvailableProcessors(),
        RuntimeInfo.jvmAvailableProcessors());
  }

  @Test
  public void testGetJavaVersion() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJavaVersion(), ri.getJavaVersion());
    Assert.assertNotNull(RuntimeInfo.javaVersion(), RuntimeInfo.javaVersion());
  }

  @Test
  public void testGetJavaVender() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJavaVender(), ri.getJavaVender());
    Assert.assertNotNull(RuntimeInfo.javaVender(), RuntimeInfo.javaVender());
  }

  @Test
  public void testGetJavaHome() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJavaHome(), ri.getJavaHome());
    Assert.assertNotNull(RuntimeInfo.javaHome(), RuntimeInfo.javaHome());
  }

  @Test
  public void testGetJvmSpecVersion() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmSpecVersion(), ri.getJvmSpecVersion());
    Assert.assertNotNull(RuntimeInfo.jvmSpecVersion(), RuntimeInfo.jvmSpecVersion());
  }

  @Test
  public void testGetJvmSpecVender() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmSpecVender(), ri.getJvmSpecVender());
    Assert.assertNotNull(RuntimeInfo.jvmSpecVender(), RuntimeInfo.jvmSpecVender());
  }

  @Test
  public void testGetJvmSpecName() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmSpecName(), ri.getJvmSpecName());
    Assert.assertNotNull(RuntimeInfo.jvmSpecName(), RuntimeInfo.jvmSpecName());
  }

  @Test
  public void testGetJvmVersion() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmVersion(), ri.getJvmVersion());
    Assert.assertNotNull(RuntimeInfo.jvmVersion(), RuntimeInfo.jvmVersion());
  }

  @Test
  public void testGetJvmVender() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmVender(), ri.getJvmVender());
    Assert.assertNotNull(RuntimeInfo.jvmVender(), RuntimeInfo.jvmVender());
  }

  @Test
  public void testGetJvmName() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJvmName(), ri.getJvmName());
    Assert.assertNotNull(RuntimeInfo.jvmName(), RuntimeInfo.jvmName());
  }

  @Test
  public void testGetJreSpecVersion() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJreSpecVersion(), ri.getJreSpecVersion());
    Assert.assertNotNull(RuntimeInfo.jreSpecVersion(), RuntimeInfo.jreSpecVersion());
  }

  @Test
  public void testGetJreSpecVender() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJreSpecVender(), ri.getJreSpecVender());
    Assert.assertNotNull(RuntimeInfo.jreSpecVender(), RuntimeInfo.jreSpecVender());
  }

  @Test
  public void testGetJreSpecName() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getJreSpecName(), ri.getJreSpecName());
    Assert.assertNotNull(RuntimeInfo.jreSpecName(), RuntimeInfo.jreSpecName());
  }

  @Test
  public void testGetOsName() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getOsName(), ri.getOsName());
    Assert.assertNotNull(RuntimeInfo.osName(), RuntimeInfo.osName());
  }

  @Test
  public void testGetOsArchitecture() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getOsArchitecture(), ri.getOsArchitecture());
    Assert.assertNotNull(RuntimeInfo.osArchitecture(), RuntimeInfo.osArchitecture());
  }

  @Test
  public void testGetOsVersion() {
    final RuntimeInfo ri = new RuntimeInfo();
    Assert.assertNotNull(ri.getOsVersion(), ri.getOsVersion());
    Assert.assertNotNull(RuntimeInfo.osVersion(), RuntimeInfo.osVersion());
  }

  @Test
  public void testgetPackageVersion() throws Exception {
    final String expVersion = "testonly";
    Manifests.DEFAULT.put("Implementation-Version", expVersion);

    final RuntimeInfo ri = new RuntimeInfo();
    final MockHttpServletRequest request = new MockHttpServletRequest();
    final String version = ri.getWarPackageVersion(this.getClass(), request);
    Assert.assertEquals(expVersion, version);

    Assert.assertEquals(expVersion, RuntimeInfo.packageVersion());
  }

}
