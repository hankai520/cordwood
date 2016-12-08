
package ren.hankai.cordwood.console.persist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ren.hankai.cordwood.jackson.DateTimeSerializer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * 插件访问记录。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 7, 2016 5:20:35 PM
 */
@Entity
@Table(name = "plugin_requests")
@Cacheable(false)
public class PluginRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "pluginName", referencedColumnName = "name", nullable = false)
  private PluginBean plugin;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "appId", referencedColumnName = "id")
  private App app;
  @Column(length = 40, nullable = false)
  @Size(min = 1, max = 20)
  private String clientIp;
  @Column(length = 400, nullable = false)
  @Size(min = 1, max = 360)
  private String requestUrl;
  @Column(length = 10, nullable = false)
  @Size(min = 1, max = 10)
  private String requestMethod;
  @Column(length = 400)
  @Size(max = 360)
  private String requestDigest;
  @Column(nullable = false)
  private long requestBytes;
  @Column(nullable = false)
  private long responseBytes;
  @Column(nullable = false)
  private long milliseconds;
  @Column(nullable = false)
  private boolean succeeded;
  @Column(nullable = false)
  private int responseCode;
  private String errors;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime;

  public String getPluginDisplayName() {
    return plugin.getDisplayName();
  }

  public String getAppName() {
    if (app != null) {
      return app.getName();
    }
    return null;
  }

  /**
   * 获取 id 字段的值。
   *
   * @return id 字段值
   */
  public Integer getId() {
    return id;
  }

  /**
   * 设置 id 字段的值。
   *
   * @param id id 字段的值
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 获取 plugin 字段的值。
   *
   * @return plugin 字段值
   */
  @JsonIgnore
  public PluginBean getPlugin() {
    return plugin;
  }

  /**
   * 设置 plugin 字段的值。
   *
   * @param plugin plugin 字段的值
   */
  public void setPlugin(PluginBean plugin) {
    this.plugin = plugin;
  }

  /**
   * 获取 app 字段的值。
   *
   * @return app 字段值
   */
  @JsonIgnore
  public App getApp() {
    return app;
  }

  /**
   * 设置 app 字段的值。
   *
   * @param app app 字段的值
   */
  public void setApp(App app) {
    this.app = app;
  }

  /**
   * 获取 clientIp 字段的值。
   *
   * @return clientIp 字段值
   */
  public String getClientIp() {
    return clientIp;
  }

  /**
   * 设置 clientIp 字段的值。
   *
   * @param clientIp clientIp 字段的值
   */
  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  /**
   * 获取 requestUrl 字段的值。
   *
   * @return requestUrl 字段值
   */
  public String getRequestUrl() {
    return requestUrl;
  }

  /**
   * 设置 requestUrl 字段的值。
   *
   * @param requestUrl requestUrl 字段的值
   */
  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  /**
   * 获取 requestMethod 字段的值。
   *
   * @return requestMethod 字段值
   */
  public String getRequestMethod() {
    return requestMethod;
  }

  /**
   * 设置 requestMethod 字段的值。
   *
   * @param requestMethod requestMethod 字段的值
   */
  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  /**
   * 获取 requestDigest 字段的值。
   *
   * @return requestDigest 字段值
   */
  public String getRequestDigest() {
    return requestDigest;
  }

  /**
   * 设置 requestDigest 字段的值。
   *
   * @param requestDigest requestDigest 字段的值
   */
  public void setRequestDigest(String requestDigest) {
    this.requestDigest = requestDigest;
  }

  /**
   * 获取 requestBytes 字段的值。
   *
   * @return requestBytes 字段值
   */
  public long getRequestBytes() {
    return requestBytes;
  }

  /**
   * 设置 requestBytes 字段的值。
   *
   * @param requestBytes requestBytes 字段的值
   */
  public void setRequestBytes(long requestBytes) {
    this.requestBytes = requestBytes;
  }

  /**
   * 获取 responseBytes 字段的值。
   *
   * @return responseBytes 字段值
   */
  public long getResponseBytes() {
    return responseBytes;
  }

  /**
   * 设置 responseBytes 字段的值。
   *
   * @param responseBytes responseBytes 字段的值
   */
  public void setResponseBytes(long responseBytes) {
    this.responseBytes = responseBytes;
  }

  /**
   * 获取 milliseconds 字段的值。
   *
   * @return milliseconds 字段值
   */
  public long getMilliseconds() {
    return milliseconds;
  }

  /**
   * 设置 milliseconds 字段的值。
   *
   * @param milliseconds milliseconds 字段的值
   */
  public void setMilliseconds(long milliseconds) {
    this.milliseconds = milliseconds;
  }

  /**
   * 获取 succeeded 字段的值。
   *
   * @return succeeded 字段值
   */
  public boolean isSucceeded() {
    return succeeded;
  }

  /**
   * 设置 succeeded 字段的值。
   *
   * @param succeeded succeeded 字段的值
   */
  public void setSucceeded(boolean succeeded) {
    this.succeeded = succeeded;
  }

  /**
   * 获取 responseCode 字段的值。
   *
   * @return responseCode 字段值
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * 设置 responseCode 字段的值。
   *
   * @param responseCode responseCode 字段的值
   */
  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  /**
   * 获取 errors 字段的值。
   *
   * @return errors 字段值
   */
  public String getErrors() {
    return errors;
  }

  /**
   * 设置 errors 字段的值。
   *
   * @param errors errors 字段的值
   */
  public void setErrors(String errors) {
    this.errors = errors;
  }

  /**
   * 获取 createTime 字段的值。
   *
   * @return createTime 字段值
   */
  @JsonSerialize(using = DateTimeSerializer.class)
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * 设置 createTime 字段的值。
   *
   * @param createTime createTime 字段的值
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 获取 serialversionuid 字段的值。
   *
   * @return serialversionuid 字段值
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
