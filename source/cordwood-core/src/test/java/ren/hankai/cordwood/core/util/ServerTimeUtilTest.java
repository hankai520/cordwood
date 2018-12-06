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
