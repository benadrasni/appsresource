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
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.ui.widget.DoubleTextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A widget to display form for {@link Unit}.
 *
 */
public class UnitDialog extends DesignerDialog {
  private static final float DEFAULT_CONVERSION = 1;

  private NumberFormat df; 
    
  private TextBox tbSymbol;
  private DropDownBox ddbType;
  private Label lblConversion;
  private DoubleTextBox tbConversion;
  
  private FlexTable widgetUnit;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UnitDialog(final DesignerModel model, Unit unit) {
    super(model, unit);
    
    getHeader().add(new Label((getUnit() == null ? 
        Main.constants.newItem() + " " : "") + Main.constants.unit()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.unit(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);

    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetUnit());
    
    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getUnit() == null)
              setItem(new Unit(getTbName().getText(), getTbSymbol().getText()));
            fill(getUnit());
            model.createOrUpdateUnit(getUnit());
            close();
          }
        }, ClickEvent.getType());
  }

  /**
   * @return the unit
   */
  public Unit getUnit() {
    return (Unit)getItem();
  }
  
  /**
   * Getter for property 'df'.
   * 
   * @return Value for property 'df'.
   */
  protected NumberFormat getDf() {
    if (df == null) {
      df = NumberFormat.getFormat(ClientUtils.NUMBERFORMAT);
    }

    return df;
  }
  
  
  /**
   * Getter for property 'tbSymbol'.
   * 
   * @return Value for property 'tbSymbol'.
   */
  public TextBox getTbSymbol() {
    if (tbSymbol == null) {
      tbSymbol = new TextBox();
      tbSymbol.setWidth(TBWIDTH_SMALL);
      if (getUnit() != null) 
        tbSymbol.setText(getUnit().getSymbol());
    }  
    return tbSymbol;
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
        
      items.add(new DropDownObjectImpl(Unit.TYPE_NONE,Main.constants.choose()));
      items.add(new DropDownObjectImpl(Unit.TYPE_LENGTH, 
          Main.constants.unitTypeLength()));
      items.add(new DropDownObjectImpl(Unit.TYPE_WEIGHT, 
          Main.constants.unitTypeWeight()));
      items.add(new DropDownObjectImpl(Unit.TYPE_AREA, 
          Main.constants.unitTypeArea()));
      items.add(new DropDownObjectImpl(Unit.TYPE_VOLUME, 
          Main.constants.unitTypeVolume()));
      items.add(new DropDownObjectImpl(Unit.TYPE_TEMPERATURE, 
          Main.constants.unitTypeTemperature()));
      items.add(new DropDownObjectImpl(Unit.TYPE_ANGLE, 
          Main.constants.unitTypeAngle()));
      items.add(new DropDownObjectImpl(Unit.TYPE_TIME, 
          Main.constants.unitTypeTime()));
      
      if (getUnit() != null)
        ddbType.setSelection(items.get(getUnit().getType()));
      else
        ddbType.setSelection(items.get(0));
      ddbType.setItems(items);
 
    }
    return ddbType;
  } 

  /**
   * Getter for property 'lblConversion'.
   * 
   * @return Value for property 'lblConversion'.
   */
  public Label getLblConversion() {
    if (lblConversion == null) {
      lblConversion = new Label(Main.constants.unitConversion());
    }  
    return lblConversion;
  }

  /**
   * Getter for property 'tbConversion'.
   * 
   * @return Value for property 'tbConversion'.
   */
  public DoubleTextBox getTbConversion() {
    if (tbConversion == null) {
      tbConversion = new DoubleTextBox(getLblConversion(), getBOk());
      if (getUnit() != null) 
        tbConversion.setText(""+getUnit().getConversion());
      else
        tbConversion.setText(""+DEFAULT_CONVERSION);

    }  
    return tbConversion;
  }
  
  /**
   * Getter for property 'widgetUnit'.
   * 
   * @return Value for property 'widgetUnit'.
   */
  public FlexTable getWidgetUnit() {
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
      widgetUnit.setWidget(3, 1, getTbSymbol());
      
      Label lblType = new Label(Main.constants.unitType());
      widgetUnit.setWidget(4, 0, lblType);
      widgetUnit.setWidget(4, 1, getDdbType());
      widgetUnit.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetUnit.setWidget(5, 0, getLblConversion());
      widgetUnit.setWidget(5, 1, getTbConversion());
    }  
    return widgetUnit;
  }
  
  // private methods

  private void fill(Unit unit) {
    super.fill(unit);

    unit.setSymbol(getTbSymbol().getText());
    unit.setType(getDdbType().getSelection().getId());
    String valueString = getTbConversion().getText().trim();
    if (valueString.length() > 0) {
      float valueFloat = new Float(getDf().parse(valueString));
      unit.setConversion(valueFloat);
    }
  }

}
