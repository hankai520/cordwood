
package ren.hankai.cordwood.data.mybatis.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ren.hankai.cordwood.data.mybatis.test.domain.User;

/**
 * 测试用户Mapper。
 *
 * @author hankai
 * @since 1.0.0
 */
@Mapper
public interface UserMapper {

  void initDatabaseTables();

  void dropDatabaseTables();

  void addUser(@Param("user") User user);

  User getUserByName(@Param("name") String name);

  void deleteAll();
}
