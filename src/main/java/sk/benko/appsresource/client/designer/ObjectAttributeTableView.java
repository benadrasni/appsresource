package sk.benko.appsresource.client.designer;

import java.util.Collection;

import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.designer.model.ObjectAttributeLoader;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.designer.layout.TableView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectAttribute;

import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display table of object attributes.
 *
 */
public class ObjectAttributeTableView extends TableView implements 
    DesignerModel.ObjectAttributeObserver {

  /**
   * @param designerView the top level view
   */
  public ObjectAttributeTableView(final DesignerView designerView) {
    super(designerView);
  }

  @Override
  public void onObjectAttributeCreated(ObjectAttribute objectAttribute) {
    add(new ObjectAttributeRowView(getDesignerView(), objectAttribute));
  }

  @Override
  public void onObjectAttributeUpdated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributesLoaded(int otId, 
      Collection<ObjectAttribute> objectAttributes) {
    clear();
    add(getHeader());
    displayRows(objectAttributes);
  }
  
  @Override
  public void onLoad() {
    getModel().addObjectAttributeObserver(this);
  }

  @Override
  public void onUnload() {
    getModel().removeObjectAttributeObserver(this);
  }

  @Override
  public void initializeHeader() {
    getHeader().clear();
    Label lblCode = new Label(Main.constants.objectAttributeCode());
    getHeader().setWidget(0, 0, lblCode);
    getHeader().getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(Main.constants.objectAttributeName());
    getHeader().setWidget(0, 1, lblName);
    Label lblDesc = new Label(Main.constants.objectAttributeDesc());
    getHeader().setWidget(0, 2, lblDesc);
    Label lblOt = new Label(Main.constants.objectAttributeOt());
    getHeader().setWidget(0, 3, lblOt);
    Label lblType = new Label(Main.constants.objectAttributeType());
    getHeader().setWidget(0, 4, lblType);
    Label lblUnit = new Label(Main.constants.objectAttributeUnit());
    getHeader().setWidget(0, 5, lblUnit);
  }

  public void displayRows(Collection<ObjectAttribute> oas) {
    for (ObjectAttribute oa : oas) 
      add(new ObjectAttributeRowView(getDesignerView(), oa));
  }
  
  @Override
  public void filter() {
    if (getModel().getObjectType() != null)
      if (getModel().getObjectAttributes().get(getModel().getObjectType().getId()) == null) {
        ObjectAttributeLoader oal = new ObjectAttributeLoader(getModel(),
            getModel().getObjectType().getId());
        oal.start();
      } else
        onObjectAttributesLoaded(getModel().getObjectType().getId(), 
            getModel().getObjectAttributes().get(getModel().getObjectType().getId()));
  }
}
