package sk.benko.appsresource.client.model;

import java.io.Serializable;
import java.util.Date;

/**
 * A client side data object representing a unit.
 *
 */
@SuppressWarnings("serial")
public abstract class DesignItem implements Serializable, Comparable<DesignItem> {

  /**
   * The primary key which is always assigned by the server.
   */
  protected int id;

  /**
   * The code of the unit.
   */
  protected String code;

  /**
   * The name of the unit.
   */
  protected String name;

  /**
   * The description of the unit.
   */
  protected String desc;

  /**
   * The key of the Author to whom this unit belongs.
   */
  protected int userId;

  /**
   * The time of the most recent update. This value is always supplied by the
   * server.
   */
  protected Date lastUpdatedAt;

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  protected DesignItem() {
  }

  public int getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Date getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public DesignItem update(int id, String code, Date lastUpdatedAt) {
    this.id = id;
    this.code = code;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  @Override
  public int compareTo(DesignItem item) {
    return getName().compareTo(item.getName());
  }
}
