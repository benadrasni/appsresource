package sk.benko.appsresource.client.model.result;

import com.google.gwt.core.client.GWT;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a response from {@link ApplicationService#getObjects(int, int, java.util.ArrayList,
 * sk.benko.appsresource.client.model.TemplateAttribute)}.
 */
@SuppressWarnings("serial")
public class GetValuesResult implements Serializable {
  private Map<Integer, Map<Integer, List<AValue>>> values;

  /**
   * Constructs a new result. This constructor can only be invoked on the server.
   *
   * @param values the map of map of values by object's id
   */
  public GetValuesResult(Map<Integer, Map<Integer, List<AValue>>> values) {
    assert !GWT.isClient();
    this.values = values;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetValuesResult() {
  }

  /**
   * Returns the values that were returned by the server. This can be zero-length, but will not be null.
   *
   * @return values
   */
  public Map<Integer, Map<Integer, List<AValue>>> getValues() {
    return values;
  }
}

