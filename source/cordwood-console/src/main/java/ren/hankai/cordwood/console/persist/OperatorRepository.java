package ren.hankai.cordwood.console.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import ren.hankai.cordwood.console.persist.model.UserBean;
import ren.hankai.cordwood.console.persist.util.CustomJpaRepository;

import javax.transaction.Transactional;

/**
 * 运维账号仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 1:56:02 PM
 */
@Transactional
public interface OperatorRepository
    extends CustomJpaRepository<UserBean>, JpaRepository<UserBean, Integer> {

}
