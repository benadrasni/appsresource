package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.HashMap;

import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.Template;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ApplicationService#getRelatedObjectCounts(int, int, sk.benko.appsresource.client.model.Template)}.
 */
@SuppressWarnings("serial")
public class GetSearchCountsResult implements Serializable {
  private HashMap<Template, Integer> counts;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param counts
   */
  public GetSearchCountsResult(HashMap<Template, Integer> counts) {
    assert !GWT.isClient();
    this.counts = counts;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetSearchCountsResult() {
  }

  /**
   * Returns the applications that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public HashMap<Template, Integer> getCounts() {
    return counts;
  }
}

