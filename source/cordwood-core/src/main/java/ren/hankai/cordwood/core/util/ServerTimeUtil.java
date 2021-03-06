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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 服务器时间工具类。
 *
 * @author hankai
 * @version 1.0
 * @since Apr 28, 2018 11:27:44 AM
 */
public class ServerTimeUtil {

  private static final Logger logger = LoggerFactory.getLogger(ServerTimeUtil.class);

  /**
   * 通过HTTP协议获取服务器时间（连接超时为2s），如果无法获取到目标服务器时间，则返回 null。
   *
   * @param httpUrl HTTP 访问地址
   * @return GMT时间（秒）
   * @author hankai
   * @since Apr 28, 2018 11:30:47 AM
   */
  public static Long getServerTimeViaHttp(String httpUrl) {
    try {
      HttpSslUtil.trustAllHostnames();
      HttpSslUtil.trustAllHttpsCertificates();
      final URL url = new URL(httpUrl);
      final URLConnection connection = url.openConnection();
      connection.setConnectTimeout(2 * 1000);
      connection.setReadTimeout(2 * 1000);
      connection.setDoOutput(false);
      final String dateStr = connection.getHeaderField("Date");
      final SimpleDateFormat dateFormat =
          new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      final Date time = dateFormat.parse(dateStr);
      if (null != time) {
        return time.getTime() / 1000;
      }
    } catch (final Exception ex) {
      logger.debug("Failed to get server time via http.", ex);
    }
    return null;
  }

}
