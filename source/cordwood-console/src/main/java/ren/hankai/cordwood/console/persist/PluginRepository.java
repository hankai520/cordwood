
package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.util.CustomJpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 插件仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginRepository
    extends CustomJpaRepository<PluginBean>, JpaRepository<PluginBean, String> {

  public static final class PluginSpecs {
    public static Specification<PluginBean> ownedBy(final String packageId) {
      return new Specification<PluginBean>() {
        @Override
        public Predicate toPredicate(Root<PluginBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.equal(root.get("pluginPackage").get("id"), packageId);
        }

      };
    }
  }

}
