
package ren.hankai.cordwood.data.jpa.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import ren.hankai.cordwood.data.jpa.domain.DeleteSpecification;
import ren.hankai.cordwood.data.jpa.domain.EntitySpecs;
import ren.hankai.cordwood.data.test.DataTestSupport;
import ren.hankai.cordwood.data.util.PageUtil;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * JPA仓库基类测试。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 3, 2018 3:06:20 PM
 */
public class BaseRepositoryTest extends DataTestSupport {

  @Autowired
  private UserRepository userRepo;
  @Autowired
  private EntityManager entityManager;

  /**
   * 初始化测试数据。
   *
   * @throws Exception 异常
   * @author hankai
   * @since Dec 3, 2018 1:46:47 PM
   */
  @Before
  public void initData() throws Exception {
    final UserBean user = new UserBean();
    for (int i = 0; i < 2; i++) {
      user.setUserName("user" + i);
      user.setAddress("addr" + i);
      userRepo.save(user);
    }
  }

  @Test
  public void testDetach() {
    final UserBean user = userRepo.getOne("user0");
    Assert.assertNotNull(user);
    userRepo.detach(user);
    Assert.assertFalse(entityManager.contains(user));
  }

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void testDeleteDeleteSpecificationOfT() {
    userRepo.delete(new DeleteSpecification<UserBean>() {

      @Override
      public Predicate toPredicate(Root<UserBean> root, CriteriaDelete<?> query,
          CriteriaBuilder cb) {
        return cb.equal(root.get("userName"), "user0");
      }
    });
    userRepo.getOne("user0");
  }

  @Test
  public void testFindAllSpecificationOfT() {
    final List<UserBean> list = userRepo.findAll(EntitySpecs.field("userName", "user0"));
    Assert.assertTrue(list.size() == 1);
    final UserBean user = list.get(0);
    Assert.assertEquals("user0", user.getUserName());
    Assert.assertEquals("addr0", user.getAddress());
  }

  @Test
  public void testFindAllSpecificationOfTSort() {
    final Specification<UserBean> spec =
        EntitySpecs.fieldIn("userName", Arrays.asList("user0", "user1"));
    final Sort sort = new Sort(Direction.DESC, "address");
    final List<UserBean> users = userRepo.findAll(spec, sort);
    Assert.assertEquals(2, users.size());
    Assert.assertEquals("user1", users.get(0).getUserName());
    Assert.assertEquals("addr1", users.get(0).getAddress());
    Assert.assertEquals("user0", users.get(1).getUserName());
    Assert.assertEquals("addr0", users.get(1).getAddress());
  }

  @Test
  public void testFindAllSpecificationOfTPageable() {
    final Specification<UserBean> spec =
        EntitySpecs.fieldIn("userName", Arrays.asList("user0", "user1"));
    Pageable pageable = PageUtil.pageWithIndexAndSize(1, 1, true, "userName");
    Page<UserBean> results = userRepo.findAll(spec, pageable);
    Assert.assertEquals(2, results.getTotalElements());
    Assert.assertEquals(1, results.getContent().size());
    Assert.assertEquals("user0", results.getContent().get(0).getUserName());
    Assert.assertEquals("addr0", results.getContent().get(0).getAddress());

    pageable = PageUtil.pageWithIndexAndSize(2, 1, true, "userName");
    results = userRepo.findAll(spec, pageable);
    Assert.assertEquals(2, results.getTotalElements());
    Assert.assertEquals(1, results.getContent().size());
    Assert.assertEquals("user1", results.getContent().get(0).getUserName());
    Assert.assertEquals("addr1", results.getContent().get(0).getAddress());
  }

  @Test
  public void testCountSpecificationOfT() {
    final long count = userRepo.count(EntitySpecs.fieldNotIn("userName", Arrays.asList("user1")));
    Assert.assertEquals(1, count);
  }

  @Test
  public void testFindOneSpecificationOfT() {
    UserBean user = userRepo.findOne(EntitySpecs.field("userName", "user0"));
    Assert.assertEquals("user0", user.getUserName());
    Assert.assertEquals("addr0", user.getAddress());
    user = userRepo.findOne(EntitySpecs.field("userName", "abc"));
    Assert.assertNull(user);
  }

  @Test
  public void testFindOneSpecificationOfTSort() {
    final Sort sort = new Sort(Direction.DESC, "address");
    final UserBean user = userRepo.findOne(new Specification<UserBean>() {

      @Override
      public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        return cb.like(root.get("userName"), "user%");
      }

    }, sort);
    Assert.assertEquals("user1", user.getUserName());
    Assert.assertEquals("addr1", user.getAddress());
  }

  @Test
  public void testFindFirst() {
    final UserBean user = userRepo.findFirst(new Specification<UserBean>() {

      @Override
      public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        return cb.like(root.get("userName"), "user%");
      }

    });
    Assert.assertEquals("user0", user.getUserName());
    Assert.assertEquals("addr0", user.getAddress());
  }

}
