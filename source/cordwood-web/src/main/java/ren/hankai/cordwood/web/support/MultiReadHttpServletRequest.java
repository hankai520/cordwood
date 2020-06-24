
package ren.hankai.cordwood.web.support;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 支持多次读取数据的Http Servlet请求包装类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 26, 2018 4:41:17 PM
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

  private static final Logger logger = LoggerFactory.getLogger(MultiReadHttpServletRequest.class);

  private ByteArrayOutputStream cachedBytes;

  public MultiReadHttpServletRequest(HttpServletRequest request) {
    super(request);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (cachedBytes == null) {
      cachedBytes = new ByteArrayOutputStream();
      IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    return new CachedServletInputStream();
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  public class CachedServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream input;

    public CachedServletInputStream() {
      input = new ByteArrayInputStream(cachedBytes.toByteArray());
    }

    @Override
    public int read() throws IOException {
      return input.read();
    }

    @Override
    public boolean isFinished() {
      return true;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
      logger.warn("CachedServletInputStream does not support this feature.");
    }
  }
}
