package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.UserModel;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object types.
 *
 */
public class ApplicationTableView extends TableView implements 
    DesignerModel.ApplicationObserver, UserModel.ApplicationObserver {
  
  /**
   * @param designerView the top level view
   */
  public ApplicationTableView(final DesignerView designerView) {
    super(designerView);
    if (getModel().getUserModel().getApplications() != null) 
      onApplicationsLoaded(getModel().getUserModel().getApplications().values());
  }

  @Override
  public void onApplicationCreated(Application application) {
    add(new ApplicationRowView(getDesignerView(), application));
  }

  @Override
  public void onApplicationUpdated(Application application) {
  }

  @Override
  public void onApplicationsLoaded(Collection<Application> applications) {
    clear();
    add(getHeader());
    displayRows();
  }

  @Override
  public void onLoad() {
    getModel().addDataObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.applicationCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.applicationName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.applicationDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblCat = new Label(Main.constants.applicationCat());
    getHeader().setWidget(0, 3, lblCat);
  }

  public void displayRows() {
    Collection<Application> apps = getModel().getUserModel().getApplications().values();
    for (Application app : apps) {
      add(new ApplicationRowView(getDesignerView(), app));
    }
  }
}
