
package ren.hankai.cordwood.console.persist;

import ren.hankai.cordwood.console.persist.model.LoginCredentialBean;

import javax.transaction.Transactional;

/**
 * 登录凭证数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2016 3:07:26 PM
 */
@Transactional
public interface LoginCredentialRepository extends BaseRepository<LoginCredentialBean, String> {

}
