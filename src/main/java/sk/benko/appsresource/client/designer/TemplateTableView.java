package sk.benko.appsresource.client.designer;

import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.*;

import java.util.Collection;
import java.util.List;

/**
 * A widget to display table of templates.
 */
public class TemplateTableView extends TableView implements Model.ApplicationObserver, DesignerModel.TemplateObserver {

  /**
   * @param designerView the top level view
   */
  public TemplateTableView(final DesignerView designerView) {
    super(designerView);
  }

  @Override
  public void onApplicationTemplatesLoaded(List<ApplicationTemplate> appts) {
    clear();
    add(getHeader());
    displayRows(appts);
  }

  @Override
  public void onTemplateCreated(Template template) {
    TemplateRowView trw = new TemplateRowView(getDesignerView(), template, "content-row");
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

  public void displayRows(List<ApplicationTemplate> appts) {
    for (ApplicationTemplate appt : appts) {
      TemplateRowView trw = new TemplateRowView(getDesignerView(), appt.getT(), "content-row");
      trw.generateWidgetFull();
      add(trw);
    }
  }
}
