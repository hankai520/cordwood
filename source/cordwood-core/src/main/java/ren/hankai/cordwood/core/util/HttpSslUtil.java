
package ren.hankai.cordwood.core.util;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class HttpSslUtil {

  private static HostnameVerifier hostnameVerifier;

  private static TrustManager[] trustManagers;

  public static void trustAllHostnames() {
    if (hostnameVerifier == null) {
      hostnameVerifier = new FakeHostnameVerifier();
    }
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
  }

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

  public static class FakeHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname,
        javax.net.ssl.SSLSession session) {
      return true;
    }
  }

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
