package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class UserToolbarView extends FlowPanel {
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserToolbarView(final UserModel model) {
    getElement().setId("content-toolbar");
  }
}
