package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.Date;

import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationService;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from
 * {@link ApplicationService#updateValue(sk.benko.appsresource.client.model.AValue, sk.benko.appsresource.client.model.AppUser)}.
 */
@SuppressWarnings("serial")
public class UpdateValueResult implements Serializable {
  private int id;
  private Date updateTime;
  private AValue value;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param id
   *          the key that was assigned to the new {@link AObject}
   * @param updateTime
   *          the time assigned to {@link AObject#getLastUpdatedAt()}
   */
  public UpdateValueResult(int id, Date updateTime, 
      AValue value) {
    assert !GWT.isClient();
    this.id = id;
    this.updateTime = updateTime;
    this.value = value;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private UpdateValueResult() {
  }

  /**
   * Returns the key that was assigned to the new {@link AObject}.
   *
   * @return
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the {@link Date} that was assigned to {@link AObject#getLastUpdatedAt()} by the server.
   *
   * @return
   */
  public Date getUpdateTime() {
    return updateTime;
  }
  
  /**
   *
   * @return
   */
  public AValue getValue() {
    return value;
  }
}
