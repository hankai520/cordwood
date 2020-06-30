
package ren.hankai.cordwood.data.jpa.repository;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends BaseRepository<UserBean, String> {

}
