package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateGroupLoader;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of template groups.
 *
 */
public class TemplateGroupTableView extends TableView implements 
    DesignerModel.TemplateGroupObserver {
  
  /**
   * @param designerView the top level view
   */
  public TemplateGroupTableView(final DesignerView designerView) {
    super(designerView);
  }

  @Override
  public void onTemplateGroupCreated(TemplateGroup templateGroup) {
    add(new TemplateGroupRowView(getDesignerView(), templateGroup));
  }

  @Override
  public void onTemplateGroupUpdated(TemplateGroup templateGroup) {
  }

  @Override
  public void onTemplateGroupsLoaded(Collection<TemplateGroup> templateGroups) {
    clear();
    add(getHeader());
    displayRows(templateGroups);
  }

  @Override
  public void onLoad() {
    getModel().addTemplateGroupObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeTemplateGroupObserver(this);
  }
  
  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.templateGroupCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.templateGroupName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.templateGroupDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblT = new Label(Main.constants.templateGroupT());
    getHeader().setWidget(0, 3, lblT);
    Label lblRank = new Label(Main.constants.templateGroupRank());
    getHeader().setWidget(0, 4, lblRank);
    Label lblSubrank = new Label(Main.constants.templateGroupSubrank());
    getHeader().setWidget(0, 5, lblSubrank);
  }

  @Override
  public void filter() {
    if (getModel().getTemplate() != null)
      if (getModel().getTemplateGroups().get(getModel().getTemplate().getId()) == null) {
        TemplateGroupLoader tgl = new TemplateGroupLoader(getModel(),
            getModel().getTemplate());
        tgl.start();
      } else
        onTemplateGroupsLoaded(getModel().getTemplateGroups()
            .get(getModel().getTemplate().getId()));
  }

  public void displayRows(Collection<TemplateGroup> tgs) {
    for (TemplateGroup tg : tgs) {
      add(new TemplateGroupRowView(getDesignerView(), tg));
    }
  }  
}
