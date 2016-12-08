
package ren.hankai.cordwood.console.persist;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ren.hankai.cordwood.console.persist.model.PluginRequest;

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
  }

}
