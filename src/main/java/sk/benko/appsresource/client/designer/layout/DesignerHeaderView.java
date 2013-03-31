package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.layout.LogoView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the UI associated with the header of the designer.
 *
 */
public class DesignerHeaderView extends FlowPanel {

  public DesignerHeaderView() {
    getElement().setId(CSSConstants.CSS_HEADER);
    
    add(new LogoView(Main.constants.designerMode()));
  }
}
