package ren.hankai.cordwood.console.persist;

import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.SidebarItemBean;

/**
 * 边栏菜单项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 10:35:04 AM
 */
@Transactional
public interface SidebarRepository extends BaseRepository<SidebarItemBean, Integer> {

}
