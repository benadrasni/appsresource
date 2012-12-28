package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class UserContentView extends FlowPanel {

  /**
   * @param uModel
   *          the model to which the Ui will bind itself
   */
  public UserContentView(final UserModel uModel) {
    
    getElement().setId("content");

    add(new UserToolbarView(uModel));
    add(new SurfaceView(uModel));
  }
}
