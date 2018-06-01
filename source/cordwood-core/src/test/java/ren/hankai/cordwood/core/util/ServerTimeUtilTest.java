
package ren.hankai.cordwood.core.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerTimeUtilTest {

  @Test
  public void testGetServerTimeViaHttp() {
    final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    final List<Logger> loggers = lc.getLoggerList();
    System.out.println(loggers);

    final Long remoteTime = ServerTimeUtil.getServerTimeViaHttp("https://www.baidu.com");
    Assert.assertNotNull(remoteTime);
    final long localTime = System.currentTimeMillis() / 1000;
    // 因为超时时间默认为2秒
    Assert.assertTrue(Math.abs(localTime - remoteTime) <= 2);
  }

}
