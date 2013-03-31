package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object types.
 *
 */
public class ObjectTypeTableView extends TableView implements 
    DesignerModel.ObjectTypeObserver {

  /**
   * @param designerView the top level view
   */
  public ObjectTypeTableView(final DesignerView designerView) {
    super(designerView);
    
    if (getModel().getObjectTypes() == null) {
      ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
      otl.start();
    } else
      onObjectTypesLoaded(getModel().getObjectTypes());   
  }

  @Override
  public void onObjectTypeCreated(ObjectType objectType) {
    add(new ObjectTypeRowView(getDesignerView(), objectType));
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
    clear();
    add(getHeader());
    displayRows(objectTypes);
  }

  @Override
  public void onLoad() {
    getModel().addObjectTypeObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectTypeObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.objectTypeCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.objectTypeName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.objectTypeDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblParent = new Label(Main.constants.objectTypeParent());
    getHeader().setWidget(0, 3, lblParent);
  }
  
  public void displayRows(Collection<ObjectType> ots) {
    for (ObjectType ot : ots) {
      add(new ObjectTypeRowView(getDesignerView(), ot));
    }
  }
}
