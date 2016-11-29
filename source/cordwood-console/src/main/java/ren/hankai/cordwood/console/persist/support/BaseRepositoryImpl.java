package ren.hankai.cordwood.console.persist.support;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import ren.hankai.cordwood.console.persist.BaseRepository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
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

  private final EntityManager entityManager;
  private final Class<T> domainClass;

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

}
