package sk.benko.appsresource.client.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a tree level.
 *
 */
@SuppressWarnings("serial")
public class TreeLevel implements Serializable {

  /**
   * The id of the tree level.
   */
  private int id;

  /**
   * The template attribute associated with level.
   */
  private TemplateAttribute ta;

  /**
   * The string value.
   */
  private String valueString;

  /**
   * The double value.
   */
  private Double valueDouble;

  /**
   * The date value.
   */
  private Date valueDate;

  /**
   * The reference value.
   */
  private int valueRef;

  /**
   * The id of tree level which the TreeLevel is subset of
   */
  private int subsetOf;

  /**
   * A constructor to be used on server-side only.
   *
   * @param id
   * @param ta
   * @param valueString
   * @param valueDate
   * @param valueDouble
   * @param valueRef
   * @param subsetOf
   */
  public TreeLevel(int id, TemplateAttribute ta, String valueString, Date valueDate, Double valueDouble, int valueRef,
                   int subsetOf) {
    assert !GWT.isClient();
    this.id = id;
    this.ta = ta;
    this.valueString = valueString;
    this.valueDate = valueDate;
    this.valueDouble = valueDouble;
    this.valueRef = valueRef;
    this.subsetOf = subsetOf;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private TreeLevel() {
  }

  public boolean isEmpty() {
    return getValueString() == null && getValueDouble() == null 
        && getValueDate() == null;
  }

  public int getId() {
    return id;
  }

  public TemplateAttribute getTa() {
    return ta;
  }

  public void setTa(TemplateAttribute ta) {
    this.ta = ta;
  }

  public String getValueString() {
    return valueString;
  }

  public Double getValueDouble() {
    return valueDouble;
  }

  public Date getValueDate() {
    return valueDate;
  }

  public int getValueRef() {
    return valueRef;
  }

  public int getSubsetOf() {
    return subsetOf;
  }
}
