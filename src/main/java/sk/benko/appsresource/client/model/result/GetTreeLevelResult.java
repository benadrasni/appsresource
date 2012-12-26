package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.List;

import sk.benko.appsresource.client.model.TreeLevel;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link sk.benko.appsresource.client.model.ApplicationService#getTreeLevel(int, int,
 * java.util.Map, int, java.util.List, sk.benko.appsresource.client.model.TemplateAttribute)}.
 */
@SuppressWarnings("serial")
public class GetTreeLevelResult implements Serializable {
  private List<TreeLevel> items;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param items
   *          the list of items to return
   */
  public GetTreeLevelResult(List<TreeLevel> items) {
    assert !GWT.isClient();
    this.items = items;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTreeLevelResult() {
  }

  /**
   * Returns the items that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return      tree items
   */
  public List<TreeLevel> getTreeLevel() {
    return items;
  }
}

