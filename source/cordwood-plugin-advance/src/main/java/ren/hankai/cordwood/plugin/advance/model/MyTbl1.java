
package ren.hankai.cordwood.plugin.advance.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

/**
 * 数据实体。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 3:19:05 PM
 */
@Entity
@Table(name = "mytbl1")
@Cacheable(false)
public class MyTbl1 implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String exp;
  private String timestamp;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getExp() {
    return exp;
  }

  public void setExp(String exp) {
    this.exp = exp;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}