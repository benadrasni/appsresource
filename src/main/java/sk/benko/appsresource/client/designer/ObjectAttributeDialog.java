package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.*;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;
import sk.benko.appsresource.client.model.loader.UnitLoader;
import sk.benko.appsresource.client.model.loader.ValueTypeLoader;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class ObjectAttributeDialog extends DesignerDialog implements DesignerModel.ObjectTypeObserver,
    DesignerModel.ValueTypeObserver, DesignerModel.UnitObserver, DesignerModel.ObjectRelationObserver {
  private ArrayList<DropDownObject> objectTypesItems;

  private DropDownBox ddbObjectType;
  private DropDownBox ddbValueType;
  private DropDownBox ddbUnit;
  private Label lblRelatedObjectType;
  private DropDownBox ddbRelatedObjectType;
  private Label lblRelation;
  private DropDownBox ddbRelation;
  private FlexTable widgetObjectAttribute;

  /**
   * @param designerView the top level view
   */
  public ObjectAttributeDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.objectAttribute());
    objectTypesItems = new ArrayList<DropDownObject>();

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getObjectAttribute() == null)
              setItem(new ObjectAttribute(getTbName().getText(),
                  ddbObjectType.getSelection().getId(),
                  ddbValueType.getSelection().getId()));
            fill(getObjectAttribute());
            designerView.getDesignerModel().createOrUpdateObjectAttribute(getObjectAttribute());
            ObjectAttributeDialog.this.hide();
          }
        }, ClickEvent.getType());

    ddbObjectType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbValueType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER,
        new ChangeHandler() {

          @Override
          public void onChange(ChangeEvent event) {
            setVisibility();
          }
        });
    ddbUnit = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    lblRelatedObjectType = new Label(Main.constants.objectAttributeRCOT());
    ddbRelatedObjectType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER,
        new ChangeHandler() {

          @Override
          public void onChange(ChangeEvent event) {
            loadRelations();
          }
    });
    lblRelation = new Label(Main.constants.objectAttributeRCOR());
    ddbRelatedObjectType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER,
        new ChangeHandler() {

          @Override
          public void onChange(ChangeEvent event) {
            loadRelations();
          }
    });
    ddbRelation = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);

    if (getModel().getObjectTypes() == null) {
      ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
      otl.start();
    } else {
      fillObjectTypes(getModel().getObjectTypes());
    }

    if (getModel().getValueTypes() == null) {
      ValueTypeLoader vtl = new ValueTypeLoader(getModel());
      vtl.start();
    } else {
      fillValueTypes(getModel().getValueTypes());
    }

    if (getModel().getUnits() == null) {
      UnitLoader ul = new UnitLoader(getModel());
      ul.start();
    } else {
      fillUnits(getModel().getUnits());
    }

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  @Override
  public void onUnitCreated(Unit unit) {
  }

  @Override
  public void onUnitUpdated(Unit unit) {
  }

  @Override
  public void onUnitsLoaded(Collection<Unit> units) {
    fillUnits(units);
  }

  @Override
  public void onValueTypeCreated(ValueType valueType) {
  }

  @Override
  public void onValueTypeUpdated(ValueType valueType) {
  }

  @Override
  public void onValueTypesLoaded(Collection<ValueType> valueTypes) {
    fillValueTypes(valueTypes);
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

  @Override
  public void onObjectRelationCreated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationUpdated(ObjectRelation objectRelation) {
  }

  @Override
  public void onObjectRelationsLoaded(int otId, Collection<ObjectRelation> objectRelations) {
    fillObjectRelations(objectRelations);
  }

  /**
   * @return the objectAttribute
   */
  public ObjectAttribute getObjectAttribute() {
    return (ObjectAttribute) getItem();
  }

  /**
   * Getter for property 'widgetObjectAttribute'.
   *
   * @return Value for property 'widgetObjectAttribute'.
   */
  @Override
  public FlexTable getItemWidget() {
    if (widgetObjectAttribute == null) {
      widgetObjectAttribute = new FlexTable();
      Label lblCode = new Label(Main.constants.objectAttributeCode());
      widgetObjectAttribute.setWidget(0, 0, lblCode);
      widgetObjectAttribute.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.objectAttributeName());
      widgetObjectAttribute.setWidget(1, 0, lblName);
      widgetObjectAttribute.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.objectAttributeDesc());
      widgetObjectAttribute.setWidget(2, 0, lblDesc);
      widgetObjectAttribute.setWidget(2, 1, getTbDesc());

      Label lblOt = new Label(Main.constants.objectAttributeOt());
      widgetObjectAttribute.setWidget(3, 0, lblOt);
      widgetObjectAttribute.setWidget(3, 1, ddbObjectType);
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblType = new Label(Main.constants.objectAttributeType());
      widgetObjectAttribute.setWidget(4, 0, lblType);
      widgetObjectAttribute.setWidget(4, 1, ddbValueType);
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblUnit = new Label(Main.constants.objectAttributeUnit());
      widgetObjectAttribute.setWidget(5, 0, lblUnit);
      widgetObjectAttribute.setWidget(5, 1, ddbUnit);
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }

    return widgetObjectAttribute;
  }

  /**
   * Fill {@link ObjectAttribute} with values from UI. Parent method must be called first.
   *
   * @param objectAttribute    the object attribute which should be filled with UI values
   */
  protected void fill(ObjectAttribute objectAttribute) {
    super.fill(objectAttribute);

    // object type
    objectAttribute.setOtId(ddbObjectType.getSelection().getId());
    objectAttribute.setOt((ObjectType) ddbObjectType.getSelection().getUserObject());
    // value type
    objectAttribute.setVtId(ddbValueType.getSelection().getId());
    objectAttribute.setVt((ValueType) ddbValueType.getSelection().getUserObject());
    // unit
    objectAttribute.setUnitId(ddbUnit.getSelection().getId());
    objectAttribute.setUnit((Unit) ddbUnit.getSelection().getUserObject());

    // shared
    switch (objectAttribute.getVt().getType()) {
      case ValueType.VT_REF:
        objectAttribute.setShared1(ddbRelatedObjectType.getSelection().getId());
        objectAttribute.setShared2(ddbRelation.getSelection().getId());
        objectAttribute.setShared4(0);
        objectAttribute.setShared5(0);
        break;

      default:
        objectAttribute.setShared1(0);
        objectAttribute.setShared2(0);
        objectAttribute.setShared3(0);
        objectAttribute.setShared4(0);
        objectAttribute.setShared5(0);
        break;
    }
  }

  /**
   * Load {@link ObjectAttribute} to UI. Parent method must be called first.
   *
   * @param item    the object attribute which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    ObjectAttribute objectAttribute = (ObjectAttribute)item;
    if (objectAttribute != null) {
      if (objectAttribute.getOt() != null) {
        ddbObjectType.setSelection(new DropDownObjectImpl(objectAttribute.getOtId(),
            objectAttribute.getOt().getName(), objectAttribute.getOt()));
      }
      if (objectAttribute.getVt() != null) {
        ddbValueType.setSelection(new DropDownObjectImpl(objectAttribute.getVtId(),
            objectAttribute.getVt().getName(), objectAttribute.getVt()));
        setVisibility();
      }
      if (objectAttribute.getUnit() != null) {
        ddbUnit.setSelection(new DropDownObjectImpl(objectAttribute.getUnitId(),
            objectAttribute.getUnit().getName(), objectAttribute.getUnit()));
      }
      if (objectAttribute.getShared1() > 0) {
        for (DropDownObject objectTypeItem : objectTypesItems) {
          if (objectTypeItem.getId() == objectAttribute.getShared1()) {
            ddbRelatedObjectType.setSelection(objectTypeItem);
            loadRelations();
            break;
          }
        }
      }
    }
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();

    ddbObjectType.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    ddbValueType.setSelection(new DropDownObjectImpl(0, Main.constants.chooseValueType()));
    ddbUnit.setSelection(new DropDownObjectImpl(0, Main.constants.chooseUnit()));
    ddbRelatedObjectType.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    ddbRelation.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    ddbRelation.clearItems();

    setVisibility();
  }

  // private methods

  private void fillObjectTypes(Collection<ObjectType> objectTypes) {
    DropDownObjectImpl chooseItem = new DropDownObjectImpl(0, Main.constants.chooseObjectType());

    for (ObjectType ot : objectTypes) {
      DropDownObjectImpl item = new DropDownObjectImpl(ot.getId(), ot.getName(), ot);
      objectTypesItems.add(item);

      if (getObjectAttribute() != null) {
        if (getObjectAttribute().getOt() != null) {
          if (getObjectAttribute().getOtId() == ot.getId()) {
            ddbObjectType.setSelection(item);
          }
        } else {
          ddbObjectType.setSelection(chooseItem);
        }
        if (getObjectAttribute().getShared1() == ot.getId()) {
          ddbRelatedObjectType.setSelection(item);
          loadRelations();
        }
      } else {
        ddbRelatedObjectType.setSelection(chooseItem);
        ddbObjectType.setSelection(chooseItem);
      }
    }

    ddbObjectType.setItems(objectTypesItems);
    ddbRelatedObjectType.setItems(objectTypesItems);
  }

  private void fillValueTypes(Collection<ValueType> valueTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseValueType()));
    for (ValueType vt : valueTypes) {
      items.add(new DropDownObjectImpl(vt.getId(), vt.getName(), vt));
    }
    ddbValueType.setItems(items);
  }

  private void fillUnits(Collection<Unit> units) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseUnit()));
    for (Unit u : units) {
      items.add(new DropDownObjectImpl(u.getId(), u.getName(), u));
    }
    ddbUnit.setItems(items);
  }

  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    ddbRelation.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    for (ObjectRelation or : objectRelations) {
      if (or.getOt2Id() == ddbRelatedObjectType.getSelection().getId()
          && or.getOt1Id() == ddbObjectType.getSelection().getId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getObjectAttribute() != null && getObjectAttribute().getShared2() == or.getId())
          ddbRelation.setSelection(items.get(items.size() - 1));
      }
    }
    ddbRelation.setItems(items);
  }

  private void setVisibility() {
    ValueType vt = (ValueType) ddbValueType.getSelection().getUserObject();
    if (vt != null && vt.getType() == ValueType.VT_REF) {
      widgetObjectAttribute.setWidget(6, 0, lblRelatedObjectType);
      widgetObjectAttribute.setWidget(6, 1, ddbRelatedObjectType);
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(6, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectAttribute.setWidget(7, 0, lblRelation);
      widgetObjectAttribute.setWidget(7, 1, ddbRelation);
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(7, 1, ClientUtils.CSS_ALIGN_RIGHT);
    } else {
      if (widgetObjectAttribute.getRowCount() == 8) {
        widgetObjectAttribute.removeRow(7);
        widgetObjectAttribute.removeRow(6);
      }
    }
  }

  private void loadRelations() {
    if (ddbRelatedObjectType.getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations().get(ddbRelatedObjectType.getSelection().getId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(), ddbRelatedObjectType.getSelection().getId());
        orl.start();
      } else
        fillObjectRelations(ors);
    }
  }
}
