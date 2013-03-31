package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.ValueType;

import java.util.ArrayList;

/**
 * A widget to display form for {@link ValueType}.
 */
public class ValueTypeDialog extends DesignerDialog {
  private static ArrayList<DropDownObject> types;

  static {
    types = new ArrayList<DropDownObject>();
    types.add(new DropDownObjectImpl(ValueType.VT_INT, Main.constants.valueTypeInteger()));
    types.add(new DropDownObjectImpl(ValueType.VT_REAL, Main.constants.valueTypeReal()));
    types.add(new DropDownObjectImpl(ValueType.VT_STRING, Main.constants.valueTypeString()));
    types.add(new DropDownObjectImpl(ValueType.VT_DATE, Main.constants.valueTypeDate()));
    types.add(new DropDownObjectImpl(ValueType.VT_REF, Main.constants.valueTypeRef()));
    types.add(new DropDownObjectImpl(ValueType.VT_TEXT, Main.constants.valueTypeText()));
    types.add(new DropDownObjectImpl(ValueType.VT_DATETIME, Main.constants.valueTypeDateTime()));
    types.add(new DropDownObjectImpl(ValueType.VT_TIME, Main.constants.valueTypeTime()));
  }

  private DropDownBox ddbType;
  private CheckBox cbSystem;
  private CheckBox cbShareable;
  private FlexTable widgetValueType;

  /**
   * @param designerView the top level view
   */
  public ValueTypeDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.valueType());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getValueType() == null)
              setItem(new ValueType(getTbName().getText(), ddbType.getSelection().getId()));
            fill(getValueType());
            designerView.getDesignerModel().createOrUpdateValueType(getValueType());
            close();
          }
        }, ClickEvent.getType());

    ddbType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbType.setItems(types);
    cbSystem = new CheckBox();
    cbSystem.setText(Main.constants.valueTypeSystem());
    cbSystem.addStyleName(ClientUtils.CSS_DISABLED);
    cbShareable = new CheckBox();
    cbShareable.setText(Main.constants.valueTypeShareable());
    reset();

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
  }

  /**
   * @return the valueType
   */
  public ValueType getValueType() {
    return (ValueType) getItem();
  }

  /**
   * Getter for property 'widgetValueType'.
   *
   * @return Value for property 'widgetValueType'.
   */
  @Override
  public FlexTable getItemWidget() {
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
      widgetValueType.setWidget(3, 1, ddbType);
      widgetValueType.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetValueType.setWidget(4, 1, cbSystem);
      widgetValueType.setWidget(5, 1, cbShareable);
    }
    return widgetValueType;
  }

  /**
   * Fill {@link ValueType} with values from UI. Parent method must be called first.
   *
   * @param valueType    the valueType which should be filled with UI values
   */
  protected void fill(ValueType valueType) {
    super.fill(valueType);

    valueType.setType(ddbType.getSelection().getId());

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

  /**
   * Load {@link ValueType} to UI. Parent method must be called first.
   *
   * @param item    the value type which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    ValueType valueType = (ValueType) item;
    ddbType.setSelection(types.get(valueType.getType() - 1));
    cbSystem.setValue(ClientUtils.getFlag(ValueType.FLAG_SYSTEM, valueType.getFlags()));
    cbShareable.setValue(ClientUtils.getFlag(ValueType.FLAG_SHAREABLE, valueType.getFlags()));
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();
    ddbType.setSelection(types.get(2));
    cbSystem.setValue(false);
    cbShareable.setValue(false);
  }
}
