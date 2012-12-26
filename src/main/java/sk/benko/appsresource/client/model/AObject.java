package sk.benko.appsresource.client.model;

import com.google.gwt.core.client.GWT;

import java.io.Serializable;
import java.util.Date;

/**
 * A client side data object representing an application.
 */
@SuppressWarnings("serial")
public class AObject implements Serializable {

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;
  /**
   * The key of the object type.
   */
  private int otId;
  /**
   * The level in object types hierarchy.
   */
  private int level;
  /**
   * The value of given (leaf) attribute.
   */
  private String leaf;
  /**
   * The key of the Author to whom this objectType belongs.
   */
  private int userId;
  private AppUser user;
  /**
   * The time of the most recent update. This value is always supplied by the
   * server.
   */
  private Date lastUpdatedAt;

  /**
   * A basic constructor to be used on client-side only.
   */
  public AObject(int otId) {
    assert GWT.isClient();
    this.otId = otId;
  }

  /**
   * A constructor to be used on client-side only.
   */
  public AObject(int id, int otId, int level, String leaf) {
    assert GWT.isClient();
    this.id = id;
    this.otId = otId;
    this.level = level;
    this.leaf = leaf;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id            id of the object
   * @param otId          id of object's type
   * @param level         object's level
   * @param leaf          object's leaf
   * @param userId        creator's id
   * @param user          created by
   * @param lastUpdatedAt last update at
   */
  public AObject(int id, int otId, int level, String leaf, int userId,
                 AppUser user, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.otId = otId;
    this.level = level;
    this.leaf = leaf;
    this.userId = userId;
    this.user = user;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private AObject() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOtId() {
    return otId;
  }

  public void setOtId(int otId) {
    this.otId = otId;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getLeaf() {
    return leaf;
  }

  public void setLeaf(String leaf) {
    this.leaf = leaf;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public AppUser getUser() {
    return user;
  }

  public void setUser(AppUser user) {
    this.user = user;
  }

  public Date getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  /**
   * Invoked when the application has been saved to the server.
   *
   * @param lastUpdatedAt the time that the server reported for the save
   * @return <code>this</code>, for chaining purposes
   */
  AObject update(Date lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  /**
   * Invoked when the model receives notification from the server that this
   * application has been modified.
   *
   * @param object a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  AObject update(AObject object) {
    if (!object.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = object.id;
      otId = object.otId;
      level = object.level;
      leaf = object.leaf;
      userId = object.userId;
      user = object.user;
      lastUpdatedAt = object.lastUpdatedAt;
    }
    return this;
  }

  AObject update(int id, Date lastUpdatedAt) {
    this.id = id;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
}
