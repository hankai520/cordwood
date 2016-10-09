
package ren.hankai.cordwood.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.persist.util.CustomJpaRepository;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginPackageRepository
                extends CustomJpaRepository<PluginPackageBean>,
                JpaRepository<PluginPackageBean, Integer> {
}
