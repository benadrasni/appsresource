package sk.benko.appsresource.client.designer;

import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.loader.TemplateRelationLoader;

import java.util.Collections;
import java.util.List;

/**
 * A widget to display table of template groups.
 */
public class TemplateRelationTableView extends TableView implements
    Model.TemplateRelationObserver,
    DesignerModel.TemplateRelationUpdateObserver {

  /**
   * @param model the model to which the UI will bind itself
   */
  public TemplateRelationTableView(DesignerModel model) {
    super(model);
  }

  @Override
  public void onTemplateRelationCreated(TemplateRelation templateRelation) {
    add(new TemplateRelationRowView(getModel(), templateRelation));
  }

  @Override
  public void onTemplateRelationUpdated(TemplateRelation templateRelation) {
  }

  @Override
  public void onTemplateRelationsLoaded(Template t, List<TemplateRelation> templateRelations) {
    clear();
    add(getHeader());
    displayRows(templateRelations);
  }

  @Override
  public void onLoad() {
    getModel().addTemplateRelationObserver(this);
    getModel().addTemplateRelationUpdateObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeTemplateRelationObserver(this);
    getModel().removeTemplateRelationUpdateObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.templateRelationCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.templateRelationName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.templateRelationDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblT1 = new Label(Main.constants.templateRelationT1());
    getHeader().setWidget(0, 3, lblT1);
    Label lblOr = new Label(Main.constants.templateRelationOr());
    getHeader().setWidget(0, 4, lblOr);
    Label lblT2 = new Label(Main.constants.templateRelationT2());
    getHeader().setWidget(0, 5, lblT2);
    Label lblRank = new Label(Main.constants.templateRelationRank());
    getHeader().setWidget(0, 6, lblRank);
    Label lblSubrank = new Label(Main.constants.templateRelationSubrank());
    getHeader().setWidget(0, 7, lblSubrank);
  }

  @Override
  public void filter() {
    if (getModel().getTemplate() != null)
      if (getModel().getRelsByTemplate().get(getModel().getTemplate().getId()) == null) {
        TemplateRelationLoader tal = new TemplateRelationLoader(getModel(),
            getModel().getTemplate());
        tal.start();
      } else
        onTemplateRelationsLoaded(getModel().getTemplate(),
            getModel().getRelsByTemplate().get(getModel().getTemplate().getId()));
  }

  public void displayRows(List<TemplateRelation> trs) {
    Collections.sort(trs);
    for (TemplateRelation tr : trs) {
      add(new TemplateRelationRowView(getModel(), tr));
    }
  }

}
