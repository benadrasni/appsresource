package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A widget that displays the Ui associated with the header of the application.
 * This includes buttons for adding notes, bring up the surface list and
 * information about the current surface and user.
 *
 */
public class ApplicationHeaderView extends FlowPanel {
  private ApplicationModel model;
  private SearchView searchView;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationHeaderView(final ApplicationModel model) {
    getElement().setId(CSSConstants.CSS_HEADER);
    setModel(model);
    
    LogoView lv = new LogoView(getModel().getAppu().getApp().getName());
    lv.addClickHandler(new ClickHandler() {
      
      @Override
      public void onClick(ClickEvent event) {
        ApplicationView av = new ApplicationView(getModel());
        av.initialize();
        Document.get().getElementById(CSSConstants.CSS_APPLICATION).removeFromParent();
        final RootPanel root = RootPanel.get();
        root.add(av);
      }
    });
    add(lv);
    
    add(getSearchView());
    
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
      public void execute () {
        getSearchView().setFocus();
      }
    });
  }
  
  public void initialize(boolean searchVisible) {
    getSearchView().setVisible(searchVisible);
  }

  /**
   * @return the model
   */
  public ApplicationModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(ApplicationModel model) {
    this.model = model;
  }

  /**
   * Getter for property 'searchView'.
   * 
   * @return Value for property 'searchView'.
   */
  public SearchView getSearchView() {
    if (searchView == null) {
      searchView = new SearchView(getModel());
    }
    return searchView;
  }
}
