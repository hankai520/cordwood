package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import ren.hankai.cordwood.console.persist.model.SidebarItemBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 边栏菜单项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 10:35:04 AM
 */
@Transactional
public interface SidebarItemRepository extends BaseRepository<SidebarItemBean, Integer> {

  /**
   * 边栏菜单查询条件。
   *
   * @author hankai
   * @version 1.0.0
   * @since Mar 12, 2017 2:09:14 PM
   */
  public static final class SidebarItemSpecs {
    /**
     * 根据名称查找菜单项。
     *
     * @param name 名称
     * @return 查询条件
     * @author hankai
     * @since Mar 12, 2017 2:09:30 PM
     */
    public static Specification<SidebarItemBean> namedItem(String name) {
      return new Specification<SidebarItemBean>() {

        @Override
        public Predicate toPredicate(Root<SidebarItemBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.equal(root.get("name"), name);
        }
      };
    }
  }

}
