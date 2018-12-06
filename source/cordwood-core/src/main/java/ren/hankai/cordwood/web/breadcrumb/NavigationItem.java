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

package ren.hankai.cordwood.web.breadcrumb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 面包屑导航注解，用于标记控制器中的映射为一个导航项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 6, 2016 2:51:57 PM
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NavigationItem {

  /**
   * 标签名（支持i18n）。
   *
   * @return 标签名
   * @author hankai
   * @since Dec 6, 2016 2:52:25 PM
   */
  public String label();

  /**
   * 所属分组。
   *
   * @return 所属分组
   * @author hankai
   * @since Dec 6, 2016 2:52:56 PM
   */
  public String family() default "shared";

  /**
   * 前一个导航项的标签名。
   *
   * @return 前一个导航项的标签名
   * @author hankai
   * @since Dec 6, 2016 2:53:13 PM
   */
  public String parent() default "";
}
