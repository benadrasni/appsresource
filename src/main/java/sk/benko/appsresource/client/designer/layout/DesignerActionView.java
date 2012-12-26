package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class DesignerActionView extends FlowPanel {
  
  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public DesignerActionView(final DesignerModel dmodel) {
    getElement().setId(CSSConstants.CSS_ACTION);

    add(new DesignerActionToolbarView(dmodel));
    add(new DesignerActionButtonsView(dmodel));
    add(new NavigationView(dmodel));
  }
}
