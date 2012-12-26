package sk.benko.appsresource.client.model.result;

import com.google.gwt.core.client.GWT;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a response from
 * {@link sk.benko.appsresource.client.model.ApplicationService#createObject(sk.benko.appsresource.client.model.AObject,
 * java.util.List, sk.benko.appsresource.client.model.AppUser)}.
 */
@SuppressWarnings("serial")
public class CreateOrUpdateObjectResult implements Serializable {
  private int id;
  private Date updateTime;
  private Map<Integer, Map<Integer, List<AValue>>> allValues;

  /**
   * Constructs a new result. This constructor can only be invoked on the server.
   *
   * @param id          the key that was assigned to the new {@link AObject}
   * @param updateTime  the time assigned to {@link AObject#getLastUpdatedAt()}
   */
  public CreateOrUpdateObjectResult(int id, Date updateTime, Map<Integer, Map<Integer, List<AValue>>> allValues) {
    assert !GWT.isClient();
    this.id = id;
    this.updateTime = updateTime;
    this.allValues = allValues;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private CreateOrUpdateObjectResult() {
  }

  /**
   * Returns the key that was assigned to the new {@link AObject}.
   *
   * @return      the id
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the {@link Date} that was assigned to {@link AObject#getLastUpdatedAt()} by the server.
   *
   * @return last update date
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * Returns the {@link AValue}s that was assigned to {@link AObject} by the server.
   *
   * @return      all values for the object and related objects
   */
  public Map<Integer, Map<Integer, List<AValue>>> getValues() {
    return allValues;
  }
}
