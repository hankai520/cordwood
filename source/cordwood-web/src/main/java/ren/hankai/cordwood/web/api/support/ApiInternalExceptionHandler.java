
package ren.hankai.cordwood.web.api.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * API 内部异常处理器（如：spring mvc内部异常）。
 *
 * @author hankai
 * @version 0.0.2
 * @since Aug 29, 2019 12:05:02 PM
 */
public class ApiInternalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ApiInternalExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    final ResponseEntity<Object> response =
        super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    if (request instanceof ServletWebRequest) {
      final ServletWebRequest swr = (ServletWebRequest) request;
      final HttpServletRequest hsr = swr.getRequest();
      logger.warn("Request method '{}' not supported by URL {}", hsr.getMethod(),
          hsr.getRequestURL().toString());
    }
    return response;
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    final ResponseEntity<Object> response =
        super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    if (request instanceof ServletWebRequest) {
      final ServletWebRequest swr = (ServletWebRequest) request;
      final HttpServletRequest hsr = swr.getRequest();
      final String supported = ex.getSupportedMediaTypes().toString();
      logger.warn("Request media type '{}' not supported by URL {}, supported media types {}",
          hsr.getContentType(), hsr.getRequestURL().toString(), supported);
    }
    return response;
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      final HttpMediaTypeNotAcceptableException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    final ResponseEntity<Object> response =
        super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    if (request instanceof ServletWebRequest) {
      final ServletWebRequest swr = (ServletWebRequest) request;
      final HttpServletRequest hsr = swr.getRequest();
      final String supported = ex.getSupportedMediaTypes().toString();
      logger.warn(
          "Response media type '{}' not acceptable by client. Request URL is {},"
              + " request media type is '{}'",
          supported, hsr.getRequestURL().toString(), hsr.getContentType());
    }
    return response;
  }

}
