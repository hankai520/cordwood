
package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.domain.Specification;
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

}
