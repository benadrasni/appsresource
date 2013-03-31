package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A widget to display form for {@link ObjectType}.
 */
public class ObjectTypeDialog extends DesignerDialog implements DesignerModel.ObjectTypeObserver {

  private DropDownBox ddbParent;
  private FlexTable widgetObjectType;

  /**
   * @param designerView the top level view
   */
  public ObjectTypeDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.objectType());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getObjectType() == null) {
              setItem(new ObjectType(getTbName().getText()));
            }
            fill(getObjectType());
            designerView.getDesignerModel().createOrUpdateObjectType(getObjectType());
            close();
          }
        }, ClickEvent.getType());

    ddbParent = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);

    if (getModel().getObjectTypes() == null) {
      ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
      otl.start();
    } else {
      fillObjectTypes(getModel().getObjectTypes());
    }

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
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
   * @param item the item to set
   */
  @Override
  public void setItem(DesignItem item) {
    super.setItem(item);
    getModel().setObjectType(getObjectType());
  }

  /**
   * @return the objectType
   */
  public ObjectType getObjectType() {
    return (ObjectType) getItem();
  }

  /**
   * Getter for property 'widgetObjectType'.
   *
   * @return Value for property 'widgetObjectType'.
   */
  @Override
  public FlexTable getItemWidget() {
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
      widgetObjectType.setWidget(3, 1, ddbParent);
      widgetObjectType.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }

    return widgetObjectType;
  }

  /**
   * Fill {@link ObjectType} with values from UI. Parent method must be called first.
   *
   * @param objectType the object type which should be filled with UI values
   */
  protected void fill(ObjectType objectType) {
    super.fill(objectType);

    // parent
    objectType.setParentId(ddbParent.getSelection().getId());
    objectType.setParent((ObjectType) ddbParent.getSelection().getUserObject());
  }

  /**
   * Load {@link ObjectType} to UI. Parent method must be called first.
   *
   * @param item the object type which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    ObjectType objectType = (ObjectType) item;
    if (objectType != null) {
      if (objectType.getParent() != null) {
        ddbParent.setSelection(new DropDownObjectImpl(objectType.getParentId(),
            objectType.getParent().getName(), objectType.getParent()));
      }
    }
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();

    ddbParent.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
  }


  // private methods

  private void fillObjectTypes(Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    ddbParent.setItems(items);
  }
}
