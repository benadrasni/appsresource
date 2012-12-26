package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class UserActionView extends FlowPanel {

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserActionView(final UserModel umodel) {
    
    getElement().setId("action");

    add(new UserActionToolbarView(umodel));
    add(new UserActionButtonsView(umodel));
  }
}
