
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ServerTimeUtilTest {

  @Test
  public void testGetServerTimeViaHttp() {
    final long remoteTime = ServerTimeUtil.getServerTimeViaHttp("http://www.baidu.com");
    final long localTime = System.currentTimeMillis() / 1000;
    // 因为超时时间默认为2秒
    Assert.assertTrue(Math.abs(localTime - remoteTime) <= 2);
  }

}
