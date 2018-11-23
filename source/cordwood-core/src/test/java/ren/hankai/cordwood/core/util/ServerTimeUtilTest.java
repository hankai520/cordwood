
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ServerTimeUtilTest {

  @Test
  public void testGetServerTimeViaHttp() {
    final Long remoteTime = ServerTimeUtil.getServerTimeViaHttp("https://www.baidu.com");
    Assert.assertNotNull(remoteTime);
    final long localTime = System.currentTimeMillis() / 1000;
    Assert.assertTrue(Math.abs(localTime - remoteTime) <= 60);
  }

}
