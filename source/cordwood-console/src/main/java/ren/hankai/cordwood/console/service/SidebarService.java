package ren.hankai.cordwood.console.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.model.RoleBean;
import ren.hankai.cordwood.console.persist.model.SidebarItemBean;
import ren.hankai.cordwood.console.persist.model.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 边栏菜单业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 10:58:02 AM
 */
@Service
public class SidebarService {

  /**
   * 获取当前用户可访问的边栏菜单项。
   *
   * @return 菜单项集合
   * @author hankai
   * @since Nov 4, 2016 10:58:58 AM
   */
  public List<SidebarItemBean> getAvailableBarItems() {
    final List<SidebarItemBean> items = new ArrayList<>();
    final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserBean) {
      final UserBean user = (UserBean) principal;
      for (final RoleBean rb : user.getRoles()) {
        items.addAll(rb.getSidebarItems());
      }
    }
    return items;
  }

}
