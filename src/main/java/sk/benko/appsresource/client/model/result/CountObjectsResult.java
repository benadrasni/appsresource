package sk.benko.appsresource.client.model.result;

import java.io.Serializable;

import sk.benko.appsresource.client.model.ApplicationService;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ApplicationService#importObjects(sk.benko.appsresource.client.model.Application, sk.benko.appsresource.client.model.Template, String, java.util.Map, java.util.Map, boolean, sk.benko.appsresource.client.model.AppUser)},
 * and {@link ApplicationService#removeDuplicates(sk.benko.appsresource.client.model.Application, sk.benko.appsresource.client.model.Template, java.util.Map, sk.benko.appsresource.client.model.AppUser)}.
 */
@SuppressWarnings("serial")
public class CountObjectsResult implements Serializable {
  private int count;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param count
   */
  public CountObjectsResult(int count) {
    assert !GWT.isClient();
    this.count = count;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private CountObjectsResult() {
  }

  /**
   * Returns the applications that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public int getCount() {
    return count;
  }
}

