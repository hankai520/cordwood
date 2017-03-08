
package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ren.hankai.cordwood.console.persist.model.PluginPackageBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 插件包数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginPackageRepository extends BaseRepository<PluginPackageBean, String> {

  public static final class PluginPackageSpecs {

    /**
     * 搜索插件包。
     *
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 26, 2016 11:13:28 AM
     */
    public static Specification<PluginPackageBean> search(String keyword) {
      return new Specification<PluginPackageBean>() {
        @Override
        public Predicate toPredicate(Root<PluginPackageBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          if (StringUtils.isNotEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            return cb.or(
                cb.like(root.get("id"), fuzzyKeyword),
                cb.like(root.get("fileName"), fuzzyKeyword),
                cb.like(root.get("developer"), fuzzyKeyword),
                cb.like(root.get("description"), fuzzyKeyword));
          }
          return null;
        }
      };
    }

    /**
     * 搜索指定用户开发的插件包。
     *
     * @param developer 开发者（用户）
     * @return 查询条件
     * @author hankai
     * @since Dec 26, 2016 11:13:47 AM
     */
    public static Specification<PluginPackageBean> userPluginPackages(String developer) {
      return new Specification<PluginPackageBean>() {
        @Override
        public Predicate toPredicate(Root<PluginPackageBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final String fuzzyDeveloper = "%" + developer + "%";
          return cb.like(root.get("developer"), fuzzyDeveloper);
        }
      };
    }
  }

  @Query(value = "select count(distinct(o.developer)) from PluginPackageBean o")
  public long countDevelopers();

}
