
package ren.hankai.cordwood.console.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.LoginCredentialRepository;
import ren.hankai.cordwood.console.persist.model.LoginCredentialBean;
import ren.hankai.cordwood.console.persist.support.DeleteSpecification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 登录凭证业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 29, 2016 3:43:29 PM
 */
@Service
public class LoginCredentialService implements PersistentTokenRepository {

  @Autowired
  private LoginCredentialRepository loginCredentialRepo;

  @Override
  public void createNewToken(PersistentRememberMeToken token) {
    final LoginCredentialBean bean = new LoginCredentialBean();
    bean.setUserName(token.getUsername());
    bean.setSeries(token.getSeries());
    bean.setToken(token.getTokenValue());
    bean.setLastUsed(token.getDate());
    loginCredentialRepo.save(bean);
  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {
    final LoginCredentialBean bean = loginCredentialRepo.findOne(series);
    if (bean != null) {
      bean.setLastUsed(lastUsed);
      bean.setToken(tokenValue);
      loginCredentialRepo.save(bean);
    }
  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    final LoginCredentialBean bean = loginCredentialRepo.findOne(seriesId);
    if (bean != null) {
      final PersistentRememberMeToken token = new PersistentRememberMeToken(bean.getUserName(),
          bean.getSeries(), bean.getToken(), bean.getLastUsed());
      return token;
    }
    return null;
  }

  @Override
  public void removeUserTokens(final String username) {
    loginCredentialRepo.delete(new DeleteSpecification<LoginCredentialBean>() {

      @Override
      public Predicate toPredicate(Root<LoginCredentialBean> root, CriteriaDelete<?> query,
          CriteriaBuilder cb) {
        return cb.equal(root.get("userName"), username);
      }
    });
  }

}
