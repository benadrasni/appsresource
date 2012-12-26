package sk.benko.appsresource.client.designer;

import java.util.ArrayList;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ValueType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display form for {@link ValueType}.
 *
 */
public class ValueTypeDialog extends DesignerDialog {
  private DropDownBox ddbType;
  private CheckBox cbSystem;
  private CheckBox cbShareable;
  
  private FlexTable widgetValueType;

  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public ValueTypeDialog(final DesignerModel model, ValueType vt) {
    super(model, vt);

    getHeader().add(new Label((getValueType() == null ? 
        Main.constants.newItem() + " " : "") + Main.constants.valueType()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.valueType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);

    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetValueType());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getValueType() == null)
              setItem(new ValueType(getTbName().getText(),
                  getDdbType().getSelection().getId()));
              fill(getValueType());
              model.createOrUpdateValueType(getValueType());
              close();
            }
        }, ClickEvent.getType());
  }

  /**
   * @return the valueType
   */
  public ValueType getValueType() {
    return (ValueType)getItem();
  }
  
  /**
   * Getter for property 'ddbType'.
   * 
   * @return Value for property 'ddbType'.
   */
  protected DropDownBox getDdbType() {
    if (ddbType == null) {
      ddbType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      
      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 

      items.add(new DropDownObjectImpl(ValueType.VT_INT, 
          Main.constants.valueTypeInteger()));
      items.add(new DropDownObjectImpl(ValueType.VT_REAL, 
          Main.constants.valueTypeReal()));
      items.add(new DropDownObjectImpl(ValueType.VT_STRING, 
          Main.constants.valueTypeString()));
      items.add(new DropDownObjectImpl(ValueType.VT_DATE, 
          Main.constants.valueTypeDate()));
      items.add(new DropDownObjectImpl(ValueType.VT_REF, 
          Main.constants.valueTypeRef()));
      items.add(new DropDownObjectImpl(ValueType.VT_TEXT, 
          Main.constants.valueTypeText()));
      items.add(new DropDownObjectImpl(ValueType.VT_DATETIME, 
          Main.constants.valueTypeDateTime()));
      items.add(new DropDownObjectImpl(ValueType.VT_TIME, 
          Main.constants.valueTypeTime()));
      
      if (getValueType() != null)
        ddbType.setSelection(items.get(getValueType().getType()-1));
      else
        // default String
        ddbType.setSelection(items.get(2));
      ddbType.setItems(items);
 
    }
    return ddbType;
  }   

  /**
   * Getter for property 'cbSystem'.
   * 
   * @return Value for property 'cbSystem'.
   */
  protected CheckBox getCbSystem() {
    if (cbSystem == null) {
      cbSystem = new CheckBox();
      
      cbSystem.setText(Main.constants.valueTypeSystem());
      if (getValueType() != null) 
        cbSystem.setValue(ClientUtils.getFlag(ValueType.FLAG_SYSTEM, 
            getValueType().getFlags()));
      cbSystem.addStyleName(ClientUtils.CSS_DISABLED);
    }
    
    return cbSystem;
  }
  
  /**
   * Getter for property 'cbShareable'.
   * 
   * @return Value for property 'cbShareable'.
   */
  protected CheckBox getCbShareable() {
    if (cbShareable == null) {
      cbShareable = new CheckBox();
      
      cbShareable.setText(Main.constants.valueTypeShareable());
      if (getValueType() != null) 
        cbShareable.setValue(ClientUtils.getFlag(ValueType.FLAG_SHAREABLE, 
            getValueType().getFlags()));
    }
    
    return cbShareable;
  }

  /**
   * Getter for property 'widgetValueType'.
   * 
   * @return Value for property 'widgetValueType'.
   */
  public FlexTable getWidgetValueType() {
    if (widgetValueType == null) {
      widgetValueType = new FlexTable();

      Label lblCode = new Label(Main.constants.valueTypeCode());
      widgetValueType.setWidget(0, 0, lblCode);
      widgetValueType.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.valueTypeName());
      widgetValueType.setWidget(1, 0, lblName);
      widgetValueType.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.valueTypeDesc());
      widgetValueType.setWidget(2, 0, lblDesc);
      widgetValueType.setWidget(2, 1, getTbDesc());

      Label lblType = new Label(Main.constants.valueTypeType());
      widgetValueType.setWidget(3, 0, lblType);
      widgetValueType.setWidget(3, 1, getDdbType());
      widgetValueType.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);
      
      widgetValueType.setWidget(4, 1, getCbSystem());
      widgetValueType.setWidget(5, 1, getCbShareable());
    }  
    return widgetValueType;
  }
  
  
  // private methods
  
  private void fill(ValueType valueType) {
    super.fill(valueType);

    valueType.setType(getDdbType().getSelection().getId());
    
    // flags
    int flags = 0;
    if (cbSystem.getValue())
      flags = ClientUtils.setFlag(ValueType.FLAG_SYSTEM, flags);
    else
      flags = ClientUtils.unsetFlag(ValueType.FLAG_SYSTEM, flags);
    if (cbShareable.getValue())
      flags = ClientUtils.setFlag(ValueType.FLAG_SHAREABLE, flags);
    else
      flags = ClientUtils.unsetFlag(ValueType.FLAG_SHAREABLE, flags);
    valueType.setFlags(flags);
  }
}
