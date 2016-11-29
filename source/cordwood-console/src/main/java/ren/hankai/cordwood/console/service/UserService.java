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

}
