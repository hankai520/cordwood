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

package ren.hankai.cordwood.data.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
 * Spring 分页助手类。
 *
 * @author hankai
 * @version 1.0
 * @since Aug 18, 2016 10:19:10 AM
 */
public class PageUtil {

  /**
   * 根据页码和页大小构建分页信息。
   *
   * @param index 页码（从1开始, ps: spring 是从0开始，这有违习惯）
   * @param size 页大小
   * @return 分页信息
   * @author hankai
   * @since Aug 18, 2016 10:24:07 AM
   */
  public static Pageable pageWithIndexAndSize(int index, int size) {
    return pageWithIndexAndSize(index, size, null, new String[] {});
  }

  /**
   * 根据页码和页大小构建分页信息。
   *
   * @param index 页码（从1开始, ps: spring 是从0开始，这有违习惯）
   * @param size 页大小
   * @param asc 是否升序
   * @param orderBy 多个排序字段
   * @return 分页信息
   * @author hankai
   * @since Jul 3, 2017 5:55:27 PM
   */
  public static Pageable pageWithIndexAndSize(int index, int size, Boolean asc, String... orderBy) {
    if ((orderBy != null) && (orderBy.length > 0) && (asc != null)) {
      final Direction direction = asc ? Direction.ASC : Direction.DESC;
      return new PageRequest(index - 1, size, direction, orderBy);
    }
    return new PageRequest(index - 1, size);
  }

  /**
   * 根据首条记录位置和返回记录数构建分页信息。
   *
   * @param offset 首条记录位置
   * @param count 返回记录数
   * @return 分页信息
   * @author hankai
   * @since Aug 18, 2016 10:24:52 AM
   */
  public static Pageable pageWithOffsetAndCount(int offset, int count) {
    return pageWithOffsetAndCount(offset, count, null, null);
  }

  /**
   * 根据首条记录位置和返回记录数构建分页信息。
   *
   * @param offset 首条记录位置
   * @param count 返回记录数
   * @param orderBy 排序字段
   * @param asc 是否升序
   * @return 分页信息
   * @author hankai
   * @since Aug 18, 2016 10:24:52 AM
   */
  public static Pageable pageWithOffsetAndCount(int offset, int count, String orderBy,
      Boolean asc) {
    if (!StringUtils.isEmpty(orderBy) && (asc != null)) {
      final Direction direction = asc ? Direction.ASC : Direction.DESC;
      if (count > 0) {
        // 避免0为除数导致异常
        return new PageRequest((offset / count), count, direction, orderBy);
      } else {
        return new PageRequest(0, count, direction, orderBy);
      }
    } else {
      if (count > 0) {
        return new PageRequest((offset / count), count);
      } else {
        return new PageRequest(0, count);
      }
    }
  }
}
