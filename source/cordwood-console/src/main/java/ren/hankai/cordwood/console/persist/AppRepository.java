
package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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
        public Predicate toPredicate(Root<AppBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.and(cb.equal(root.get("appKey"), key),
              cb.equal(root.get("secretKey"), sk));
        }
      };
    }

    /**
     * 搜索应用。
     *
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 26, 2016 11:13:05 AM
     */
    public static Specification<AppBean> search(String keyword) {
      return new Specification<AppBean>() {
        @Override
        public Predicate toPredicate(Root<AppBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          Predicate pre = null;
          if (!StringUtils.isEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            pre = cb.or(
                cb.like(root.<String>get("name"), fuzzyKeyword),
                cb.like(root.<String>get("introduction"), fuzzyKeyword),
                cb.like(root.<String>get("appKey"), fuzzyKeyword),
                cb.like(root.<String>get("secretKey"), fuzzyKeyword));
          }
          return pre;
        }
      };
    }
  }

}
