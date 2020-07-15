
package ren.hankai.cordwood.data.mybatis.test;

import org.springframework.context.annotation.Configuration;
import ren.hankai.cordwood.data.mybatis.config.AbstractMybatisConfig;
import ren.hankai.cordwood.data.mybatis.test.domain.User;
import ren.hankai.cordwood.data.mybatis.test.mapper.UserMapper;

/**
 * Mybatis配置类。
 *
 * @author hankai
 * @since 1.0.0
 */
@Configuration
public class MybatisConfig extends AbstractMybatisConfig {

  @Override
  protected String[] getMapperPackages() {
    final String pkg = UserMapper.class.getPackage().getName();
    return new String[] {pkg};
  }

  @Override
  protected String getTypeAliasPackage() {
    return User.class.getPackage().getName();
  }

}
