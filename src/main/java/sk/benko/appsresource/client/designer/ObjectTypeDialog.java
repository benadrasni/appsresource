package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * A widget to display form for {@link ObjectType}.
 *
 */
public class ObjectTypeDialog extends DesignerDialog implements
    DesignerModel.ObjectTypeObserver {
  
  private DropDownBox ddbParent;
  private FlexTable widgetObjectType;
  
  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public ObjectTypeDialog(final DesignerModel model, ObjectType ot) {
    super(model, ot);
    getModel().setObjectType(ot);
    getModel().addObjectTypeObserver(this);
    
    getHeader().add(new Label((getObjectType() == null ? 
        Main.constants.newItem() + " " : "") + Main.constants.objectType()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.objectType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM + " " 
        + ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
    
    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetObjectType());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getObjectType() == null)
                setItem(new ObjectType(getTbName().getText()));
              fill(getObjectType());
              model.createOrUpdateObjectType(getObjectType());
              close();
            }
        }, ClickEvent.getType());
  }

  public void close() {
    getModel().removeObjectTypeObserver(this);
    hide();
  }
  
  @Override
  public void onObjectTypeCreated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
    fillObjectTypes(objectTypes);
  }
  
  
  /**
   * @return the objectType
   */
  public ObjectType getObjectType() {
    return (ObjectType)getItem();
  }  
  
  /**
   * Getter for property 'ddbParent'.
   * 
   * @return Value for property 'ddbParent'.
   */
  protected DropDownBox getDdbParent() {
    if (ddbParent == null) {
      ddbParent = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      if (getModel().getObjectType() != null && getModel().getObjectType().getParent() != null)  
        ddbParent.setSelection(new DropDownObjectImpl(getModel().getObjectType().getParentId(), 
            getModel().getObjectType().getParent().getName(), getModel().getObjectType().getParent()));
      else
        ddbParent.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectType()));
      
      if (getModel().getObjectTypes() == null) {
        ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
        otl.start();    
      } else
        fillObjectTypes(getModel().getObjectTypes());
    }
    return ddbParent;
  }  
  
  /**
   * Getter for property 'widgetObjectType'.
   * 
   * @return Value for property 'widgetObjectType'.
   */
  public FlexTable getWidgetObjectType() {
    if (widgetObjectType == null) {
      widgetObjectType = new FlexTable();

      Label lblCode = new Label(Main.constants.objectTypeCode());
      widgetObjectType.setWidget(0, 0, lblCode);
      widgetObjectType.setWidget(0, 1, getLblCodeValue());
      
      Label lblName = new Label(Main.constants.objectTypeName());
      widgetObjectType.setWidget(1, 0, lblName);
      widgetObjectType.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.objectTypeDesc());
      widgetObjectType.setWidget(2, 0, lblDesc);
      widgetObjectType.setWidget(2, 1, getTbDesc());

      Label lblParent = new Label(Main.constants.objectTypeParent());
      widgetObjectType.setWidget(3, 0, lblParent);
      widgetObjectType.setWidget(3, 1, getDdbParent());
      widgetObjectType.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }
    
    return widgetObjectType;
  }
  
  // private methods
  
  private void fill(ObjectType objectType) {
    super.fill(objectType);
    
    // parent
    objectType.setParentId(getDdbParent().getSelection().getId());
    objectType.setParent((ObjectType)getDdbParent().getSelection().getUserObject());
  }
  
  private void fillObjectTypes (Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    getDdbParent().setItems(items);
  }
}
