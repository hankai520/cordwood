
package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.domain.Specification;

import ren.hankai.cordwood.console.persist.model.AppBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 应用仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 9:51:27 AM
 */
public interface AppRepository extends BaseRepository<AppBean, Integer> {

  public static final class AppSpecs {
    /**
     * 查找已登记的应用。
     *
     * @param key 应用标识
     * @param sk 应用秘钥
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 2:36:16 PM
     */
    public static Specification<AppBean> identifiedApp(String key, String sk) {
      return new Specification<AppBean>() {
        @Override
        public Predicate toPredicate(Root<AppBean> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
          return cb.and(cb.equal(root.get("appKey"), key),
              cb.equal(root.get("secretKey"), sk));
        }
      };
    }
  }

}
