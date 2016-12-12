
package ren.hankai.cordwood.console.persist.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.console.persist.PluginRequestRepositoryCustom;
import ren.hankai.cordwood.console.persist.model.PluginRequestBean;
import ren.hankai.cordwood.console.view.model.SummarizedRequest;
import ren.hankai.cordwood.plugin.Plugin;
import ren.hankai.cordwood.plugin.api.PluginManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * 插件访问仓库扩展实现。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 9:17:33 AM
 */
@Component
public class PluginRequestRepositoryImpl implements PluginRequestRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private PluginManager pluginManager;

  @Override
  public long getUserPluginAccessCount(String userEmail, Date beginTime, Date endTime) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    final Root<PluginRequestBean> root = cq.from(PluginRequestBean.class);
    cq.select(cb.count(root));
    cq.where(
        cb.like(root.get("plugin").get("developer"), "%" + userEmail + "%"),
        cb.between(root.get("createTime"), beginTime, endTime));
    final Long result = entityManager.createQuery(cq).getSingleResult();
    return result != null ? result : 0;
  }

  @Override
  public double getUserPluginTimeUsageAvg(String userEmail, Date beginTime, Date endTime) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Double> cq = cb.createQuery(Double.class);
    final Root<PluginRequestBean> root = cq.from(PluginRequestBean.class);
    cq.select(cb.avg(cb.sum(root.get("milliseconds"), 0.0f)));
    cq.where(
        cb.like(root.get("plugin").get("developer"), "%" + userEmail + "%"),
        cb.equal(root.get("succeeded"), true),
        cb.between(root.get("createTime"), beginTime, endTime));
    final Double result = entityManager.createQuery(cq).getSingleResult();
    return result != null ? result : 0;
  }

  @Override
  public long getPluginTotalDataBytes(Date beginTime, Date endTime) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
    final Root<PluginRequestBean> root = cq.from(PluginRequestBean.class);
    cq.multiselect(
        cb.sum(root.get("requestBytes")).alias("requestBytes"),
        cb.sum(root.get("responseBytes")).alias("responseBytes"));
    cq.where(cb.between(root.get("createTime"), beginTime, endTime));
    final Tuple result = entityManager.createQuery(cq).getSingleResult();
    Long reqBytes = result.get("requestBytes", Long.class);
    reqBytes = (reqBytes == null) ? 0 : reqBytes;
    Long resBytes = result.get("responseBytes", Long.class);
    resBytes = (resBytes == null) ? 0 : resBytes;
    return reqBytes + resBytes;
  }

  @Override
  public long getUserPluginDataBytes(String userEmail, Date beginTime, Date endTime) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
    final Root<PluginRequestBean> root = cq.from(PluginRequestBean.class);
    cq.multiselect(
        cb.sum(root.get("requestBytes")).alias("requestBytes"),
        cb.sum(root.get("responseBytes")).alias("responseBytes"));
    cq.where(
        cb.like(root.get("plugin").get("developer"), "%" + userEmail + "%"),
        cb.between(root.get("createTime"), beginTime, endTime));
    final Tuple result = entityManager.createQuery(cq).getSingleResult();
    Long reqBytes = result.get("requestBytes", Long.class);
    reqBytes = (reqBytes == null) ? 0 : reqBytes;
    Long resBytes = result.get("responseBytes", Long.class);
    resBytes = (resBytes == null) ? 0 : resBytes;
    return reqBytes + resBytes;
  }

  @Override
  public List<SummarizedRequest> getRequestsGroupByPlugin(String userEmail, Date beginTime,
      Date endTime) {
    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
    final Root<PluginRequestBean> root = cq.from(PluginRequestBean.class);
    cq.multiselect(
        root.get("plugin").get("name").alias("pluginName"),
        cb.sumAsDouble(root.get("requestBytes")).alias("totalRequestBytes"),
        cb.sumAsDouble(root.get("responseBytes")).alias("totalResponseBytes"),
        cb.count(root).alias("totalCount"),
        cb.max(root.get("createTime")).alias("lastAccessTime"),
        cb.avg(cb.sum(root.get("milliseconds"), 0.0f)).alias("timeUsageAvg"));
    cq.where(cb.equal(root.get("succeeded"), true),
        cb.like(root.get("plugin").get("developer"), "%" + userEmail + "%"),
        cb.between(root.get("createTime"), beginTime, endTime));
    cq.groupBy(root.get("plugin").get("name"));
    final List<Tuple> results = entityManager.createQuery(cq).getResultList();
    final List<SummarizedRequest> list = new ArrayList<>();
    for (final Tuple tuple : results) {
      final SummarizedRequest sr = new SummarizedRequest();
      sr.setAccessCount(tuple.get("totalCount", Long.class));
      sr.setTimeUsageAvg(tuple.get("timeUsageAvg", Double.class));
      sr.setInboundBytes(tuple.get("totalRequestBytes", Double.class));
      sr.setOutboundBytes(tuple.get("totalResponseBytes", Double.class));
      final Plugin plugin = pluginManager.getPlugin(tuple.get("pluginName", String.class));
      sr.setPluginName(plugin.getDisplayName());
      sr.setPluginIsActive(plugin.isActive());
      sr.setLastAccessTime(tuple.get("lastAccessTime", Date.class));
      list.add(sr);
    }
    return list;
  }

}
