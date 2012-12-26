package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.ValueType;

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
public class ObjectAttributeRowView extends FlexTable implements 
    DesignerModel.ObjectAttributeObserver {

  private DesignerModel model;
  private ObjectAttribute objectAttribute;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectAttributeRowView(DesignerModel model, ObjectAttribute oa) {
    setModel(model);
    setObjectAttribute(oa);
    
    setStyleName("content-row");

    generateWidget(getObjectAttribute());

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new ObjectAttributeDialog(getModel(), getObjectAttribute());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(ObjectAttribute oa) {
    Label lblCode = new Label(oa.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(oa.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(oa.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblOt = new Label(oa.getOt().getName());
    setWidget(0, 3, lblOt);
    
    String type = "";
    switch (oa.getVt().getType()) {
    case ValueType.VT_INT:
      type = Main.constants.valueTypeInteger();
      break;
    case ValueType.VT_REAL:
      type = Main.constants.valueTypeReal();
      break;
    case ValueType.VT_STRING:
      type = Main.constants.valueTypeString();
      break;
    case ValueType.VT_DATE:
      type = Main.constants.valueTypeDate();
      break;
    case ValueType.VT_DATETIME:
      type = Main.constants.valueTypeDateTime();
      break;
    case ValueType.VT_TIME:
      type = Main.constants.valueTypeTime();
      break;
    case ValueType.VT_REF:
      type = Main.constants.valueTypeRef();
      break;
    case ValueType.VT_TEXT:
      type = Main.constants.valueTypeText();
      break;
    }
    Label lblType = new Label(oa.getVt().getName() + " (" + type + ")");
    setWidget(0, 4, lblType);
    Label lblUnit;
    if (oa.getUnit() != null)
      lblUnit = new Label(oa.getUnit().getName());
    else
      lblUnit = new Label("");
    setWidget(0, 5, lblUnit);
  }

  @Override
  public void onObjectAttributeCreated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributeUpdated(ObjectAttribute objectAttribute) {
    if (this.objectAttribute.getId() == objectAttribute.getId()) 
      generateWidget(objectAttribute);    
  }

  @Override
  public void onObjectAttributesLoaded(int otId,
      Collection<ObjectAttribute> objectAttributes) {
  }
  
  @Override
  public void onLoad() {
    getModel().addObjectAttributeObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectAttributeObserver(this);
  }

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

  public ObjectAttribute getObjectAttribute() {
    return objectAttribute;
  }

  public void setObjectAttribute(ObjectAttribute objectAttribute) {
    this.objectAttribute = objectAttribute;
  }
  
}
