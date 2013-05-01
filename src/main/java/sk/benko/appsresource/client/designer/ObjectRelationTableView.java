package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object filterAttributes.
 *
 */
public class ObjectRelationTableView extends TableView implements 
    DesignerModel.ObjectRelationObserver {

  /**
   * @param designerView the top level view
   */
  public ObjectRelationTableView(final DesignerView designerView) {
    super(designerView);
  }

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
    add(new ObjectRelationRowView(getDesignerView(), objectRelation));
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationsLoaded(int otId,
      Collection<ObjectRelation> objectRelations) {
    if (otId == getModel().getObjectType().getId()) {
      clear();
      add(getHeader());
      displayRows(objectRelations);
    }
  }
  
  @Override
  public void onLoad() {
    getModel().addObjectRelationObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectRelationObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.objectRelationCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.objectRelationName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.objectRelationDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblOt1 = new Label(Main.constants.objectRelationOt1());
    getHeader().setWidget(0, 3, lblOt1);
    Label lblOt2 = new Label(Main.constants.objectRelationOt2());
    getHeader().setWidget(0, 4, lblOt2);
    Label lblType = new Label(Main.constants.objectRelationType());
    getHeader().setWidget(0, 5, lblType);
    Label lblComprel = new Label(Main.constants.objectRelationComprel());
    getHeader().setWidget(0, 6, lblComprel);
  }

  public void displayRows(Collection<ObjectRelation> oas) {
    for (ObjectRelation oa : oas) {
      add(new ObjectRelationRowView(getDesignerView(), oa));
    }
  }
  
  @Override
  public void filter() {
    if (getModel().getObjectType() != null)
      if (getModel().getObjectRelations().get(getModel().getObjectType().getId()) == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(),
            getModel().getObjectType().getId());
        orl.start();
      } else
        onObjectRelationsLoaded(getModel().getObjectType().getId(), 
            getModel().getObjectRelations().get(getModel().getObjectType().getId()));
  }
}
