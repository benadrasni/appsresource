package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ValueType;
import sk.benko.appsresource.client.model.loader.ValueTypeLoader;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object types.
 *
 */
public class ValueTypeTableView extends TableView implements 
    DesignerModel.ValueTypeObserver {

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ValueTypeTableView(DesignerModel model) {
    super(model);

    if (getModel().getValueTypes() == null) {
      ValueTypeLoader vtl = new ValueTypeLoader(getModel());
      vtl.start();
    } else 
      onValueTypesLoaded(getModel().getValueTypes());
  }

  @Override
  public void onValueTypeCreated(ValueType valueType) {
    add(new ValueTypeRowView(getModel(), valueType));
  }

  @Override
  public void onValueTypeUpdated(ValueType valueType) {
  }

  @Override
  public void onValueTypesLoaded(Collection<ValueType> valueTypes) {
    clear();
    add(getHeader());
    displayRows(valueTypes);
  }
  
  @Override
  public void onLoad() {
    getModel().addValueTypeObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeValueTypeObserver(this);
  }
  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.valueTypeCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.valueTypeName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.valueTypeDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblType = new Label(Main.constants.valueTypeType());
    getHeader().setWidget(0, 3, lblType);
  }
  
  public void displayRows(Collection<ValueType> valueTypes) {
    boolean line = false;
    for (ValueType vt : valueTypes) {
      if (!line && !vt.isSystem()) {
        // separator between system and custom value types
        line = true;
        FlexTable ft = new FlexTable();
        ft.setBorderWidth(1);
        ft.setWidth("100%");
        add(ft);
      }
      add(new ValueTypeRowView(getModel(), vt));
    }
  }
}
