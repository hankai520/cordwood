
package ren.hankai.cordwood.oauth2.token.store;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;

import java.util.Collection;

/**
 * 通过数据库存储OAuth2授权信息。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 18, 2020 5:31:52 PM
 */
public class JdbcApprovalStore implements ApprovalStore {

  @Override
  public boolean addApprovals(final Collection<Approval> approvals) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean revokeApprovals(final Collection<Approval> approvals) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Collection<Approval> getApprovals(final String userId, final String clientId) {
    // TODO Auto-generated method stub
    return null;
  }

}
