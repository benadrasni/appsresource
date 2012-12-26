package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class DesignerActionToolbarView extends FlowPanel {
  
  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public DesignerActionToolbarView(final DesignerModel dmodel) {
    getElement().setId(CSSConstants.CSS_ACTION_TOOLBAR);
  }
}
