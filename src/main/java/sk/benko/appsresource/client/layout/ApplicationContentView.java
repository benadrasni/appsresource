package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.application.SearchObjects;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class ApplicationContentView extends FlowPanel {
  ApplicationView av;
  
  ApplicationToolbarView atv;
  SearchObjects srv;

  
  /**
   * @param av
   *          the model to which the Ui will bind itself
   */
  public ApplicationContentView(final ApplicationView av) {
    this.av = av;
    getElement().setId(CSSConstants.CSS_CONTENT);

    atv = new ApplicationToolbarView(av); 
    srv = new SearchObjects(av); 
  }
  
  public void initialize() {
    clear();
    atv.initialize();
    add(atv);
    srv.initialize();
    add(srv);
  }
}
