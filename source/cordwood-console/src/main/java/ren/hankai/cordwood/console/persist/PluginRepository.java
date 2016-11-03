
package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.PluginBean;
import ren.hankai.cordwood.console.persist.util.CustomJpaRepository;

/**
 * 插件仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginRepository
    extends CustomJpaRepository<PluginBean>, JpaRepository<PluginBean, Integer> {
}