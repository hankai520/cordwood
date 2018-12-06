/*******************************************************************************
 * Copyright (C) 2018 hankai
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package ren.hankai.cordwood.web.breadcrumb;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 面包屑导航项模型。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 6, 2016 2:55:06 PM
 */
public class BreadCrumbLink implements Serializable {

  private static final long serialVersionUID = -1734182996388561350L;

  private BreadCrumbLink previous;
  private final List<BreadCrumbLink> next = new LinkedList<>();
  private String url;
  private String family;
  private String label;
  private boolean currentPage;
  private String parentKey;
  private BreadCrumbLink parent;

  /**
   * 初始化面包屑导航模型。
   * 
   * @param family 家族（分组）
   * @param label 标签（显示名）
   * @param currentPage 是否是当前显示的页面
   * @param parentKey 父页面标识
   */
  public BreadCrumbLink(String family, String label, boolean currentPage, String parentKey) {
    this.family = family;
    this.label = label;
    this.currentPage = currentPage;
    this.parentKey = parentKey;
  }

  public void addNext(BreadCrumbLink next) {
    this.next.add(next);
  }

  /**
   * 获取 previous 字段的值。
   *
   * @return previous 字段值
   */
  public BreadCrumbLink getPrevious() {
    return previous;
  }

  /**
   * 设置 previous 字段的值。
   *
   * @param previous previous 字段的值
   */
  public void setPrevious(BreadCrumbLink previous) {
    this.previous = previous;
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
   * 获取 family 字段的值。
   *
   * @return family 字段值
   */
  public String getFamily() {
    return family;
  }

  /**
   * 设置 family 字段的值。
   *
   * @param family family 字段的值
   */
  public void setFamily(String family) {
    this.family = family;
  }

  /**
   * 获取 label 字段的值。
   *
   * @return label 字段值
   */
  public String getLabel() {
    return label;
  }

  /**
   * 设置 label 字段的值。
   *
   * @param label label 字段的值
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * 获取 currentPage 字段的值。
   *
   * @return currentPage 字段值
   */
  public boolean isCurrentPage() {
    return currentPage;
  }

  /**
   * 设置 currentPage 字段的值。
   *
   * @param currentPage currentPage 字段的值
   */
  public void setCurrentPage(boolean currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * 获取 parentKey 字段的值。
   *
   * @return parentKey 字段值
   */
  public String getParentKey() {
    return parentKey;
  }

  /**
   * 设置 parentKey 字段的值。
   *
   * @param parentKey parentKey 字段的值
   */
  public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  /**
   * 获取 parent 字段的值。
   *
   * @return parent 字段值
   */
  public BreadCrumbLink getParent() {
    return parent;
  }

  /**
   * 设置 parent 字段的值。
   *
   * @param parent parent 字段的值
   */
  public void setParent(BreadCrumbLink parent) {
    this.parent = parent;
  }

  /**
   * 获取 serialversionuid 字段的值。
   *
   * @return serialversionuid 字段值
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  /**
   * 获取 next 字段的值。
   *
   * @return next 字段值
   */
  public List<BreadCrumbLink> getNext() {
    return next;
  }

}
