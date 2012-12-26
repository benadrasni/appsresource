package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.Track;
import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the UI associated with the content of the application.
 *
 */
public class ApplicationView extends FlowPanel {
  ApplicationModel amodel;
  
  ApplicationHeaderView ahv;
  ApplicationActionView aav;
  ApplicationContentView acv;

  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public ApplicationView(final ApplicationModel amodel) {
    amodel.removeObservers();
    this.amodel = amodel;
    getElement().setId(CSSConstants.CSS_APPLICATION);
    
    ahv = new ApplicationHeaderView(amodel);
    aav = new ApplicationActionView(this);
    acv = new ApplicationContentView(this);

    add(ahv);
    add(aav);
    add(acv);
    
    Track.track("app: " + getModel().getAppu().getApp().getName());
  }
  
  public void initialize() {
    ahv.initialize(true);
    aav.initialize();
    acv.initialize();
  }
  
  public ApplicationModel getModel() {
    return amodel;
  }
}
