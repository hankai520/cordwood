
package ren.hankai.cordwood.oauth2.test.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 17, 2020 9:04:25 AM
 */
@Service
public class MockUserDetailsService implements UserDetailsService {

  public static final String USER_NAME = "admin";
  public static final String USER_PWD = "123456";

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final List<GrantedAuthority> auths = new ArrayList<>();
    auths.add(new SimpleGrantedAuthority("ADMIN"));
    return new User(USER_NAME, passwordEncoder.encode(USER_PWD), auths);
  }

}
