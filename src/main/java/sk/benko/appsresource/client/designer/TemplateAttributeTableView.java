package sk.benko.appsresource.client.designer;

import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.TableView;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.TemplateAttributeLoader;

import java.util.Collections;
import java.util.List;

/**
 * A widget to display table of template groups.
 */
public class TemplateAttributeTableView extends TableView implements
    Model.TemplateAttributeObserver,
    DesignerModel.TemplateAttributeUpdateObserver {

  /**
   * @param model the model to which the Ui will bind itself
   */
  public TemplateAttributeTableView(DesignerModel model) {
    super(model);
  }

  @Override
  public void onTemplateAttributeCreated(TemplateAttribute templateAttribute) {
    TemplateAttributeRowView tarw = new TemplateAttributeRowView(templateAttribute, "content-row");
    tarw.addHandlers(getModel());
    tarw.generateWidgetFull();
    getModel().addTemplateAttributeUpdateObserver(tarw);
    add(tarw);
  }

  @Override
  public void onTemplateAttributeUpdated(TemplateAttribute templateAttribute) {
  }

  @Override
  public void onTemplateAttributesLoaded(Template t, List<TemplateAttribute> tas, TemplateRelation tr) {
    clear();
    add(getHeader());
    displayRows(tas);
  }

  @Override
  public void onLoad() {
    getModel().addTemplateAttributeObserver(this);
    getModel().addTemplateAttributeUpdateObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeTemplateAttributeObserver(this);
    getModel().removeTemplateAttributeUpdateObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.templateAttributeCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.templateAttributeName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.templateAttributeDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblTg = new Label(Main.constants.templateAttributeTg());
    getHeader().setWidget(0, 3, lblTg);
    Label lblOa = new Label(Main.constants.templateAttributeOa());
    getHeader().setWidget(0, 4, lblOa);
    Label lblStyle = new Label(Main.constants.templateAttributeStyle());
    getHeader().setWidget(0, 5, lblStyle);
    Label lblDefault = new Label(Main.constants.templateAttributeDefault());
    getHeader().setWidget(0, 6, lblDefault);
    Label lblLength = new Label(Main.constants.templateAttributeLength());
    getHeader().setWidget(0, 7, lblLength);
  }

  @Override
  public void filter() {
    if (getModel().getTemplate() != null)
      if (getModel().getAttrsByTemplate().get(getModel().getTemplate().getId()) == null) {
        TemplateAttributeLoader tal = new TemplateAttributeLoader(getModel(),
            getModel().getTemplate());
        tal.start();
      } else
        onTemplateAttributesLoaded(getModel().getTemplate(),
            getModel().getAttrsByTemplate().get(getModel().getTemplate().getId()),
            null);
  }

  public void displayRows(List<TemplateAttribute> tas) {
    Collections.sort(tas, new TemplateAttributeComparator());
    for (TemplateAttribute ta : tas) {
      TemplateAttributeRowView tarw = new TemplateAttributeRowView(ta, "content-row");
      tarw.addHandlers(getModel());
      tarw.generateWidgetFull();
      getModel().addTemplateAttributeUpdateObserver(tarw);
      add(tarw);
    }
  }
}
