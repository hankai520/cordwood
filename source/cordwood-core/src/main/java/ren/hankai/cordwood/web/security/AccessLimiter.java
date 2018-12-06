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

package ren.hankai.cordwood.web.security;

import org.springframework.web.servlet.HandlerExecutionChain;

import javax.servlet.http.HttpServletResponse;

/**
 * 访问限制器，通过限流、熔断机制保护服务器，增强稳定性。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 12, 2018 10:51:56 AM
 */
public interface AccessLimiter {

  /**
   * 处理访问。
   *
   * @param requestHandler 请求处理器
   * @param response HTTP响应，用于写入响应代码
   * @return 是否允许处理访问
   * @author hankai
   * @since Oct 12, 2018 10:57:35 AM
   */
  boolean handleAccess(HandlerExecutionChain requestHandler, HttpServletResponse response);

}
