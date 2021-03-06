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

package ren.hankai.cordwood.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ren.hankai.cordwood.core.api.support.WebServiceSupport;
import ren.hankai.cordwood.web.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 视图控制器基类，用于绘制web页面，处理表单数据等控制器类可继承此类。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 23, 2018 4:25:22 PM
 */
public abstract class BaseViewController {

  private static final Logger logger = LoggerFactory.getLogger(WebServiceSupport.class);

  /**
   * 获取404错误页面。
   *
   * @return 错误页面
   * @author hankai
   * @since Nov 23, 2018 4:38:35 PM
   */
  protected abstract String getNotFoundViewName();

  /**
   * 获取系统内部错误页面。
   *
   * @return 错误页面
   * @author hankai
   * @since Nov 23, 2018 4:38:47 PM
   */
  protected abstract String getErrorViewName();

  /**
   * 处理系统异常。
   *
   * @param request HTTP请求
   * @param throwable 异常
   * @return 视图模型
   * @author hankai
   * @since Nov 23, 2018 4:37:33 PM
   */
  @ExceptionHandler({Exception.class, Error.class})
  public ModelAndView handleException(HttpServletRequest request, Throwable throwable) {
    final ModelAndView mav = new ModelAndView();
    if (throwable instanceof NotFoundException) {
      mav.setViewName(getNotFoundViewName());
      mav.setStatus(HttpStatus.NOT_FOUND);
    } else {
      mav.setViewName(getErrorViewName());
      mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    logger.warn("Exception occurred.", throwable);
    return mav;
  }

}
