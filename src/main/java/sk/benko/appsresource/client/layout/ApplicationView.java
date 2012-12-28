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
  ApplicationModel aModel;

  ApplicationHeaderView ahv;
  ApplicationActionView aav;
  ApplicationContentView acv;

  /**
   * @param aModel
   *          the model to which the UI will bind itself
   */
  public ApplicationView(final ApplicationModel aModel) {
    aModel.removeObservers();
    this.aModel = aModel;
    getElement().setId(CSSConstants.CSS_APPLICATION);
    
    ahv = new ApplicationHeaderView(aModel);
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
    return aModel;
  }
}
