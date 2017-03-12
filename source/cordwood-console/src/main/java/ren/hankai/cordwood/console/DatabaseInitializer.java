
package ren.hankai.cordwood.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ren.hankai.cordwood.console.persist.RoleRepository;
import ren.hankai.cordwood.console.persist.RoleRepository.RoleSpecs;
import ren.hankai.cordwood.console.persist.SidebarItemRepository;
import ren.hankai.cordwood.console.persist.SidebarItemRepository.SidebarItemSpecs;
import ren.hankai.cordwood.console.persist.UserRepository;
import ren.hankai.cordwood.console.persist.UserRepository.UserSpecs;
import ren.hankai.cordwood.console.persist.model.RoleBean;
import ren.hankai.cordwood.console.persist.model.SidebarItemBean;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.model.UserBean.UserStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * 数据库初始化器，写入必要的初始数据。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 11:31:14 AM
 */
@Component
public class DatabaseInitializer {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

  @Autowired
  private UserRepository userRepo;
  @Autowired
  private RoleRepository roleRepo;
  @Autowired
  private SidebarItemRepository sidebarItemRepo;

  private void initSidebarItems() {
    final List<SidebarItemBean> items = new ArrayList<>();

    SidebarItemBean item = new SidebarItemBean();
    item.setDisplayText("nav.dashboard");
    item.setIconClasses("fa fa-tachometer fa-fw");
    item.setName("dashboard");
    item.setSink(1);
    item.setUrl("/admin/dashboard");
    if (sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("dashboard")) == null) {
      items.add(item);
    }

    item = new SidebarItemBean();
    item.setDisplayText("nav.plugins");
    item.setIconClasses("fa fa-plug fa-fw");
    item.setName("plugins");
    item.setSink(2);
    item.setUrl("/admin/plugin_packages");
    if (sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("plugins")) == null) {
      items.add(item);
    }

    item = new SidebarItemBean();
    item.setDisplayText("nav.users");
    item.setIconClasses("fa fa-user-circle-o fa-fw");
    item.setName("users");
    item.setSink(3);
    item.setUrl("/admin/users");
    if (sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("users")) == null) {
      items.add(item);
    }

    item = new SidebarItemBean();
    item.setDisplayText("nav.apps");
    item.setIconClasses("fa fa-code fa-fw");
    item.setName("apps");
    item.setSink(4);
    item.setUrl("/admin/apps");
    if (sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("apps")) == null) {
      items.add(item);
    }

    sidebarItemRepo.save(items);
  }

  private void initRoles() {
    final SidebarItemBean dashboardItem =
        sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("dashboard"));
    final SidebarItemBean pluginsItem =
        sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("plugins"));
    final SidebarItemBean usersItem = sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("users"));
    final SidebarItemBean appsItem = sidebarItemRepo.findOne(SidebarItemSpecs.namedItem("apps"));

    final List<RoleBean> roles = new ArrayList<>();

    RoleBean role = new RoleBean();
    role.setName("ROLE_ADMIN");
    role.setSidebarItems(Arrays.asList(dashboardItem, pluginsItem, usersItem, appsItem));
    if (roleRepo.findOne(RoleSpecs.namedRole("ROLE_ADMIN")) == null) {
      roles.add(role);
    }

    role = new RoleBean();
    role.setName("ROLE_CONFIG");
    role.setSidebarItems(Arrays.asList(dashboardItem, pluginsItem));
    if (roleRepo.findOne(RoleSpecs.namedRole("ROLE_CONFIG")) == null) {
      roles.add(role);
    }

    roleRepo.save(roles);
  }

  private void initUsers() {
    final List<UserBean> users = new ArrayList<>();

    UserBean user = new UserBean();
    user.setEmail("sa@sparksoft.com.cn");
    user.setName("超级管理员");
    user.setAboutMe("超级管理员");
    user.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");// 123456
    user.setStatus(UserStatus.Enabled);
    user.setCreateTime(new Date());
    final RoleBean saRole = roleRepo.findOne(RoleSpecs.namedRole("ROLE_ADMIN"));
    user.setRoles(Arrays.asList(saRole));
    if (userRepo.findOne(UserSpecs.emailExists(user)) == null) {
      users.add(user);
    }

    user = new UserBean();
    user.setEmail("cfg@sparksoft.com.cn");
    user.setName("系统配置员");
    user.setAboutMe("系统配置员");
    user.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");// 123456
    user.setStatus(UserStatus.Enabled);
    user.setCreateTime(new Date());
    final RoleBean cfgRole = roleRepo.findOne(RoleSpecs.namedRole("ROLE_CONFIG"));
    user.setRoles(Arrays.asList(cfgRole));
    if (userRepo.findOne(UserSpecs.emailExists(user)) == null) {
      users.add(user);
    }

    userRepo.save(users);
  }

  @PostConstruct
  @Transactional
  private void initData() throws Exception {
    try {
      // 创建内置菜单项
      initSidebarItems();
      // 创建内置角色
      initRoles();
      // 创建默认帐号，避免帐号意外丢失造成无法登录。
      initUsers();
    } catch (final Exception ex) {
      logger.error("Failed to set up initial data for database.", ex);
    }
  }
}
