package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display unit in table row.
 *
 */
public class UnitRowView extends FlexTable implements 
    DesignerModel.UnitObserver {

  private DesignerModel model;
  private Unit unit;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UnitRowView(final DesignerModel model, final Unit u) {
    setModel(model);
    setUnit(u);

    setStyleName("content-row");

    generateWidget(getUnit());

    // invoke dialog on left click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new UnitDialog(model, getUnit());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(Unit u) {
    Label lblCode = new Label(u.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblSymbol = new Label(u.getSymbol());
    setWidget(0, 1, lblSymbol);
    getCellFormatter().setWidth(0, 1, "60px");
    Label lblName = new Label(u.getName());
    setWidget(0, 2, lblName);
    Label lblDesc = new Label(u.getDesc());
    setWidget(0, 3, lblDesc);
    Label lblType;
    switch (u.getType()) {
    case 1:
      lblType = new Label(Main.constants.unitTypeLength());
      break;
    case 2:
      lblType = new Label(Main.constants.unitTypeWeight());
      break;
    case 3:
      lblType = new Label(Main.constants.unitTypeArea());
      break;
    case 4:
      lblType = new Label(Main.constants.unitTypeVolume());
      break;
    case 5:
      lblType = new Label(Main.constants.unitTypeTemperature());
      break;
    case 6:
      lblType = new Label(Main.constants.unitTypeAngle());
      break;
    case 7:
      lblType = new Label(Main.constants.unitTypeTime());
      break;

    default:
      lblType = new Label("");
      break;
    }
    setWidget(0, 4, lblType);
  }

  @Override
  public void onUnitCreated(Unit unit) {
  }

  @Override
  public void onUnitUpdated(Unit unit) {
    if (getUnit().getId() == unit.getId()) 
      generateWidget(unit);    
  }

  @Override
  public void onUnitsLoaded(Collection<Unit> units) {
  }
  
  @Override
  public void onLoad() {
    getModel().addUnitObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeUnitObserver(this);
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
   * @return the unit
   */
  public Unit getUnit() {
    return unit;
  }

  /**
   * @param unit the unit to set
   */
  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
