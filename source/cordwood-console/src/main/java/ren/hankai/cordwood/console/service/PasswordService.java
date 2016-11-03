package ren.hankai.cordwood.console.service;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 密码相关业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 9:39:00 PM
 */
@Service("passwordEncoder") // 名字必须叫 passwordEncoder，这样才会被用作默认密码编码器
public class PasswordService implements PasswordEncoder {

  private final ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();

  @Override
  public String encode(CharSequence rawPassword) {
    return shaPasswordEncoder.encodePassword(rawPassword.toString(), null);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    final String rawPwdString = rawPassword.toString();
    if (encodedPassword.equals(rawPwdString)) {
      return true;
    } else if (encodedPassword.equals(shaPasswordEncoder.encodePassword(rawPwdString, null))) {
      return true;
    }
    return false;
  }

}
