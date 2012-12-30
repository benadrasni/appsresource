package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class ObjectTemplateView extends FlowPanel {
  private ObjectView objectView;
  
  private ObjectTemplate otemp;

  /**
   * @param objectView
   */
  public ObjectTemplateView(final ObjectView objectView) {
    setObjectView(objectView);
    getElement().setId(CSSConstants.CSS_CONTENT_TEMPLATE);
    add(getOtemp());
  }
  
  public void initialize() {
    getOtemp().initialize(getModel().getAppt().getT());
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
   * @return the otemp
   */
  public ObjectTemplate getOtemp() {
    if (otemp == null) 
      otemp = new ObjectTemplate(getObjectView(), true);
    return otemp;
  }
}
