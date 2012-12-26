package sk.benko.appsresource.client.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 * A client side data object representing a value.
 *
 */
@SuppressWarnings("serial")
public class AValue implements Serializable {

  public interface Observer {
    void onUpdate(AValue value);
  }

  /**
   * The primary key which is always assigned by the server.
   */
  private int id;

  /**
   * The key of the object.
   */
  private int oId;

  /**
   * The key of the object attribute.
   */
  private int oaId;

  /**
   * The rank in object types hierarchy.
   */
  private int rank;
  
  /**
   * The string value.
   */
  private String valueString;
  private int langId;

  /**
   * The double value.
   */
  private Double valueDouble;

  /**
   * The date value.
   */
  private Date valueDate;
  private Date valueTimestamp;

  /**
   * The reference value.
   */
  private int valueRef;

  /**
   * The key of the Author to whom this objectType belongs.
   */
  private int userId;

  /**
   * The time of the most recent update. This value is always supplied by the
   * server.
   */
  private Date lastUpdatedAt;

  /**
   * An observer to receive callbacks whenever this {@link AValue} is updated.
   */
  private transient Observer observer;

  /**
   * A basic constructor to be used on client-side only for string value type.
   *
   */
  public AValue(int oId, int oaId, String valueString, int langId) {
    assert GWT.isClient();
    this.oId = oId;
    this.oaId = oaId;
    this.valueString = valueString;
    this.langId = langId;
  }

  /**
   * A basic constructor to be used on client-side only for date value type.
   *
   */
  public AValue(int oId, int oaId, Date valueDate, Date valueTimestamp) {
    assert GWT.isClient();
    this.oId = oId;
    this.oaId = oaId;
    this.valueDate = valueDate;
    this.valueTimestamp = valueTimestamp;
  }

  /**
   * A basic constructor to be used on client-side only for float value type.
   *
   */
  public AValue(int oId, int oaId, Double valueDouble) {
    assert GWT.isClient();
    this.oId = oId;
    this.oaId = oaId;
    this.valueDouble = valueDouble;
  }

  /**
   * A basic constructor to be used on client-side only for reference value type.
   *
   */
  public AValue(int oId, int oaId, int valueRef) {
    assert GWT.isClient();
    this.oId = oId;
    this.oaId = oaId;
    this.valueRef = valueRef;
    this.rank = 0;
  }

  /**
   * A constructor to be used on server-side only.
   *
   * @param key
   * @param oId
   * @param oaId
   * @param rank
   * @param lastUpdatedAt
   * @param author
   */
  public AValue(int id, int oId, int oaId, int rank, String valueString, 
      int langId, Date valueDate, Date valueTimestamp, Double valueDouble, 
      int valueRef, int userId, Date lastUpdatedAt) {
    this.id = id;
    this.oId = oId;
    this.oaId = oaId;
    this.rank = rank;
    this.valueString = valueString;
    this.langId = langId;
    this.valueDate = valueDate;
    this.valueTimestamp = valueTimestamp;
    this.valueDouble = valueDouble;
    this.valueRef = valueRef;
    this.userId = userId;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  /**
   * A default constructor to allow these objects to be serialized with GWT's
   * RPC.
   */
  @SuppressWarnings("unused")
  private AValue() {
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }

  public int getOId() {
    return oId;
  }

  public void setOId(int oId) {
    this.oId = oId;
  }

  public int getOaId() {
    return oaId;
  }

  public void setOaId(int oaId) {
    this.oaId = oaId;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getValueString() {
    return valueString;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }
  
  public int getLangId() {
    return langId;
  }
  
  public void setLangId(int langId) {
    this.langId = langId;
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

  public Date getValueTimestamp() {
    return valueTimestamp;
  }

  public void setValueTimestamp(Date valueTimestamp) {
    this.valueTimestamp = valueTimestamp;
  }

  public int getValueRef() {
    return valueRef;
  }

  public void setValueRef(int valueRef) {
    this.valueRef = valueRef;
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

  /**
   * Gets the observer that is receiving notification when 
   * the application is modified.
   *
   * @return
   */
  public Observer getObserver() {
    return observer;
  }

  /**
   * Sets the observer that will receive notification when this application is
   * modified.
   *
   * @param observer
   */
  public void setObserver(Observer observer) {
    this.observer = observer;
  }

  /**
   * Creates a copy of given value.
   *
   * @return new value
   */
  public AValue copy() {
    return new AValue(getId(), getOId(), getOaId(), 
        getRank(), getValueString(), getLangId(), 
        getValueDate(), getValueTimestamp(), getValueDouble(), 
        getValueRef(), getUserId(), getLastUpdatedAt());
  }
  
  /**
   * Invoked when the application has been saved to the server.
   *
   * @param lastUpdatedAt
   *          the time that the server reported for the save
   * @return <code>this</code>, for chaining purposes
   */
  AValue update(Date lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }

  /**
   * Invoked when the model receives notification from the server that this 
   * application has been modified.
   *
   * @param value
   *          a note containing up-to-date information about <code>this</code>
   * @return <code>this</code>, for chaining purposes
   */
  AValue update(AValue value) {
    if (!value.getLastUpdatedAt().equals(lastUpdatedAt)) {
      id = value.id;
      oId = value.oId;
      oaId = value.oaId;
      rank = value.rank;
      valueString = value.valueString;
      langId = value.langId;
      valueDate = value.valueDate;
      valueTimestamp = value.valueTimestamp;
      valueDouble = value.valueDouble;
      valueRef = value.valueRef;
      userId = value.userId;
      lastUpdatedAt = value.lastUpdatedAt;
      if (observer != null) observer.onUpdate(this);
    }
    return this;
  }
  

  AValue update(int id, Date lastUpdatedAt) {
    this.id = id;
    this.lastUpdatedAt = lastUpdatedAt;

    return this;
  }
  
  public boolean equals(TreeLevel item, boolean isDerived, String derivedValue) {
    return (isDerived && derivedValue.equals(item.getValueString()))
        || (getValueString() != null && getValueString().equals(item.getValueString()))
        || (getValueDate() != null && getValueDate().equals(item.getValueDate()))
        || (getValueDouble() != null && getValueDouble().equals(item.getValueDouble()))
        || (getValueRef() > 0 && getValueRef() == item.getValueRef());
  }
  
  public boolean isEmpty() {
    return (getValueString() == null || getValueString().length() == 0) 
      && getValueDate() == null && getValueTimestamp() == null 
      && getValueDouble() == null && getValueRef() == 0;
  }
}
