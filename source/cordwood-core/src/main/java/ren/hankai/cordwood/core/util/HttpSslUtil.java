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

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Https 协议的 SSL 配置助手类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jul 31, 2018 2:56:02 PM
 */
public final class HttpSslUtil {

  private static HostnameVerifier hostnameVerifier = null;

  private static TrustManager[] trustManagers = null;

  /**
   * 信任所有主机名（域名）。
   *
   * @author hankai
   * @since Jul 31, 2018 2:56:44 PM
   */
  public static void trustAllHostnames() {
    if (hostnameVerifier == null) {
      hostnameVerifier = new FakeHostnameVerifier();
    }
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
  }

  /**
   * 信任所有 HTTPS协议的 SSL证书。
   *
   * @return 安全连接上下文
   * @author hankai
   * @since Jul 31, 2018 2:57:00 PM
   */
  public static SSLContext trustAllHttpsCertificates() {
    SSLContext context = null;
    if (trustManagers == null) {
      trustManagers = new TrustManager[] {new FakeX509TrustManager()};
    }
    try {
      context = SSLContext.getInstance("SSL");
      context.init(null, trustManagers, new SecureRandom());
    } catch (final GeneralSecurityException gse) {
      throw new IllegalStateException(gse.getMessage());
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    return context;
  }

  /**
   * 伪主机名验证器，内部实现不验证主机域名。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 31, 2018 2:57:21 PM
   */
  public static class FakeHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname,
        javax.net.ssl.SSLSession session) {
      return true;
    }
  }

  /**
   * 伪 X.509 格式的证书验证器，内部信任所有SSL证书。
   *
   * @author hankai
   * @version 1.0.0
   * @since Jul 31, 2018 2:58:02 PM
   */
  public static class FakeX509TrustManager implements X509TrustManager {

    private static final X509Certificate[] acceptedIssuers =
        new X509Certificate[] {};

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
        String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
        String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return (acceptedIssuers);
    }
  }
}
