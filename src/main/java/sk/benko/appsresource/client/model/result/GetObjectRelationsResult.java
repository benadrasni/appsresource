package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.DbService;
import sk.benko.appsresource.client.model.ObjectRelation;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link DbService#getObjectRelations(String)}.
 */
@SuppressWarnings("serial")
public class GetObjectRelationsResult implements Serializable {
  private ArrayList<ObjectRelation> objectRelations;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param timestamp
   *          an opaque timestamp
   * @param valueTypes
   *          the list of value types to return
   */
  public GetObjectRelationsResult(ArrayList<ObjectRelation> objectRelations) {
    assert !GWT.isClient();
    this.objectRelations = objectRelations;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetObjectRelationsResult() {
  }

  /**
   * Returns the value types that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<ObjectRelation> getObjectRelations() {
    return objectRelations;
  }
}
