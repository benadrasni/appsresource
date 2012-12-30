package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the Ui associated with the header of the application.
 * This includes buttons for adding notes, bring up the surface list and
 * information about the current surface and user.
 *
 */
public class ObjectHeaderView extends FlowPanel {

  /**
   * @param aModel
   *          the model to which the Ui will bind itself
   */
  public ObjectHeaderView(final ApplicationModel aModel){
    getElement().setId("header");
     
    LogoView lv = new LogoView(aModel.getAppu().getApp().getName());
    add(lv);
  }
}
