package sk.benko.appsresource.client.layout;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class ApplicationActionButtonsView extends FlowPanel {
  ApplicationView av;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationActionButtonsView(final ApplicationView av) {
    this.av = av;
    setStyleName("buttons_bar");
  }
  
  public void initialize() {
    clear();
  }
}
