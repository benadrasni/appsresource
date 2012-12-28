package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.Date;

import sk.benko.appsresource.client.model.DbService;
import sk.benko.appsresource.client.model.ObjectRelation;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from
 * {@link DbService#createObjectRelation(sk.benko.appsresource.client.model.ObjectRelation, sk.benko.appsresource.client.model.AppUser)}.
 */
@SuppressWarnings("serial")
public class CreateOrUpdateObjectRelationResult implements Serializable {
  private int id;
  private String code;
  private Date updateTime;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param id
   *          the key that was assigned to the new {@link ObjectRelation}
   * @param updateTime
   *          the time assigned to {@link ObjectRelation#getLastUpdatedAt()}
   */
  public CreateOrUpdateObjectRelationResult(int id, String code, Date updateTime) {
    assert !GWT.isClient();
    this.id = id;
    this.code = code;
    this.updateTime = updateTime;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private CreateOrUpdateObjectRelationResult() {
  }

  /**
   * Returns the key that was assigned to the new {@link ObjectRelation}.
   *
   * @return
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the code that was assigned to the new {@link ObjectRelation}.
   *
   * @return
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the {@link Date} that was assigned to
   * {@link ObjectRelation#getLastUpdatedAt()} by the server.
   *
   * @return
   */
  public Date getUpdateTime() {
    return updateTime;
  }
}
