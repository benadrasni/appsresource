package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.UserModel;

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
public class ApplicationRowView extends FlexTable implements 
    DesignerModel.ApplicationObserver, UserModel.ApplicationObserver {

  private Application application;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ApplicationRowView(final DesignerModel model, final Application app) {
    model.addDataObserver(this);
    
    setStyleName("content-row");
    setApplication(app);

    generateWidget(getApplication());

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Main.status.showTaskStatus(Main.constants.loading());
        new ApplicationDialog(model, getApplication());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(Application app) {
    Label lblCode = new Label(app.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(app.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(app.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblCat = new Label(app.getCategory());
    setWidget(0, 3, lblCat);
  }
  
  @Override
  public void onApplicationCreated(Application application) {
  }

  @Override
  public void onApplicationUpdated(Application application) {
    if (getApplication().getId() == application.getId()) 
      generateWidget(application);    
  }

  @Override
  public void onApplicationsLoaded(Collection<Application> applications) {
  }

  /**
   * @return the application
   */
  public Application getApplication() {
    return application;
  }

  /**
   * @param application the application to set
   */
  public void setApplication(Application application) {
    this.application = application;
  }
  
  
}
