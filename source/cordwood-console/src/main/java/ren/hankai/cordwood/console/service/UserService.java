package ren.hankai.cordwood.console.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.UserRepository;
import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.util.EntitySpecs;

/**
 * 用户认证服务。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 4:38:21 PM
 */
@Service("userDetailsService")
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final UserBean userBean = userRepo.findOne(EntitySpecs.field("loginId", username));
    if (userBean == null) {
      throw new UsernameNotFoundException(
          String.format("User with loginid \"%s\" not found!", username));
    }
    return userBean;
  }

}
