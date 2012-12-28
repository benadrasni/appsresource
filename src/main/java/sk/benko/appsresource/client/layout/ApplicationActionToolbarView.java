package sk.benko.appsresource.client.layout;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class ApplicationActionToolbarView extends FlowPanel {
  ApplicationView av;
  
  /**
   * @param av
   *          the model to which the Ui will bind itself
   */
  public ApplicationActionToolbarView(final ApplicationView av) {
    this.av = av;
    getElement().setId("action-toolbar");
  }
  
  public void initialize() {
    clear();
  }
}
