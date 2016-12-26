
package ren.hankai.cordwood.core.api.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import ren.hankai.cordwood.core.api.exception.ApiException;

import javax.servlet.http.HttpServletRequest;

/**
 * web service 基类，处理异常等公共事务。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 2:07:27 PM
 */
public abstract class WebServiceSupport {

  private static final Logger logger = LoggerFactory.getLogger(WebServiceSupport.class);

  /**
   * 处理内部异常导致的接口调用错误。
   * 
   * @param request HTTP请求
   * @param exception 异常
   * @return 错误
   * @author hankai
   * @since Dec 26, 2016 11:28:14 AM
   */
  @ExceptionHandler({Exception.class, Error.class})
  @ResponseBody
  public ApiResponse handleInternalError(HttpServletRequest request, Exception exception) {
    final ApiResponse response = new ApiResponse();
    response.setCode(ApiCode.InternalError);
    response.setMessage(exception.getMessage());
    logger.error(request.getRequestURL().toString(), exception);
    return response;
  }

  /**
   * 处理接口业务逻辑错误。
   * 
   * @param request HTTP 请求
   * @param exception 异常
   * @return 错误
   * @author hankai
   * @since Dec 26, 2016 11:31:57 AM
   */
  @ExceptionHandler({ApiException.class})
  @ResponseBody
  public ApiResponse handleBusinessError(HttpServletRequest request, ApiException exception) {
    final ApiResponse response = new ApiResponse();
    response.setCode(ApiCode.Success);
    response.getBody().setError(exception.getCode());
    response.getBody().setMessage(exception.getMessage());
    logger.error(request.getRequestURL().toString(), exception);
    return response;
  }

}
