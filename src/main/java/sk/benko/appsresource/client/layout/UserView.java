package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.Track;
import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget that displays the UI associated with the content of the application.
 *
 */
public class UserView extends FlowPanel {

  /**
   * @param parent
   *          the parent for this widget
   * @param model
   *          the model to which the UI will bind itself
   */
  public UserView(final UserModel umodel) {
    getElement().setId(CSSConstants.CSS_APPLICATION);
    
    add(new UserHeaderView(umodel));
    add(new UserActionView(umodel));
    add(new UserContentView(umodel));
    
    Track.track("user");
  }
}
