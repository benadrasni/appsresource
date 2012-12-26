package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.TemplateListItem;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ApplicationService#getTemplateListItems(TemplateList)}.
 */
@SuppressWarnings("serial")
public class GetTemplateListItemsResult implements Serializable {
  private ArrayList<TemplateListItem> templateListItems;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param timestamp
   *          an opaque timestamp
   * @param templateListItems
   *          the list of template list items to return
   */
  public GetTemplateListItemsResult(ArrayList<TemplateListItem> templateListItems) {
    assert !GWT.isClient();
    this.templateListItems = templateListItems;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateListItemsResult() {
  }

  /**
   * Returns the template trees that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateListItem> getTemplateListItems() {
    return templateListItems;
  }
}
