package sk.benko.appsresource.client.model.result;

import java.io.Serializable;
import java.util.ArrayList;

import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateGroup;

import com.google.gwt.core.client.GWT;

/**
 * Encapsulates a response from {@link ObjectService#getTemplateAttributes(TemplateGroup)}.
 */
@SuppressWarnings("serial")
public class GetTemplateAttributesResult implements Serializable {
  private ArrayList<TemplateAttribute> templateAttributes;

  /**
   * Constructs a new result. This constructor can only be invoked on the
   * server.
   *
   * @param templates
   *          the list of template attributes to return
   */
  public GetTemplateAttributesResult(ArrayList<TemplateAttribute> templateAttributes) {
    assert !GWT.isClient();
    this.templateAttributes = templateAttributes;
  }

  /**
   * Needed for RPC serialization.
   */
  @SuppressWarnings("unused")
  private GetTemplateAttributesResult() {
  }

  /**
   * Returns the template attributes that were returned by the server. 
   * This can be zero-length, but will not be null.
   *
   * @return
   */
  public ArrayList<TemplateAttribute> getTemplateAttributes() {
    return templateAttributes;
  }
}
