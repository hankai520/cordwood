
package ren.hankai.cordwood.console.persist;


import ren.hankai.cordwood.console.view.model.ChannelRequest;
import ren.hankai.cordwood.console.view.model.RequestCountAndVolume;
import ren.hankai.cordwood.console.view.model.SummarizedRequest;

import java.util.Date;
import java.util.List;

/**
 * 插件访问仓库扩展接口。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 12, 2016 9:12:48 AM
 */
public interface PluginRequestRepositoryCustom {

  /**
   * 获取用户插件访问量。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 访问次数
   * @author hankai
   * @since Dec 12, 2016 1:55:20 PM
   */
  long getUserPluginAccessCount(String userEmail, Date beginTime, Date endTime);

  /**
   * 获取用户插件的平均响应时间。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 平均响应时间（毫秒）
   * @author hankai
   * @since Dec 12, 2016 1:40:34 PM
   */
  double getUserPluginTimeUsageAvg(String userEmail, Date beginTime, Date endTime);

  /**
   * 所有插件的流量和（请求+响应数据字节数）。
   *
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 流量和
   * @author hankai
   * @since Dec 12, 2016 1:41:53 PM
   */
  long getPluginTotalDataBytes(Date beginTime, Date endTime);

  /**
   * 获取用户插件流量和。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 流量和
   * @author hankai
   * @since Dec 12, 2016 1:42:48 PM
   */
  long getUserPluginDataBytes(String userEmail, Date beginTime, Date endTime);

  /**
   * 获取按插件分组汇总后的访问统计信息。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 汇总后的插件访问信息
   * @author hankai
   * @since Dec 12, 2016 1:26:24 PM
   */
  public List<SummarizedRequest> getRequestsGroupByPlugin(String userEmail, Date beginTime,
      Date endTime);

  /**
   * 获取按渠道分组汇总后的插件访问数。
   *
   * @param userEmail 用户邮箱
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 分组汇总后的插件访问数
   * @author hankai
   * @since Dec 14, 2016 11:37:55 AM
   */
  public ChannelRequest getRequestCountGroupByChannel(String userEmail, Date beginTime,
      Date endTime);

  /**
   * 获取插件访问的次数和流量数据。
   *
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @return 按日期汇总后的数据
   * @author hankai
   * @since Jan 3, 2017 1:50:40 PM
   */
  public List<RequestCountAndVolume> getRequestCountAndVolume(Date beginTime, Date endTime);
}
