package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class UserActionToolbarView extends FlowPanel {
  
  /**
   * @param uModel
   *          the model to which the Ui will bind itself
   */
  public UserActionToolbarView(final UserModel uModel) {
    getElement().setId("action-toolbar");
  }
}
