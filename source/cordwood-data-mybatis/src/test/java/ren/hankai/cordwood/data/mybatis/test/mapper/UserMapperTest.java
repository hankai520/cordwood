
package ren.hankai.cordwood.data.mybatis.test.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ren.hankai.cordwood.data.mybatis.test.DataMybatisTestSupport;
import ren.hankai.cordwood.data.mybatis.test.domain.User;

/**
 * 用户Mapper测试。
 *
 * @author hankai
 * @since 1.0.0
 */
public class UserMapperTest extends DataMybatisTestSupport {

  @Autowired
  private UserMapper mapper;

  @Before
  public void setup() {
    mapper.initDatabaseTables();
  }

  @After
  public void teardown() {
    mapper.dropDatabaseTables();
  }

  @Test
  public void testGetUserByName() {
    final User user = new User();
    user.setName("张三");
    user.setAge(30);
    mapper.addUser(user);

    final User gotYou = mapper.getUserByName(user.getName());
    assertNotNull(gotYou);
    assertEquals(user.getName(), gotYou.getName());
    assertEquals(user.getAge(), gotYou.getAge());
  }

}
