package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import sk.benko.appsresource.client.model.TemplateTreeItem;

/**
 * Encapsulates a response from {@link ApplicationService#getTemplateTreeItems(String)}.
 */
@SuppressWarnings("serial")
public class GetTemplateTreeItemsResult implements Serializable {
  private ArrayList<TemplateTreeItem> templateTreeItems;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param timestamp
   *          an opaque timestamp
   * @param templates
   *          the list of template attributes to return
   */
  public GetTemplateTreeItemsResult(ArrayList<TemplateTreeItem> templateTreeItems) {
    assert !GWT.isClient();
    this.templateTreeItems = templateTreeItems;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateTreeItemsResult() {
  }

  /**
   * Returns the template trees that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateTreeItem> getTemplateTreeItems() {
    return templateTreeItems;
  }
}
