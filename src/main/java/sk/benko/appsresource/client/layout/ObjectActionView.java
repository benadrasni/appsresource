package sk.benko.appsresource.client.layout;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 */
public class ObjectActionView extends FlowPanel {
  ObjectView objectView;
  
  ObjectActionToolbarView oatv;
  ObjectActionButtonsView oabv;
  ObjectTreeView objectTreeView;
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectActionView(final ObjectView objectView) {
    this.objectView = objectView;
    getElement().setId("action");

    oatv = new ObjectActionToolbarView(objectView);
    add(oatv);
    oabv = new ObjectActionButtonsView(objectView);
    add(oabv);
    setObjectTreeView(new ObjectTreeView(objectView));
    add(objectTreeView);
  }
  
  public void initialize() {
    oatv.initialize();
    oabv.initialize();
    getObjectTreeView().initialize();
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
   * @return the oatv
   */
  public ObjectActionToolbarView getOatv() {
    return oatv;
  }

  /**
   * @param oatv the oatv to set
   */
  public void setOatv(ObjectActionToolbarView oatv) {
    this.oatv = oatv;
  }

  /**
   * @return the oabv
   */
  public ObjectActionButtonsView getOabv() {
    return oabv;
  }

  /**
   * @param oabv the oabv to set
   */
  public void setOabv(ObjectActionButtonsView oabv) {
    this.oabv = oabv;
  }

  /**
   * @return the objectTreeView
   */
  public ObjectTreeView getObjectTreeView() {
    return objectTreeView;
  }

  /**
   * @param objectTreeView the objectTreeView to set
   */
  public void setObjectTreeView(ObjectTreeView objectTreeView) {
    this.objectTreeView = objectTreeView;
  }
}
