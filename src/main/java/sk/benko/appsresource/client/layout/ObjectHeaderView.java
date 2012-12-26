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
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectHeaderView(final ApplicationModel amodel){
    getElement().setId("header");
     
    LogoView lv = new LogoView(amodel.getAppu().getApp().getName());
    add(lv);
  }
}
