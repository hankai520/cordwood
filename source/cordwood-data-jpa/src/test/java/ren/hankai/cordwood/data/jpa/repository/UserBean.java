
package ren.hankai.cordwood.data.jpa.repository;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Cacheable(false)
public class UserBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String userName;

  private String address;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

}
