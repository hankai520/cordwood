package ren.hankai.cordwood.console.persist.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 菜单边栏项。
 *
 * @author hankai
 * @version 1.0.0
 * @since Nov 2, 2016 10:08:47 AM
 */
@Entity
@Table(name = "sidebar_items")
@Cacheable(false)
public class SidebarItemBean implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parentId", referencedColumnName = "id")
  private SidebarItemBean parentItem;
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentItem")
  @OrderBy("sink asc")
  private List<SidebarItemBean> subItems;
  private String name; // 用于唯一标识项，用来生成 HTML 元素的id
  private String url; // 边栏项链接的页面地址
  private String iconClasses; // 边栏项的图标样式
  private String displayText; // 边栏项在界面上显示的文本
  private int sink; // 边栏项排序（按值升序排列）

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
   * 获取 parentItem 字段的值。
   *
   * @return parentItem 字段值
   */
  public SidebarItemBean getParentItem() {
    return parentItem;
  }

  /**
   * 设置 parentItem 字段的值。
   *
   * @param parentItem parentItem 字段的值
   */
  public void setParentItem(SidebarItemBean parentItem) {
    this.parentItem = parentItem;
  }

  /**
   * 获取 subItems 字段的值。
   *
   * @return subItems 字段值
   */
  public List<SidebarItemBean> getSubItems() {
    return subItems;
  }

  /**
   * 设置 subItems 字段的值。
   *
   * @param subItems subItems 字段的值
   */
  public void setSubItems(List<SidebarItemBean> subItems) {
    this.subItems = subItems;
  }

  /**
   * 获取 name 字段的值。
   *
   * @return name 字段值
   */
  public String getName() {
    return name;
  }

  /**
   * 设置 name 字段的值。
   *
   * @param name name 字段的值
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取 url 字段的值。
   *
   * @return url 字段值
   */
  public String getUrl() {
    return url;
  }

  /**
   * 设置 url 字段的值。
   *
   * @param url url 字段的值
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * 获取 iconClasses 字段的值。
   *
   * @return iconClasses 字段值
   */
  public String getIconClasses() {
    return iconClasses;
  }

  /**
   * 设置 iconClasses 字段的值。
   *
   * @param iconClasses iconClasses 字段的值
   */
  public void setIconClasses(String iconClasses) {
    this.iconClasses = iconClasses;
  }

  /**
   * 获取 displayText 字段的值。
   *
   * @return displayText 字段值
   */
  public String getDisplayText() {
    return displayText;
  }

  /**
   * 设置 displayText 字段的值。
   *
   * @param displayText displayText 字段的值
   */
  public void setDisplayText(String displayText) {
    this.displayText = displayText;
  }

  /**
   * 获取 sink 字段的值。
   *
   * @return sink 字段值
   */
  public int getSink() {
    return sink;
  }

  /**
   * 设置 sink 字段的值。
   *
   * @param sink sink 字段的值
   */
  public void setSink(int sink) {
    this.sink = sink;
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
