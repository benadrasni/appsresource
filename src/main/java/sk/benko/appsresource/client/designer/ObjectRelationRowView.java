package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectRelation;

import java.util.Collection;

/**
 * A widget to display object type in table row.
 */
public class ObjectRelationRowView extends FlexTable implements DesignerModel.ObjectRelationObserver {

  private DesignerView designerView;
  private ObjectRelation objectRelation;

  /**
   * @param designerView   the top level view
   * @param objectRelation the object relation
   */
  public ObjectRelationRowView(final DesignerView designerView, final ObjectRelation objectRelation) {
    this.designerView = designerView;
    this.objectRelation = objectRelation;

    setStyleName("content-row");

    generateWidget(objectRelation);

    // invoke dialog on double click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getObjectRelationDialog().setItem(objectRelation);
        designerView.getObjectRelationDialog().show();
      }
    });

    // disable text highlighting
    addDomHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
      }
    }, MouseDownEvent.getType());
  }

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
    if (this.objectRelation.getId() == objectRelation.getId())
      generateWidget(objectRelation);
  }

  @Override
  public void onObjectRelationsLoaded(int otId, Collection<ObjectRelation> objectRelations) {
  }

  @Override
  public void onLoad() {
    designerView.getDesignerModel().addObjectRelationObserver(this);
  }

  @Override
  public void onUnload() {
    designerView.getDesignerModel().removeObjectRelationObserver(this);
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
}
