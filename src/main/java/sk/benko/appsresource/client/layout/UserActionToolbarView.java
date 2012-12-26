package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class UserActionToolbarView extends FlowPanel {
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserActionToolbarView(final UserModel umodel) {
    getElement().setId("action-toolbar");
  }
}
