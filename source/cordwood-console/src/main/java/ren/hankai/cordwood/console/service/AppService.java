
package ren.hankai.cordwood.console.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ren.hankai.cordwood.console.persist.AppRepository;
import ren.hankai.cordwood.console.persist.AppRepository.AppSpecs;
import ren.hankai.cordwood.console.persist.model.AppBean;
import ren.hankai.cordwood.console.persist.support.EntitySpecs;
import ren.hankai.cordwood.core.Preferences;

import java.util.List;
import java.util.UUID;

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

  /**
   * 搜索应用。
   *
   * @param keyword 关键字
   * @param pageable 分页
   * @return 应用集合
   * @author hankai
   * @since Dec 23, 2016 11:33:55 AM
   */
  public Page<AppBean> search(String keyword, Pageable pageable) {
    final Specification<AppBean> spec = AppSpecs.search(keyword);
    return appRepo.findAll(spec, pageable);
  }

  /**
   * 检查应用名称是否已被使用。
   *
   * @param app 应用
   * @param excludeSelf 是否忽略自身
   * @return 名称是否已被使用
   * @author hankai
   * @since Dec 23, 2016 1:14:26 PM
   */
  public boolean isNameDuplicated(AppBean app, boolean excludeSelf) {
    final List<AppBean> apps = appRepo.findAll(EntitySpecs.field("name", app.getName()));
    boolean hasDuplicates = false;
    if (excludeSelf && (app.getId() != null)) {
      for (final AppBean ab : apps) {
        if (!ab.getId().equals(app.getId())) {
          hasDuplicates = true;
          break;
        }
      }
    } else {
      hasDuplicates = (apps.size() > 0);
    }
    return hasDuplicates;
  }

  /**
   * 添加或更新应用信息。
   *
   * @param app 应用
   * @return 更新后的应用信息
   * @author hankai
   * @since Dec 23, 2016 1:19:00 PM
   */
  @Transactional
  public AppBean save(AppBean app) {
    return appRepo.save(app);
  }

  /**
   * 根据应用ID查找应用。
   *
   * @param id 应用ID
   * @return 应用信息
   * @author hankai
   * @since Dec 23, 2016 1:35:59 PM
   */
  public AppBean getAppById(Integer id) {
    return appRepo.findOne(id);
  }

  /**
   * 根据ID删除应用。
   *
   * @param id 应用ID
   * @author hankai
   * @since Dec 23, 2016 1:41:07 PM
   */
  @Transactional
  public void deleteAppById(Integer id) {
    appRepo.delete(id);
  }

  /**
   * 随机生成应用标识。
   *
   * @return 应用标识
   * @author hankai
   * @since Dec 23, 2016 1:22:31 PM
   */
  public String generateAppKey() {
    String key = "appkey:" + UUID.randomUUID().toString()
        + System.currentTimeMillis()
        + Preferences.getSystemSk();
    key = DigestUtils.md5Hex(key);
    return key;
  }

  /**
   * 随机生成应用秘钥。
   *
   * @return 应用秘钥
   * @author hankai
   * @since Dec 23, 2016 1:22:29 PM
   */
  public String generateSecretKey() {
    String sk = "appsk:" + UUID.randomUUID().toString()
        + System.currentTimeMillis()
        + Preferences.getSystemSk();
    sk = DigestUtils.md5Hex(sk);
    return sk;
  }

}
