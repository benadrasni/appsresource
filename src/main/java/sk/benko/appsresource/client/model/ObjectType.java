package sk.benko.appsresource.client.model;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing an object type.
 *
 */
@SuppressWarnings("serial")
public class ObjectType extends DesignItem {

  /**
   * The id of the Object Type's parent.
   */
  private int parentId;

  /**
   * The parent of the Object Type.
   */
  private ObjectType parent;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param name
   */
  public ObjectType(String name) {
    assert GWT.isClient();
    this.name = name;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param code
   * @param name
   * @param desc
   * @param parentId
   * @param parent
   * @param userId
   * @param lastUpdatedAt
   */
  public ObjectType(int id, String code, String name, String desc, int parentId, 
      ObjectType parent, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.parentId = parentId;
    this.parent = parent;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ObjectType() {
  }
  
  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  public ObjectType getParent() {
    return parent;
  }

  public void setParent(ObjectType parent) {
    this.parent = parent;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * objectType has been modified.
   *
   * @param objectType
   *          a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  ObjectType update(ObjectType objectType) {
    if (!objectType.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = objectType.id;
      name = objectType.name;
      code = objectType.code;
      desc = objectType.desc;
      parentId = objectType.parentId;
      userId = objectType.userId;
      lastUpdatedAt = objectType.lastUpdatedAt;
    }
    return this;
  }
}
