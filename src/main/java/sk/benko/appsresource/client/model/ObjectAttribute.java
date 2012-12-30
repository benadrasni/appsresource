package sk.benko.appsresource.client.model;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a object attribute.
 *
 */
@SuppressWarnings("serial")
public class ObjectAttribute extends DesignItem {

  /**
   * The object type to which object attribute belongs.
   */
  private int otId;
  private ObjectType ot;

  /**
   * The type of the object attribute.
   */
  private int vtId;
  private ValueType vt;

  /**
   * The unit of the object attribute.
   */
  private int unitId;
  private Unit unit;

  /**
   * Shared values are used for specific styles (like dynamic combos, tables, ...) 
   */
  private int shared1;
  private int shared2;
  private int shared3;
  private int shared4;
  private int shared5;

  /**
   * A basic constructor to be used on client-side only.
   *
   * @param name
   * @param otId
   * @param typeId
   */
  public ObjectAttribute(String name, int otId, int typeId) {
    assert GWT.isClient();
    this.name = name;
    this.otId = otId;
    this.vtId = typeId;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param code
   * @param name
   * @param desc
   * @param otId
   * @param typeId
   * @param unitId
   * @param shared1
   * @param shared2
   * @param shared3
   * @param shared4
   * @param shared5
   * @param userId
   * @param lastUpdatedAt
   */
  public ObjectAttribute(int id, String code, String name, String desc, int otId, 
      ObjectType ot, int typeId, ValueType vt, int unitId, Unit unit, 
      int shared1, int shared2, int shared3, int shared4, int shared5, 
      int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.otId = otId;
    this.ot = ot;
    this.vtId = typeId;
    this.vt = vt;
    this.unitId = unitId;
    this.unit = unit;
    this.shared1 = shared1;
    this.shared2 = shared2;
    this.shared3 = shared3;
    this.shared4 = shared4;
    this.shared5 = shared5;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }
  
  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ObjectAttribute() {
  }

  public int getOtId() {
    return otId;
  }

  public void setOtId(int otId) {
    this.otId = otId;
  }

  public ObjectType getOt() {
    return ot;
  }

  public void setOt(ObjectType ot) {
    this.ot = ot;
  }

  public int getVtId() {
    return vtId;
  }

  public void setVtId(int vtId) {
    this.vtId = vtId;
  }

  public ValueType getVt() {
    return vt;
  }

  public void setVt(ValueType vt) {
    this.vt = vt;
  }

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public int getShared1() {
    return shared1;
  }

  public void setShared1(int shared1) {
    this.shared1 = shared1;
  }

  public int getShared2() {
    return shared2;
  }

  public void setShared2(int shared2) {
    this.shared2 = shared2;
  }

  public int getShared3() {
    return shared3;
  }

  public void setShared3(int shared3) {
    this.shared3 = shared3;
  }

  public int getShared4() {
    return shared4;
  }

  public void setShared4(int shared4) {
    this.shared4 = shared4;
  }

  public int getShared5() {
    return shared5;
  }

  public void setShared5(int shared5) {
    this.shared5 = shared5;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * objectType has been modified.
   *
   * @param objectAttribute
   *          a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  ObjectAttribute update(ObjectAttribute objectAttribute) {
    if (!objectAttribute.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = objectAttribute.id;
      name = objectAttribute.name;
      code = objectAttribute.code;
      desc = objectAttribute.desc;
      otId = objectAttribute.otId;
      vtId = objectAttribute.vtId;
      vt = objectAttribute.vt;
      unitId = objectAttribute.unitId;
      unit = objectAttribute.unit;
      shared1 = objectAttribute.shared1;
      shared2 = objectAttribute.shared2;
      shared3 = objectAttribute.shared3;
      shared4 = objectAttribute.shared4;
      shared5 = objectAttribute.shared5;
      userId = objectAttribute.userId;
      lastUpdatedAt = objectAttribute.lastUpdatedAt;
    }
    return this;
  }
}
