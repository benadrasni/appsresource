package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the Ui associated with the header of the application.
 * This includes buttons for adding notes, bring up the surface list and
 * information about the current surface and user.
 *
 */
public class UserHeaderView extends FlowPanel {

  /**
   * @param parent
   *          the parent for this widget
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UserHeaderView(final UserModel model) {
    getElement().setId("header");
    
    add(new LogoView(Main.constants.userMode()));
  }
}
