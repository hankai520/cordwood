
package ren.hankai.cordwood.console.persist;

import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.PluginPackageBean;

/**
 * 插件包数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginPackageRepository extends BaseRepository<PluginPackageBean, String> {
}
