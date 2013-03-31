package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
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
public class ValueTypeRowView extends FlexTable implements 
    DesignerModel.ValueTypeObserver {

  private DesignerView designerView;
  private ValueType valueType;
  
  /**
   * @param designerView    the top level view
   * @param valueType       the value type
   */
  public ValueTypeRowView(final DesignerView designerView, final ValueType valueType) {
    this.designerView = designerView;
    this.valueType = valueType;
    
    setStyleName("content-row");

    generateWidget(valueType);

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getValueTypeDialog().setItem(valueType);
        designerView.getValueTypeDialog().show();
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(ValueType vt) {
    Label lblCode = new Label(vt.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(vt.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(vt.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblType;
    switch (vt.getType()) {
    case ValueType.VT_INT:
      lblType = new Label(Main.constants.valueTypeInteger());
      break;
    case ValueType.VT_REAL:
      lblType = new Label(Main.constants.valueTypeReal());
      break;
    case ValueType.VT_STRING:
      lblType = new Label(Main.constants.valueTypeString());
      break;
    case ValueType.VT_DATE:
      lblType = new Label(Main.constants.valueTypeDate());
      break;
    case ValueType.VT_DATETIME:
      lblType = new Label(Main.constants.valueTypeDateTime());
      break;
    case ValueType.VT_TIME:
      lblType = new Label(Main.constants.valueTypeTime());
      break;
    case ValueType.VT_REF:
      lblType = new Label(Main.constants.valueTypeRef());
      break;
    case ValueType.VT_TEXT:
      lblType = new Label(Main.constants.valueTypeText());
      break;

    default:
      lblType = new Label("");
      break;
    }
    setWidget(0, 3, lblType);
  }

  @Override
  public void onValueTypeCreated(ValueType valueType) {
  }

  @Override
  public void onValueTypeUpdated(ValueType valueType) {
    if (getValueType().getId() == valueType.getId()) 
      generateWidget(valueType);    
  }

  @Override
  public void onValueTypesLoaded(Collection<ValueType> valueTypes) {
  }
  
  @Override
  public void onLoad() {
    designerView.getDesignerModel().addValueTypeObserver(this);
  }

  @Override
  public void onUnload() {
    designerView.getDesignerModel().removeValueTypeObserver(this);
  }

  // getters and setters

  /**
   * @return the valueType
   */
  public ValueType getValueType() {
    return valueType;
  }

  /**
   * @param valueType the valueType to set
   */
  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }
  
}
