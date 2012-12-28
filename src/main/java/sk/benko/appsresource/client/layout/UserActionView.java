package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class UserActionView extends FlowPanel {

  /**
   * @param uModel
   *          the model to which the Ui will bind itself
   */
  public UserActionView(final UserModel uModel) {
    
    getElement().setId("action");

    add(new UserActionToolbarView(uModel));
    add(new UserActionButtonsView(uModel));
  }
}
