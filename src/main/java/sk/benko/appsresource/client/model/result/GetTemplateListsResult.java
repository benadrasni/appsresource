package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.TemplateList;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ApplicationService#getTemplateTrees(String)}.
 */
@SuppressWarnings("serial")
public class GetTemplateListsResult implements Serializable {
  private ArrayList<TemplateList> templateLists;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param timestamp
   *          an opaque timestamp
   * @param templateLists
   *          the list of template lists to return
   */
  public GetTemplateListsResult(ArrayList<TemplateList> templateLists) {
    assert !GWT.isClient();
    this.templateLists = templateLists;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateListsResult() {
  }

  /**
   * Returns the template trees that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateList> getTemplateLists() {
    return templateLists;
  }
}