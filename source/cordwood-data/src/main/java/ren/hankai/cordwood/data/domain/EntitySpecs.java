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
/*
 * Copyright © 2016 hankai.ren, All rights reserved.
 * http://www.hankai.ren
 */

package ren.hankai.cordwood.data.domain;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * JPA 实体通用查询条件。
 *
 * @author hankai
 * @version 1.0
 * @since Oct 9, 2016 3:18:12 PM
 */
public abstract class EntitySpecs {

  /**
   * 单字段查询条件。
   *
   * @param <T> 实体类型
   * @param fieldName 字段名称
   * @param value 字段值
   * @return 查询条件
   * @author hankai
   * @since Aug 18, 2016 10:45:18 AM
   */
  public static <T> Specification<T> field(final String fieldName, final Object value) {
    return new Specification<T>() {

      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get(fieldName), value);
      }
    };
  }

  /**
   * 字段值在给定范围内的查询条件。
   *
   * @param <T> 实体类型
   * @param fieldName 字段
   * @param values 取值范围
   * @return 查询条件
   * @author hankai
   * @since Oct 13, 2016 2:58:59 PM
   */
  public static <T> Specification<T> fieldIn(final String fieldName, final List<?> values) {
    return new Specification<T>() {

      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return root.get(fieldName).in(values);
      }
    };
  }

  /**
   * 查询字段的值不在指定范围内的实体。
   *
   * @param <T> 实体类型
   * @param fieldName 字段
   * @param values 取值范围
   * @return 查询条件
   * @author hankai
   * @since Oct 14, 2016 1:07:16 PM
   */
  public static <T> Specification<T> fieldNotIn(final String fieldName, final List<?> values) {
    return new Specification<T>() {

      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.not(root.get(fieldName).in(values));
      }
    };
  }
}
