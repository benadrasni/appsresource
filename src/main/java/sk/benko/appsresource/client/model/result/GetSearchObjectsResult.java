package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link sk.benko.appsresource.client.model.ApplicationService#getRelatedObjects(int, int, int, int, int, int)}
 * and {@link sk.benko.appsresource.client.model.ApplicationService#getSearchObjects(int, String, int, int, int)}.
 */
@SuppressWarnings("serial")
public class GetSearchObjectsResult implements Serializable {
  private Map<AObject, List<AValue>> objectValues;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param objectValues      the list of values to return
   */
  public GetSearchObjectsResult(Map<AObject, List<AValue>> objectValues) {
    assert !GWT.isClient();
    this.objectValues = objectValues;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetSearchObjectsResult() {
  }

  /**
   * Returns the applications that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return      object's values
   */
  public Map<AObject, List<AValue>> getObjectValues() {
    return objectValues;
  }
}

