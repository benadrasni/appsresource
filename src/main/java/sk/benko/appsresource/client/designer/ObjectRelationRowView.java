package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectRelation;

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
public class ObjectRelationRowView extends FlexTable implements 
    DesignerModel.ObjectRelationObserver {

  private DesignerModel model;
  private ObjectRelation objectRelation;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ObjectRelationRowView(final DesignerModel model, final ObjectRelation or) {
    setModel(model);
    setObjectRelation(or);
    
    setStyleName("content-row");

    generateWidget(getObjectRelation());

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new ObjectRelationDialog(getModel(), getObjectRelation());
      }});

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }}, MouseDownEvent.getType());
  }

  private void generateWidget(ObjectRelation or) {
    Label lblCode = new Label(or.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(or.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(or.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblOt1 = new Label(or.getOt1().getName());
    setWidget(0, 3, lblOt1);
    Label lblOt2 = new Label(or.getOt2().getName());
    setWidget(0, 4, lblOt2);
    
    String type = "";
    switch (or.getType()) {
    case ObjectRelation.RT_11:
      type = Main.constants.relationType11();
      break;
    case ObjectRelation.RT_1N:
      type = Main.constants.relationType1N();
      break;
    case ObjectRelation.RT_N1:
      type = Main.constants.relationTypeN1();
      break;
    case ObjectRelation.RT_NN:
      type = Main.constants.relationTypeNN();
      break;
    }
    Label lblType = new Label(type);
    setWidget(0, 5, lblType);
    Label lblComprel;
    if (or.getOr() != null)
      lblComprel = new Label(or.getOr().getName());
    else
      lblComprel = new Label("");
    setWidget(0, 6, lblComprel);
  }

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
    if (getObjectRelation().getId() == objectRelation.getId()) 
      generateWidget(objectRelation);    
  }

  @Override
  public void onObjectRelationsLoaded(int otId,
      Collection<ObjectRelation> objectRelations) {
  }
  
  @Override
  public void onLoad() {
    getModel().addObjectRelationObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectRelationObserver(this);
  }

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
   * @return the objectRelation
   */
  public ObjectRelation getObjectRelation() {
    return objectRelation;
  }

  /**
   * @param objectRelation the objectRelation to set
   */
  public void setObjectRelation(ObjectRelation objectRelation) {
    this.objectRelation = objectRelation;
  }
}
