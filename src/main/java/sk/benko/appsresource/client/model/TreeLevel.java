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
   * A basic constructor to be used on client-side only for string value type.
   *
   */
  public TreeLevel(TemplateAttribute ta, String valueString) {
    assert GWT.isClient();
    this.ta = ta;
    this.valueString = valueString;
  }

  /**
   * A basic constructor to be used on client-side only for date value type.
   *
   */
  public TreeLevel(TemplateAttribute ta, Date valueDate) {
    assert GWT.isClient();
    this.ta = ta;
    this.valueDate = valueDate;
  }

  /**
   * A basic constructor to be used on client-side only for float value type.
   *
   */
  public TreeLevel(TemplateAttribute ta, Double valueDouble) {
    assert GWT.isClient();
    this.ta = ta;
    this.valueDouble = valueDouble;
  }

  /**
   * A basic constructor to be used on client-side only for float value type.
   *
   */
  public TreeLevel(TemplateAttribute ta, int valueRef) {
    assert GWT.isClient();
    this.ta = ta;
    this.valueRef = valueRef;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param ta
   * @param valueString
   * @param valueDate
   * @param valueDouble
   * @param valueRef
   */
  public TreeLevel(TemplateAttribute ta, String valueString, 
      Date valueDate, Double valueDouble, int valueRef) {
    assert !GWT.isClient();
    this.ta = ta;
    this.valueString = valueString;
    this.valueDate = valueDate;
    this.valueDouble = valueDouble;
    this.valueRef = valueRef;
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
  
  public TemplateAttribute getTa() {
    return ta;
  }

  public void setTa(TemplateAttribute ta) {
    this.ta = ta;
  }

  public String getValueString() {
    return valueString;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }
  
  public Double getValueDouble() {
    return valueDouble;
  }

  public void setValueDouble(Double valueDouble) {
    this.valueDouble = valueDouble;
  }

  public Date getValueDate() {
    return valueDate;
  }

  public void setValueDate(Date valueDate) {
    this.valueDate = valueDate;
  }

  public int getValueRef() {
    return valueRef;
  }

  public void setValueRef(int valueRef) {
    this.valueRef = valueRef;
  }
}
