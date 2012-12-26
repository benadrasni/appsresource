package sk.benko.appsresource.client.model;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a unit.
 *
 */
@SuppressWarnings("serial")
public class Unit extends DesignItem {

  public static int TYPE_NONE = 0;
  public static int TYPE_LENGTH = 1;
  public static int TYPE_WEIGHT = 2;
  public static int TYPE_AREA = 3;
  public static int TYPE_VOLUME = 4;
  public static int TYPE_TEMPERATURE = 5;
  public static int TYPE_ANGLE = 6;
  public static int TYPE_TIME = 7;
  
  /**
   * The symbol of the unit.
   */
  private String symbol;

  /**
   * The type of the unit.
   */
  private int type;

  /**
   * The conversion of the unit to the SI unit.
   */
  private float conversion;

  /**
   * A constructor to be used on client-side only.
   *
   * @param name
   * @param symbol
   */
  public Unit(String name, String symbol) {
    assert GWT.isClient();
    setName(name);
    setSymbol(symbol);
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param code
   * @param name
   * @param symbol
   * @param lastUpdatedAt
   * @param author
   */
  public Unit(int id, String code, String name, String desc, String symbol, 
      int type, float conversion, int userId, Date lastUpdatedAt) {
    assert !GWT.isClient();
    this.id = id;
    setCode(code);
    setName(name);
    setDesc(desc);
    setSymbol(symbol);
    setType(type);
    setConversion(conversion);
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private Unit() {
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public float getConversion() {
    return conversion;
  }

  public void setConversion(float conversion) {
    this.conversion = conversion;
  }
}
