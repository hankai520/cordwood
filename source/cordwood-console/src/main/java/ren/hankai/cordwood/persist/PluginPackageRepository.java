
package ren.hankai.cordwood.persist;

import ren.hankai.cordwood.persist.model.PluginPackageBean;
import ren.hankai.cordwood.persist.util.CustomJpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 插件包数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginPackageRepository
    extends CustomJpaRepository<PluginPackageBean>, JpaRepository<PluginPackageBean, Integer> {
}