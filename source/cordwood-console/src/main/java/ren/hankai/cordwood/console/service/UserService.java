package ren.hankai.cordwood.console.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.RoleRepository;
import ren.hankai.cordwood.console.persist.UserRepository;
import ren.hankai.cordwood.console.persist.UserRepository.UserSpecs;
import ren.hankai.cordwood.console.persist.model.RoleBean;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.support.EntitySpecs;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

/**
 * 用户认证服务。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 4:38:21 PM
 */
@Service("userDetailsService")
public class UserService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepo;
  @Autowired
  private RoleRepository roleRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final UserBean userBean = userRepo.findOne(EntitySpecs.field("email", username));
    if (userBean == null) {
      throw new UsernameNotFoundException(
          String.format("User with email \"%s\" not found!", username));
    }
    return userBean;
  }

  /**
   * 查询已授权访问的用户。
   *
   * @param email 邮箱
   * @param encryptedPassword 密码（密文）
   * @return 用户信息
   * @author hankai
   * @since Nov 28, 2016 10:16:56 AM
   */
  public UserBean getAuthenticatedUser(String email, String encryptedPassword) {
    final Sort sort = new Sort(Direction.DESC, "id");
    final List<UserBean> users =
        userRepo.findAll(UserSpecs.authenticatedUser(email, encryptedPassword), sort);
    if ((users != null) && (users.size() > 0)) {
      if (users.size() > 1) {
        logger.error(String.format("Found duplicate user with email \"%s\"", email));
      }
      return users.get(0);
    }
    return null;
  }

  /**
   * 根据用户标识查找用户。
   *
   * @param userId 用户唯一标识
   * @return 用户信息
   * @author hankai
   * @since Nov 30, 2016 9:42:25 AM
   */
  public UserBean getUserById(Integer userId) {
    return userRepo.findOne(userId);
  }

  /**
   * 按用户邮箱查找用户。
   * 
   * @param email 邮箱
   * @return 用户信息
   * @author hankai
   * @since Dec 8, 2016 5:28:31 PM
   */
  public UserBean getUserByEmail(String email) {
    return userRepo.findOne(EntitySpecs.field("email", email));
  }

  /**
   * 更新用户信息。
   *
   * @param user 用户信息
   * @return 更新后的用户
   * @author hankai
   * @since Nov 30, 2016 10:47:14 AM
   */
  public UserBean updateUserInfo(UserBean user) {
    return userRepo.save(user);
  }

  /**
   * 获取用户头像。
   *
   * @param user 用户
   * @return 头像文件
   * @author hankai
   * @since Nov 30, 2016 5:25:12 PM
   */
  public File getUserAvatar(UserBean user) {
    final String fileName = String.format("avatar_%d.jpg", user.getId());
    final String path = Preferences.getAttachmentDir() + File.separator + fileName;
    final File file = new File(path);
    if (file.exists() && file.isFile()) {
      return file;
    }
    return null;
  }

  /**
   * 搜索用户。
   *
   * @param keyword 关键字
   * @param pageable 分页
   * @return 搜索结果
   * @author hankai
   * @since Dec 7, 2016 9:44:55 AM
   */
  public Page<UserBean> search(String keyword, Pageable pageable) {
    final Specification<UserBean> spec = UserSpecs.search(keyword);
    return userRepo.findAll(spec, pageable);
  }

  /**
   * 检查邮箱是否已被使用。
   *
   * @param user 用户
   * @param excludeSelf 是否排除自身信息（比如在更新用户信息时，检查是否重复，需要排除自身，否则一定会重复）
   * @return 是否已被使用
   * @author hankai
   * @since Dec 7, 2016 10:20:21 AM
   */
  public boolean isEmailDuplicated(UserBean user, boolean excludeSelf) {
    final List<UserBean> users = userRepo.findAll(UserSpecs.emailExists(user));
    boolean hasDuplicates = false;
    if (excludeSelf && (user.getId() != null)) {
      for (final UserBean ub : users) {
        if (!ub.getId().equals(user.getId())) {
          hasDuplicates = true;
          break;
        }
      }
    } else {
      hasDuplicates = (users.size() > 0);
    }
    return hasDuplicates;
  }

  /**
   * 检查邮箱是否已被使用。
   *
   * @param user 用户
   * @param excludeSelf 是否排除自身信息（比如在更新用户信息时，检查是否重复，需要排除自身，否则一定会重复）
   * @return 是否已被使用
   * @author hankai
   * @since Dec 7, 2016 2:52:29 PM
   */
  public boolean isMobileDuplicated(UserBean user, boolean excludeSelf) {
    final List<UserBean> users = userRepo.findAll(UserSpecs.mobileExists(user));
    boolean hasDuplicates = false;
    if (excludeSelf && (user.getId() != null)) {
      for (final UserBean ub : users) {
        if (!ub.getId().equals(user.getId())) {
          hasDuplicates = true;
          break;
        }
      }
    } else {
      hasDuplicates = (users.size() > 0);
    }
    return hasDuplicates;
  }

  /**
   * 保存用户信息。
   *
   * @param user 用户信息
   * @return 保存后的用户信息
   * @author hankai
   * @since Dec 7, 2016 11:11:55 AM
   */
  @Transactional
  public UserBean save(UserBean user) {
    return userRepo.save(user);
  }

  /**
   * 删除用户。
   *
   * @param userId 用户ID
   * @author hankai
   * @since Dec 7, 2016 4:57:44 PM
   */
  @Transactional
  public void deleteUser(Integer userId) {
    userRepo.delete(userId);
  }

  /**
   * 获取系统当前定义的所有角色。
   *
   * @return 角色列表
   * @author hankai
   * @since Dec 7, 2016 1:17:47 PM
   */
  public List<RoleBean> getAvailableRoles() {
    return roleRepo.findAll();
  }
}
