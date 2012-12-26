package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class UserContentView extends FlowPanel {

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserContentView(final UserModel umodel) {
    
    getElement().setId("content");

    add(new UserToolbarView(umodel));
    add(new SurfaceView(umodel));
  }
}
