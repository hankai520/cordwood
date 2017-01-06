
package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import ren.hankai.cordwood.console.persist.model.PluginRequestBean;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 插件请求记录仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 8, 2016 9:52:07 AM
 */
public interface PluginRequestRepository
    extends BaseRepository<PluginRequestBean, Integer>, PluginRequestRepositoryCustom {

  public static final class PluginRequestSpecs {

    /**
     * 根据关键字搜索插件访问记录。
     *
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 3:24:27 PM
     */
    public static Specification<PluginRequestBean> search(String keyword) {
      return new Specification<PluginRequestBean>() {
        @Override
        public Predicate toPredicate(Root<PluginRequestBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          Predicate pre = null;
          if (!StringUtils.isEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            pre = cb.or(
                cb.like(root.get("plugin").get("name"), fuzzyKeyword),
                cb.like(root.get("plugin").get("displayName"), fuzzyKeyword),
                cb.like(root.get("plugin").get("description"), fuzzyKeyword),
                cb.like(root.get("plugin").get("pluginPackage").get("developer"), fuzzyKeyword),
                cb.like(root.get("clientIp"), fuzzyKeyword),
                cb.like(root.get("requestUrl"), fuzzyKeyword),
                cb.like(root.get("requestDigest"), fuzzyKeyword),
                cb.like(root.get("errors"), fuzzyKeyword));
          }
          return pre;
        }
      };
    }

    /**
     * 根据关键字搜索用户所开发的插件的访问记录。
     *
     * @param userEmail 用户邮箱
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 28, 2016 3:30:37 PM
     */
    public static Specification<PluginRequestBean> searchUserPluginRequests(String userEmail,
        String keyword) {
      return new Specification<PluginRequestBean>() {
        @Override
        public Predicate toPredicate(Root<PluginRequestBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          Predicate pre = cb.like(root.get("plugin").get("pluginPackage").get("developer"),
              "%" + userEmail + "%");
          if (!StringUtils.isEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            final Predicate p = cb.or(
                cb.like(root.get("plugin").get("name"), fuzzyKeyword),
                cb.like(root.get("plugin").get("displayName"), fuzzyKeyword),
                cb.like(root.get("plugin").get("description"), fuzzyKeyword),
                cb.like(root.get("plugin").get("pluginPackage").get("developer"), fuzzyKeyword),
                cb.like(root.get("clientIp"), fuzzyKeyword),
                cb.like(root.get("requestUrl"), fuzzyKeyword),
                cb.like(root.get("requestDigest"), fuzzyKeyword),
                cb.like(root.get("errors"), fuzzyKeyword));
            pre = (pre == null) ? p : cb.and(pre, p);
          }
          return pre;
        }
      };
    }

    /**
     * 查询用户所有的插件请求。
     *
     * @param userEmail 用户邮箱
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 7:40:05 PM
     */
    public static Specification<PluginRequestBean> userPluginRequests(String userEmail,
        Date beginTime, Date endTime) {
      return new Specification<PluginRequestBean>() {
        @Override
        public Predicate toPredicate(Root<PluginRequestBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final String fuzzyEmail = "%" + userEmail + "%";
          return cb.and(
              cb.like(root.get("plugin").get("pluginPackage").get("developer"), fuzzyEmail),
              cb.between(root.get("createTime"), beginTime, endTime));
        }
      };
    }

    /**
     * 查询成功或失败的用户插件请求。
     *
     * @param userEmail 用户邮箱
     * @param succeeded 请求是否成功
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 7:38:02 PM
     */
    public static Specification<PluginRequestBean> userPluginRequests(String userEmail,
        boolean succeeded) {
      return new Specification<PluginRequestBean>() {
        @Override
        public Predicate toPredicate(Root<PluginRequestBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final String fuzzyEmail = "%" + userEmail + "%";
          return cb.and(
              cb.like(root.get("plugin").get("pluginPackage").get("developer"), fuzzyEmail),
              cb.equal(root.get("succeeded"), succeeded));
        }
      };
    }

    /**
     * 查询失败的插件请求。
     *
     * @return 查询条件
     * @author hankai
     * @since Jan 5, 2017 10:12:32 AM
     */
    public static Specification<PluginRequestBean> failedRequests() {
      return new Specification<PluginRequestBean>() {
        @Override
        public Predicate toPredicate(Root<PluginRequestBean> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          return cb.equal(root.get("succeeded"), false);
        }
      };
    }
  }

  /**
   * 查询插件请求的平均用时（毫秒）。
   *
   * @return 平均用时
   * @author hankai
   * @since Jan 5, 2017 9:57:09 AM
   */
  @Query(value = "select avg(cast(o.milliseconds as double)) from PluginRequestBean o")
  public Double getResponseTimeAvg();

  /**
   * 查询有插件访问的天数。
   *
   * @return 访问的天数
   * @author hankai
   * @since Jan 5, 2017 2:34:24 PM
   */
  @Query(value = "select count(distinct(cast(o.createTime as DATE))) from PluginRequestBean o")
  public Long getNumberOfDays();
}
