package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.util.CustomJpaRepository;

/**
 * 用户数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 4:42:15 PM
 */
@Transactional
public interface UserRepository
    extends CustomJpaRepository<UserBean>, JpaRepository<UserBean, Integer> {

}
