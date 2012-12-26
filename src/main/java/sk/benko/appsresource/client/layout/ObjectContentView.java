package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.application.SearchObjects;
import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class ObjectContentView extends FlowPanel {
  private ObjectView objectView;

  ObjectToolbarView otoolv;
  ObjectTemplateView otempv;
  SearchObjects srv;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectContentView(final ObjectView objectView) {
    this.objectView = objectView;
    getElement().setId(CSSConstants.CSS_CONTENT);

    otoolv = new ObjectToolbarView(this.objectView);
    otempv = new ObjectTemplateView(this.objectView);
    //srv = new SearchObjects(this.objectView.getApplicationView()); 
  }
  
  public void initialize(boolean isSearch) {
    clear();
    if (isSearch) {
      srv.initialize();
      add(srv);
    } else {
      otoolv.initialize(getModel().getAppt());
      add(otoolv);
      otempv.initialize();
      add(otempv);
    }
  }

  // getters and setters
  
  /**
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return objectView;
  }

  /**
   * @param objectView the objectView to set
   */
  public void setObjectView(ObjectView objectView) {
    this.objectView = objectView;
  }

  /**
   * @return the objectView
   */
  public ApplicationModel getModel() {
    return getObjectView().getModel();
  }

  /**
   * @return the otoolv
   */
  public ObjectToolbarView getOtoolv() {
    return otoolv;
  }

  /**
   * @param otoolv the otoolv to set
   */
  public void setOtoolv(ObjectToolbarView otoolv) {
    this.otoolv = otoolv;
  }

  /**
   * @return the otempv
   */
  public ObjectTemplateView getOtempv() {
    return otempv;
  }

  /**
   * @param otempv the otempv to set
   */
  public void setOtempv(ObjectTemplateView otempv) {
    this.otempv = otempv;
  }

  /**
   * @return the srv
   */
  public SearchObjects getSrv() {
    return srv;
  }

  /**
   * @param srv the srv to set
   */
  public void setSrv(SearchObjects srv) {
    this.srv = srv;
  }

  
}
