package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the section name.
 *
 */
public class ApplicationToolbarView extends FlowPanel {
  private ApplicationView applicationView;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationToolbarView(final ApplicationView av) {
    getElement().setId(CSSConstants.CSS_CONTENT_TOOLBAR);
    setApplicationView(av);
  }
  
  public void initialize() {
  }
  
  // getters and setters
  
  /**
   * @return the applicationView
   */
  public ApplicationView getApplicationView() {
    return applicationView;
  }

  /**
   * @param applicationView the applicationView to set
   */
  public void setApplicationView(ApplicationView applicationView) {
    this.applicationView = applicationView;
  }
}
