
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
   * @author hankai
   * @since Jul 31, 2018 2:57:00 PM
   */
  public static void trustAllHttpsCertificates() {
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
