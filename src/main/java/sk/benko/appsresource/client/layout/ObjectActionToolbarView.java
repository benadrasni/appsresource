package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.ApplicationModel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display the section name.
 *
 */
public class ObjectActionToolbarView extends FlowPanel {
  ObjectView objectView;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectActionToolbarView(final ObjectView objectView) {
    setObjectView(objectView);
    getElement().setId("action-toolbar");
  }
  
  public void initialize() {
    clear();
    add(new Label(getModel().getAppt().getT().getName()));
  }

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
  
}
