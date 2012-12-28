package sk.benko.appsresource.client.model;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a value type.
 *
 */
@SuppressWarnings("serial")
public class ValueType extends DesignItem {
  public static final int FLAG_SYSTEM = 0;
  public static final int FLAG_SHAREABLE = 1;
  
  public static final int VT_INT = 1;
  public static final int VT_REAL = 2;
  public static final int VT_STRING = 3;
  public static final int VT_DATE = 4;
  public static final int VT_REF = 5;
  public static final int VT_TEXT = 6;
  public static final int VT_DATETIME = 7;
  public static final int VT_TIME = 8;
  
  /**
   * The basic type of the value Type.
   */
  private int type;

  /**
   * The flags of the value Type.
   */
  private int flags;

  /**
   * A constructor to be used on client-side only.
   *
   * @param name
   * @param type
   */
  public ValueType(String name, int type) {
    assert GWT.isClient();
    this.name = name;
    this.type = type;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param code
   * @param name
   * @param type
   * @param userId
   * @param lastUpdatedAt
   */
  public ValueType(int id, String code, String name, String desc, int type, 
      int flags, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.name = name;
    this.desc = desc;
    this.type = type;
    this.flags = flags;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }
  
  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private ValueType() {
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * objectType has been modified.
   *
   * @param valueType
   *          a value type containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  ValueType update(ValueType valueType) {
    if (!valueType.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = valueType.id;
      code = valueType.code;
      name = valueType.name;
      desc = valueType.desc;
      type = valueType.type;
      flags = valueType.flags;
      userId = valueType.userId;
      lastUpdatedAt = valueType.lastUpdatedAt;
    }
    return this;
  }

  public boolean isSystem() {
    return ClientUtils.getFlag(FLAG_SYSTEM, this.flags);
  }

  public boolean isShareable() {
    return ClientUtils.getFlag(FLAG_SHAREABLE, this.flags);
  }

}
