package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.ApplicationService;
import sk.benko.appsresource.client.model.TemplateRelation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;

/**
 * Encapsulates a response from {@link ApplicationService#getTemplateRelations(Template)}.
 */
@SuppressWarnings("serial")
public class GetTemplateRelationsResult implements Serializable {
  private ArrayList<TemplateRelation> templateRelations;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param templateRelations
   *          the list of template relations to return
   */
  public GetTemplateRelationsResult(ArrayList<TemplateRelation> templateRelations) {
    assert !GWT.isClient();
    this.templateRelations = templateRelations;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateRelationsResult() {
  }

  /**
   * Returns the object types that were returned by the server. This can be
   * zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateRelation> getTemplateRelations() {
    return templateRelations;
  }
}
