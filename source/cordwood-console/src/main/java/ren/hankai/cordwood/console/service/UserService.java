package ren.hankai.cordwood.console.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.UserRepository;
import ren.hankai.cordwood.console.persist.UserRepository.UserSpecs;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.support.EntitySpecs;
import ren.hankai.cordwood.core.Preferences;

import java.io.File;
import java.util.List;

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

}
