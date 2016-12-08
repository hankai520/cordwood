
package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ren.hankai.cordwood.console.persist.model.PluginRequest;

import java.util.Calendar;
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
public interface PluginRequestRepository extends BaseRepository<PluginRequest, Integer> {

  @Query("select avg(cast(o.milliseconds as float)) from PluginRequest o where o.plugin.developer like :userEmail and o.createTime between :beginTime and :endTime")
  Double timeUsageAverage(@Param("userEmail") String userEmail, @Param("beginTime") Date beginTime,
      @Param("endTime") Date endTime);

  public static final class PluginRequestSpecs {

    /**
     * 根据关键字搜索插件访问记录。
     *
     * @param keyword 关键字
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 3:24:27 PM
     */
    public static Specification<PluginRequest> search(String keyword) {
      return new Specification<PluginRequest>() {
        @Override
        public Predicate toPredicate(Root<PluginRequest> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          Predicate pre = null;
          if (!StringUtils.isEmpty(keyword)) {
            final String fuzzyKeyword = "%" + keyword + "%";
            pre = cb.or(
                cb.like(root.get("plugin").get("name"), fuzzyKeyword),
                cb.like(root.get("plugin").get("displayName"), fuzzyKeyword),
                cb.like(root.get("plugin").get("description"), fuzzyKeyword),
                cb.like(root.get("plugin").get("developer"), fuzzyKeyword),

                cb.like(root.get("app").get("name"), fuzzyKeyword),
                cb.like(root.get("app").get("introduction"), fuzzyKeyword),
                cb.like(root.get("app").get("appKey"), fuzzyKeyword),
                cb.like(root.get("app").get("secretKey"), fuzzyKeyword),

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
     * 今天的插件访问记录。
     *
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 6:35:14 PM
     */
    public static Specification<PluginRequest> requestsOfToday() {
      return new Specification<PluginRequest>() {

        @Override
        public Predicate toPredicate(Root<PluginRequest> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final Calendar cal = Calendar.getInstance();
          cal.set(Calendar.HOUR_OF_DAY, 0);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          cal.set(Calendar.MILLISECOND, 0);
          final Date beginTime = cal.getTime();

          cal.set(Calendar.HOUR_OF_DAY, 23);
          cal.set(Calendar.MINUTE, 59);
          cal.set(Calendar.SECOND, 59);
          cal.set(Calendar.MILLISECOND, 999);
          final Date endTime = cal.getTime();

          return cb.between(root.get("createTime"), beginTime, endTime);
        }
      };
    }

    /**
     * 查询用户所有的插件请求。
     * 
     * @param userEmail 用户邮箱
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 7:40:05 PM
     */
    public static Specification<PluginRequest> userPluginRequests(String userEmail) {
      return new Specification<PluginRequest>() {
        @Override
        public Predicate toPredicate(Root<PluginRequest> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final String fuzzyEmail = "%" + userEmail + "%";
          return cb.like(root.get("plugin").get("developer"), fuzzyEmail);
        }
      };
    }

    /**
     * 用户当天的插件访问记录。
     *
     * @param userEmail 用户邮箱
     * @return 查询条件
     * @author hankai
     * @since Dec 8, 2016 5:34:36 PM
     */
    public static Specification<PluginRequest> usersPluginRequestsToday(String userEmail) {
      return Specifications.where(requestsOfToday()).and(userPluginRequests(userEmail));
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
    public static Specification<PluginRequest> userPluginRequests(String userEmail,
        boolean succeeded) {
      return new Specification<PluginRequest>() {
        @Override
        public Predicate toPredicate(Root<PluginRequest> root, CriteriaQuery<?> query,
            CriteriaBuilder cb) {
          final String fuzzyEmail = "%" + userEmail + "%";
          return cb.and(cb.like(root.get("plugin").get("developer"), fuzzyEmail),
              cb.equal(root.get("succeeded"), succeeded));
        }
      };
    }
  }

}
