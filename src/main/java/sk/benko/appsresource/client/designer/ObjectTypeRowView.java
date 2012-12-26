package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display object type in table row.
 *
 */
public class ObjectTypeRowView extends FlexTable implements 
    DesignerModel.ObjectTypeObserver {

  private DesignerModel model;
  private ObjectType objectType;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectTypeRowView(final DesignerModel model, final ObjectType ot) {
    setModel(model);
    setObjectType(ot);
    
    setStyleName("content-row");

    generateWidget(getObjectType());

    // invoke dialog on click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new ObjectTypeDialog(getModel(), getObjectType());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(ObjectType ot) {
    Label lblCode = new Label(ot.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(ot.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(ot.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblParent;
    if (ot.getParent() != null)
      lblParent = new Label(ot.getParent().getName());
    else
      lblParent = new Label("");
    setWidget(0, 3, lblParent);
  }

  @Override
  public void onObjectTypeCreated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
    if (getObjectType().getId() == objectType.getId()) 
      generateWidget(objectType);    
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
  }
  
  @Override
  public void onLoad() {
    getModel().addObjectTypeObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectTypeObserver(this);
  }

  // getters and setters

  /**
   * @return the model
   */
  public DesignerModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(DesignerModel model) {
    this.model = model;
  }

  /**
   * @return the objectType
   */
  public ObjectType getObjectType() {
    return objectType;
  }

  /**
   * @param objectType the objectType to set
   */
  public void setObjectType(ObjectType objectType) {
    this.objectType = objectType;
  }
}
