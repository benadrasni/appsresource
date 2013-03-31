package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.ui.widget.DoubleTextBox;

import java.util.ArrayList;

/**
 * A widget to display form for {@link Unit}.
 */
public class UnitDialog extends DesignerDialog {
  private static final float DEFAULT_CONVERSION = 1;
  private static ArrayList<DropDownObject> types;
  private static NumberFormat df;

  static {
    types = new ArrayList<DropDownObject>();
    types.add(new DropDownObjectImpl(Unit.TYPE_NONE, Main.constants.choose()));
    types.add(new DropDownObjectImpl(Unit.TYPE_LENGTH, Main.constants.unitTypeLength()));
    types.add(new DropDownObjectImpl(Unit.TYPE_WEIGHT, Main.constants.unitTypeWeight()));
    types.add(new DropDownObjectImpl(Unit.TYPE_AREA, Main.constants.unitTypeArea()));
    types.add(new DropDownObjectImpl(Unit.TYPE_VOLUME, Main.constants.unitTypeVolume()));
    types.add(new DropDownObjectImpl(Unit.TYPE_TEMPERATURE, Main.constants.unitTypeTemperature()));
    types.add(new DropDownObjectImpl(Unit.TYPE_ANGLE, Main.constants.unitTypeAngle()));
    types.add(new DropDownObjectImpl(Unit.TYPE_TIME, Main.constants.unitTypeTime()));

    df = NumberFormat.getFormat(ClientUtils.NUMBERFORMAT);
  }

  private TextBox tbSymbol;
  private DropDownBox ddbType;
  private Label lblConversion;
  private DoubleTextBox tbConversion;
  private FlexTable widgetUnit;

  /**
   * @param designerView the top level view
   */
  public UnitDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.unit());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getUnit() == null)
              setItem(new Unit(getTbName().getText(), tbSymbol.getText()));
            fill(getUnit());
            designerView.getDesignerModel().createOrUpdateUnit(getUnit());
            close();
          }
        }, ClickEvent.getType());

    tbSymbol = new TextBox();
    tbSymbol.setWidth(TBWIDTH_SMALL);
    ddbType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbType.setItems(types);
    lblConversion = new Label(Main.constants.unitConversion());
    tbConversion = new DoubleTextBox(lblConversion, getBOk());

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  /**
   * @return the unit
   */
  public Unit getUnit() {
    return (Unit) getItem();
  }

  /**
   * Getter for property 'widgetUnit'.
   *
   * @return Value for property 'widgetUnit'.
   */
  @Override
  public FlexTable getItemWidget() {
    if (widgetUnit == null) {
      widgetUnit = new FlexTable();

      Label lblCode = new Label(Main.constants.unitCode());
      widgetUnit.setWidget(0, 0, lblCode);
      widgetUnit.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.unitName());
      widgetUnit.setWidget(1, 0, lblName);
      widgetUnit.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.unitDesc());
      widgetUnit.setWidget(2, 0, lblDesc);
      widgetUnit.setWidget(2, 1, getTbDesc());

      Label lblSymbol = new Label(Main.constants.unitSymbol());
      widgetUnit.setWidget(3, 0, lblSymbol);
      widgetUnit.setWidget(3, 1, tbSymbol);

      Label lblType = new Label(Main.constants.unitType());
      widgetUnit.setWidget(4, 0, lblType);
      widgetUnit.setWidget(4, 1, ddbType);
      widgetUnit.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetUnit.setWidget(5, 0, lblConversion);
      widgetUnit.setWidget(5, 1, tbConversion);
    }
    return widgetUnit;
  }

  /**
   * Fill {@link Unit} with values from UI. Parent method must be called first.
   *
   * @param unit    the unit which should be filled with UI values
   */
  protected void fill(Unit unit) {
    super.fill(unit);

    unit.setSymbol(tbSymbol.getText());
    unit.setType(ddbType.getSelection().getId());
    String valueString = tbConversion.getText().trim();
    if (valueString.length() > 0) {
      float valueFloat = new Float(df.parse(valueString));
      unit.setConversion(valueFloat);
    }
  }

  /**
   * Load {@link Unit} to UI. Parent method must be called first.
   *
   * @param item    the unit which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    Unit unit = (Unit)item;
    tbSymbol.setText(unit.getSymbol());
    tbConversion.setText("" + unit.getConversion());
    ddbType.setSelection(types.get(getUnit().getType()));
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();
    tbSymbol.setText("");
    ddbType.setSelection(types.get(0));
    tbConversion.setText("" + DEFAULT_CONVERSION);
  }
}
