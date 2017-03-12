
package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.domain.Specification;
import ren.hankai.cordwood.console.persist.model.RoleBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 角色仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 7, 2016 1:16:44 PM
 */
public interface RoleRepository extends BaseRepository<RoleBean, Integer> {

  /**
   * 角色查询条件。
   *
   * @author hankai
   * @version 1.0.0
   * @since Mar 12, 2017 2:19:17 PM
   */
  public static final class RoleSpecs {
    /**
     * 根据名称查找角色。
     *
     * @param name 名称
     * @return 查询条件
     * @author hankai
     * @since Mar 12, 2017 2:19:17 PM
     */
    public static Specification<RoleBean> namedRole(String name) {
      return new Specification<RoleBean>() {

        @Override
        public Predicate toPredicate(Root<RoleBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.equal(root.get("name"), name);
        }
      };
    }
  }
}
