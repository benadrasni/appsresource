package sk.benko.appsresource.client.model;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a object relation.
 *
 */
@SuppressWarnings("serial")
public class ObjectRelation extends DesignItem {
  public static final int RT_11 = 1;
  public static final int RT_1N = 2;
  public static final int RT_N1 = 3;
  public static final int RT_NN = 4;
  
  /**
   * The object type (source) to which object relation belongs.
   */
  private int ot1Id;
  private ObjectType ot1;

  /**
   * The object type (target) to which object relation belongs.
   */
  private int ot2Id;
  private ObjectType ot2;

  /**
   * The type of the object relation.
   */
  private int type;

  /**
   * The complementary object relation.
   */
  private int orId;
  private ObjectRelation or;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param code
   * @param name
   * @param otId
   * @param typeId
   */
  public ObjectRelation(String name, int ot1Id, int ot2Id, int type) {
    assert GWT.isClient();
    this.name = name;
    this.ot1Id = ot1Id;
    this.ot2Id = ot2Id;
    this.type = type;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param code
   * @param name
   * @param desc
   * @param ot1Id
   * @param ot1
   * @param ot2Id
   * @param ot2
   * @param typeId
   * @param orId
   * @param userId
   * @param lastUpdatedAt
   */
  public ObjectRelation(int id, String code, String name, String desc, int ot1Id, 
      ObjectType ot1, int ot2Id, ObjectType ot2, int type, int orId, 
      ObjectRelation or, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.ot1Id = ot1Id;
    this.ot1 = ot1;
    this.ot2Id = ot2Id;
    this.ot2 = ot2;
    this.type = type;
    this.orId = orId;
    this.or = or;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ObjectRelation() {
  }
  
  public int getOt1Id() {
    return ot1Id;
  }

  public void setOt1Id(int ot1Id) {
    this.ot1Id = ot1Id;
  }

  public ObjectType getOt1() {
    return ot1;
  }

  public void setOt1(ObjectType ot1) {
    this.ot1 = ot1;
  }

  public int getOt2Id() {
    return ot2Id;
  }

  public void setOt2Id(int ot2Id) {
    this.ot2Id = ot2Id;
  }

  public ObjectType getOt2() {
    return ot2;
  }

  public void setOt2(ObjectType ot2) {
    this.ot2 = ot2;
  }
  
  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getOrId() {
    return orId;
  }

  public void setOrId(int orId) {
    this.orId = orId;
  }

  public ObjectRelation getOr() {
    return or;
  }

  public void setOr(ObjectRelation or) {
    this.or = or;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * objectType has been modified.
   *
   * @param objectRelation
   *          a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  public ObjectRelation update(ObjectRelation objectRelation) {
    if (!objectRelation.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = objectRelation.id;
      name = objectRelation.name;
      code = objectRelation.code;
      desc = objectRelation.desc;
      ot1Id = objectRelation.ot1Id;
      ot1 = objectRelation.ot1;
      ot2Id = objectRelation.ot2Id;
      ot2 = objectRelation.ot2;
      type = objectRelation.type;
      orId = objectRelation.orId;
      userId = objectRelation.userId;
      lastUpdatedAt = objectRelation.lastUpdatedAt;
    }
    return this;
  }

  ObjectRelation update(int id, String code, ObjectType ot1, ObjectType ot2, 
      ObjectRelation or, Date lastUpdatedAt) {
    this.id = id;
    this.code = code;
    this.ot1 = ot1;
    this.ot2 = ot2;
    this.or = or;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
}
