package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateGroup;

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
public class TemplateGroupRowView extends FlexTable implements 
    DesignerModel.TemplateGroupObserver {

  private DesignerModel model;
  private TemplateGroup templateGroup;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateGroupRowView(final DesignerModel model, final TemplateGroup tg) {
    setModel(model);
    setTemplateGroup(tg);
    
    setStyleName("content-row");

    generateWidget(getTemplateGroup());

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new TemplateGroupDialog(getModel(), getTemplateGroup());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(TemplateGroup tg) {
    Label lblCode = new Label(tg.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(tg.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(tg.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblT = new Label(tg.getT().getName());
    setWidget(0, 3, lblT);
    Label lblRank = new Label(""+tg.getRank());
    setWidget(0, 4, lblRank);
    Label lblSubrank = new Label(""+tg.getSubRank());
    setWidget(0, 5, lblSubrank);
  }

  @Override
  public void onTemplateGroupCreated(TemplateGroup templateGroup) {
  }

  @Override
  public void onTemplateGroupUpdated(TemplateGroup templateGroup) {
    if (getTemplateGroup().getId() == templateGroup.getId()) 
      generateWidget(templateGroup);    
  }

  @Override
  public void onTemplateGroupsLoaded(Collection<TemplateGroup> templateGroups) {
  }
  
  @Override
  public void onLoad() {
    getModel().addTemplateGroupObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeTemplateGroupObserver(this);
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
   * @return the templateGroup
   */
  public TemplateGroup getTemplateGroup() {
    return templateGroup;
  }

  /**
   * @param templateGroup the templateGroup to set
   */
  public void setTemplateGroup(TemplateGroup templateGroup) {
    this.templateGroup = templateGroup;
  }

}
