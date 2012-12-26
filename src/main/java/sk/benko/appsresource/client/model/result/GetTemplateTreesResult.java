package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import sk.benko.appsresource.client.model.TemplateTree;

/**
 * Encapsulates a response from {@link ApplicationService#getTemplateTrees(String)}.
 */
@SuppressWarnings("serial")
public class GetTemplateTreesResult implements Serializable {
  private ArrayList<TemplateTree> templateTrees;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param timestamp
   *          an opaque timestamp
   * @param templates
   *          the list of template attributes to return
   */
  public GetTemplateTreesResult(ArrayList<TemplateTree> templateTrees) {
    assert !GWT.isClient();
    this.templateTrees = templateTrees;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateTreesResult() {
  }

  /**
   * Returns the template trees that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateTree> getTemplateTrees() {
    return templateTrees;
  }
}