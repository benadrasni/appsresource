package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.model.loader.UnitLoader;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object types.
 *
 */
public class UnitTableView extends TableView implements 
    DesignerModel.UnitObserver {
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public UnitTableView(DesignerModel model) {
    super(model);

    if (getModel().getUnits() == null) {
      UnitLoader ul = new UnitLoader(getModel());
      ul.start();
    } else
      onUnitsLoaded(getModel().getUnits());
  }

  @Override
  public void onUnitCreated(Unit unit) {
    add(new UnitRowView(getModel(), unit));
  }

  @Override
  public void onUnitUpdated(Unit unit) {
  }

  @Override
  public void onUnitsLoaded(Collection<Unit> units) {
    clear();
    add(getHeader());
    displayRows(units);
  }
  
  @Override
  public void onLoad() {
    getModel().addUnitObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeUnitObserver(this);
  }
  
  @Override
  public void initializeHeader() {
    Label lblCode = new Label(Main.constants.unitCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblSymbol = new Label(Main.constants.unitSymbol());
    getHeader().setWidget(0, 1, lblSymbol);
    getHeader().getCellFormatter().setWidth(0, 1, "60px");
    Label lblName = new Label(Main.constants.unitName());
    getHeader().setWidget(0, 2, lblName);
    Label lblDesc = new Label(Main.constants.unitDesc());
    getHeader().setWidget(0, 3, lblDesc);
    Label lblType = new Label(Main.constants.unitType());
    getHeader().setWidget(0, 4, lblType);
  }
  
  public void displayRows(Collection<Unit> units) {
    for (Unit unit : units) {
      add(new UnitRowView(getModel(), unit));
    }
  }
}
