package sk.benko.appsresource.client.layout;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class ApplicationActionView extends FlowPanel {
  ApplicationView av;
  
  ApplicationActionToolbarView aatv;
  ApplicationActionButtonsView aabv;
  ApplicationTreeView atv;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationActionView(final ApplicationView av) {
    this.av = av;
    getElement().setId("action");

    aatv = new ApplicationActionToolbarView(av);
    aabv = new ApplicationActionButtonsView(av); 
    atv = new ApplicationTreeView(av);
    
  }
  
  public void initialize() {
    clear();
    aatv.initialize();
    add(aatv);
    aabv.initialize();
    add(aabv);
    atv.initialize();
    add(atv);
  }
}
