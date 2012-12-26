package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.TemplateRelation;

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
public class TemplateRelationRowView extends FlexTable implements 
    DesignerModel.TemplateRelationUpdateObserver {

  private DesignerModel model;
  private TemplateRelation templateRelation;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateRelationRowView(final DesignerModel model, final TemplateRelation tr) {
    setModel(model);
    setTemplateRelation(tr);
    
    setStyleName("content-row");

    generateWidget(getTemplateRelation());

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new TemplateRelationDialog(getModel(), getTemplateRelation());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(TemplateRelation tr) {
    Label lblCode = new Label(tr.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(tr.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(tr.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblT1 = new Label(tr.getT1().getName());
    setWidget(0, 3, lblT1);
    Label lblOr = new Label(tr.getOr().getName());
    setWidget(0, 4, lblOr);
    Label lblT2 = new Label(tr.getT2().getName());
    setWidget(0, 5, lblT2);
    Label lblRank = new Label(""+tr.getRank());
    setWidget(0, 6, lblRank);
    Label lblSubrank = new Label(""+tr.getSubrank());
    setWidget(0, 7, lblSubrank);
  }

  @Override
  public void onTemplateRelationCreated(TemplateRelation templateRelation) {
  }

  @Override
  public void onTemplateRelationUpdated(TemplateRelation templateRelation) {
    if (getTemplateRelation().getId() == templateRelation.getId()) 
      generateWidget(templateRelation);    
  }

  @Override
  public void onLoad() {
    getModel().addTemplateRelationUpdateObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeTemplateRelationUpdateObserver(this);
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
   * @return the templateRelation
   */
  public TemplateRelation getTemplateRelation() {
    return templateRelation;
  }

  /**
   * @param templateRelation the templateRelation to set
   */
  public void setTemplateRelation(TemplateRelation templateRelation) {
    this.templateRelation = templateRelation;
  }

}
