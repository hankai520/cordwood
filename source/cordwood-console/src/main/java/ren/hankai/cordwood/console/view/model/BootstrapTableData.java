
package ren.hankai.cordwood.console.view.model;

import java.util.List;

/**
 * 包装 bootstrap-table 控件通过 ajax 交互所需的数据。
 *
 * @author hankai
 * @version 1.0.0
 * @since Dec 7, 2016 9:37:42 AM
 */
public class BootstrapTableData {

  private long total;
  private List<?> rows;

  /**
   * 获取 total 字段的值。
   *
   * @return total 字段值
   */
  public long getTotal() {
    return total;
  }

  /**
   * 设置 total 字段的值。
   *
   * @param total total 字段的值
   */
  public void setTotal(long total) {
    this.total = total;
  }

  /**
   * 获取 rows 字段的值。
   *
   * @return rows 字段值
   */
  public List<?> getRows() {
    return rows;
  }

  /**
   * 设置 rows 字段的值。
   *
   * @param rows rows 字段的值
   */
  public void setRows(List<?> rows) {
    this.rows = rows;
  }

}
