
package ren.hankai.cordwood.core.util;

import org.junit.Assert;
import org.junit.Test;
import ren.hankai.cordwood.core.test.http.DoNothingHandler;
import ren.hankai.cordwood.core.test.http.TinyHttpServer;

public class ServerTimeUtilTest {

  @Test
  public void testGetServerTimeViaHttp() throws Exception {
    final int port = 9000;
    final TinyHttpServer server = new TinyHttpServer(new DoNothingHandler(null));
    server.start(port, false);

    final Long remoteTime = ServerTimeUtil.getServerTimeViaHttp("http://127.0.0.1:" + port);
    Assert.assertNotNull(remoteTime);
    final long localTime = System.currentTimeMillis() / 1000;
    Assert.assertTrue(Math.abs(localTime - remoteTime) <= 1);

    server.stop();
  }

}
