package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class DesignerActionView extends FlowPanel {
  
  /**
   * @param designerView      the top level view
   */
  public DesignerActionView(final DesignerView designerView) {
    getElement().setId(CSSConstants.CSS_ACTION);

    add(new DesignerActionToolbarView());
    add(new DesignerActionButtonsView(designerView));
    add(new NavigationView(designerView));
  }
}
