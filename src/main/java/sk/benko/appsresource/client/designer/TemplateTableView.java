package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.TableView;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.ApplicationTemplateLoader;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of templates.
 *
 */
public class TemplateTableView extends TableView implements 
  Model.ApplicationObserver, DesignerModel.TemplateObserver {

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateTableView(DesignerModel model) {
    super(model);
  }

  @Override
  public void onApplicationTemplatesLoaded(ArrayList<ApplicationTemplate> appts) {
    clear();
    add(getHeader());
    displayRows(appts);
  }
  
  
  @Override
  public void onTemplateCreated(Template template) {
    TemplateRowView trw = new TemplateRowView(template, "content-row");
    trw.setModel(getModel());
    trw.addHandlers();
    trw.generateWidgetFull();
    add(trw);
  }

  @Override
  public void onTemplateUpdated(Template template) {
  }

  @Override
  public void onTemplatesLoaded(Collection<Template> templates) {
  }
  
  @Override
  public void onLoad() {
    getModel().addDataObserver(this);
    getModel().addTemplateObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeDataObserver(this);
    getModel().removeTemplateObserver(this);
  }
  
  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.templateCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.templateName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.templateDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblOt = new Label(Main.constants.templateOt());
    getHeader().setWidget(0, 3, lblOt);
    Label lblOa = new Label(Main.constants.templateTa());
    getHeader().setWidget(0, 4, lblOa);
  }
  
  @Override
  public void filter() {
    if (getModel().getApplication() != null)
      if (getModel().getAppTemplatesByApp().get(getModel().getApplication().getId()) == null) {
        ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
            getModel().getApplication());
        atl.start();
      } else
        onApplicationTemplatesLoaded(getModel().getAppTemplatesByApp()
            .get(getModel().getApplication().getId()));
  }
  
  public void displayRows(ArrayList<ApplicationTemplate> appts) {
    for (ApplicationTemplate appt : appts) {
      TemplateRowView trw = new TemplateRowView(appt.getT(), "content-row");
      trw.setModel(getModel());
      trw.addHandlers();
      trw.generateWidgetFull();
      add(trw);
    }
  }
}
