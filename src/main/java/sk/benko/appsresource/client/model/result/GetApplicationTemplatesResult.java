package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.ApplicationTemplate;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ApplicationService#getApplicationTemplates(String)}.
 */
@SuppressWarnings("serial")
public class GetApplicationTemplatesResult implements Serializable {
  private ArrayList<ApplicationTemplate> applicationTemplates;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param templates
   *          the list of template attributes to return
   */
  public GetApplicationTemplatesResult(ArrayList<ApplicationTemplate> applicationTemplates) {
    assert !GWT.isClient();
    this.applicationTemplates = applicationTemplates;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetApplicationTemplatesResult() {
  }

  /**
   * Returns the applications that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<ApplicationTemplate> getApplicationTemplates() {
    return applicationTemplates;
  }
}
