package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.Track;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the UI associated with the designer.
 *
 */
public class DesignerView extends FlowPanel {

  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public DesignerView(final DesignerModel dmodel) {
    getElement().setId(CSSConstants.CSS_APPLICATION);
    
    add(new DesignerHeaderView(dmodel));
    add(new DesignerActionView(dmodel));
    
    Track.track("designer");
  }
}
