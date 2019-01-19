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

package ren.hankai.cordwood.data.jpa.support;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ren.hankai.cordwood.data.domain.DeleteSpecification;
import ren.hankai.cordwood.data.jpa.repository.BaseRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * JPA 实体仓库基类实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 21, 2016 5:21:28 PM
 */
public class BaseRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
    implements BaseRepository<T, I> {

  protected final EntityManager entityManager;
  protected final Class<T> domainClass;

  /**
   * 构造实体仓库基类。
   *
   * @param domainClass 实体类的类型
   * @param entityManager JPA 实体管理器
   */
  public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
    this.domainClass = domainClass;
  }

  /**
   * 实体仓库基类初始化。
   *
   * @param entityInformation 实体信息
   * @param entityManager 实体管理器（JPA核心对象）
   */
  public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
    this.domainClass = entityInformation.getJavaType();
  }

  @Override
  public void detach(Object entity) {
    entityManager.detach(entity);
  }

  @Override
  public int delete(DeleteSpecification<T> spec) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaDelete<T> cd = cb.createCriteriaDelete(domainClass);
    final Root<T> root = cd.from(domainClass);
    cd.where(spec.toPredicate(root, cd, cb));
    return entityManager.createQuery(cd).executeUpdate();
  }

  @Override
  public T findFirst(Specification<T> specification) {
    return findOne(specification, null);
  }

  @Override
  public T findOne(Specification<T> specification, Sort sort) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<T> cq = cb.createQuery(domainClass);
    final Root<T> root = cq.from(domainClass);
    cq.where(specification.toPredicate(root, cq, cb));
    if (sort != null) {
      final List<javax.persistence.criteria.Order> orders = new ArrayList<>();
      final Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        final Order order = it.next();
        if (order.isAscending()) {
          orders.add(cb.asc(root.get(order.getProperty())));
        } else {
          orders.add(cb.desc(root.get(order.getProperty())));
        }
      }
      if (orders.size() > 0) {
        cq.orderBy(orders);
      }
    }
    final List<T> results = entityManager.createQuery(cq).setMaxResults(1).getResultList();
    if ((results != null) && (results.size() > 0)) {
      return results.get(0);
    }
    return null;
  }

  @Override
  public int executeNativeUpdate(String sql) {
    return entityManager.createNativeQuery(sql).executeUpdate();
  }

  @Override
  public List<T> executeNativeQuery(String sql) {
    return entityManager.createNativeQuery(sql, domainClass).getResultList();
  }

}
