package sk.benko.appsresource.client.designer.layout;

import com.google.gwt.user.client.ui.FlowPanel;
import sk.benko.appsresource.client.CSSConstants;

/**
 * A widget to display the action toolbar.
 */
public class DesignerActionToolbarView extends FlowPanel {

  /**
   *
   */
  public DesignerActionToolbarView() {
    getElement().setId(CSSConstants.CSS_ACTION_TOOLBAR);
  }
}
