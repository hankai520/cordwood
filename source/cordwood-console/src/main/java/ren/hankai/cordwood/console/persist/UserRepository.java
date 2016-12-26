package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.UserBean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 用户数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 4:42:15 PM
 */
@Transactional
public interface UserRepository extends BaseRepository<UserBean, Integer> {

  public static final class UserSpecs {
    /**
     * 查询授权访问的用户。
     *
     * @param email 邮箱
     * @param encryptedPassword 密码（密文）
     * @return 查询条件
     * @author hankai
     * @since Nov 28, 2016 10:18:50 AM
     */
    public static Specification<UserBean> authenticatedUser(final String email,
        final String encryptedPassword) {
      return new Specification<UserBean>() {

        @Override
        public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.and(cb.equal(root.get("email"), email),
              cb.equal(root.get("password"), encryptedPassword));
        }
      };
    }

    /**
     * 按关键字查询用户。
     *
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 7, 2016 9:43:16 AM
     */
    public static Specification<UserBean> search(String keyword) {
      return new Specification<UserBean>() {
        @Override
        public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          Predicate pre = null;
          if (!StringUtils.isEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            pre = cb.or(
                cb.like(root.<String>get("email"), fuzzyKeyword),
                cb.like(root.<String>get("mobile"), fuzzyKeyword),
                cb.like(root.<String>get("name"), fuzzyKeyword));
          }
          return pre;
        }
      };
    }

    /**
     * 邮箱是否已被使用。
     *
     * @param user 用户
     * @return 查询条件
     * @author hankai
     * @since Dec 7, 2016 10:17:25 AM
     */
    public static Specification<UserBean> emailExists(UserBean user) {
      return new Specification<UserBean>() {
        @Override
        public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final Predicate pre = cb.equal(root.get("email"), user.getEmail());
          return pre;
        }
      };
    }

    /**
     * 手机号码是否已被使用。
     *
     * @param user 用户信息
     * @return 查询条件
     * @author hankai
     * @since Dec 7, 2016 2:48:41 PM
     */
    public static Specification<UserBean> mobileExists(UserBean user) {
      return new Specification<UserBean>() {
        @Override
        public Predicate toPredicate(Root<UserBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final Predicate pre = cb.equal(root.get("mobile"), user.getMobile());
          return pre;
        }
      };
    }
  }
}
