
package ren.hankai.cordwood.console.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ren.hankai.cordwood.console.persist.AppRepository;
import ren.hankai.cordwood.console.persist.AppRepository.AppSpecs;
import ren.hankai.cordwood.console.persist.model.AppBean;
import ren.hankai.cordwood.console.persist.support.EntitySpecs;

import java.util.List;

/**
 * App 业务逻辑。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 9:53:13 AM
 */
@Service
public class AppService {

  @Autowired
  private AppRepository appRepo;

  /**
   * 根据 app key 查找应用。
   *
   * @param appKey 应用唯一标识
   * @return 应用信息
   * @author hankai
   * @since Dec 8, 2016 11:20:34 AM
   */
  public AppBean getAppByAppKey(String appKey) {
    return appRepo.findOne(EntitySpecs.field("appKey", appKey));
  }

  /**
   * 查找登记的应用。
   * 
   * @param appKey 应用标识
   * @param secretKey 应用秘钥
   * @return 应用信息
   * @author hankai
   * @since Dec 8, 2016 2:39:34 PM
   */
  public AppBean getIdentifiedApp(String appKey, String secretKey) {
    final List<AppBean> apps = appRepo.findAll(AppSpecs.identifiedApp(appKey, secretKey));
    if ((apps != null) && (apps.size() > 0)) {
      return apps.get(0);
    }
    return null;
  }

}
