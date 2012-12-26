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
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.Unit;
import sk.benko.appsresource.client.model.ValueType;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;
import sk.benko.appsresource.client.model.loader.UnitLoader;
import sk.benko.appsresource.client.model.loader.ValueTypeLoader;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 *
 */
public class ObjectAttributeDialog extends DesignerDialog implements 
    DesignerModel.ObjectTypeObserver, 
    DesignerModel.ValueTypeObserver,
    DesignerModel.UnitObserver,
    DesignerModel.ObjectRelationObserver {
  
  private DropDownBox ddbObjectType;
  private DropDownBox ddbValueType;
  private DropDownBox ddbUnit;
  private Label lblRCOT; 
  private DropDownBox ddbRCOT;
  private Label lblRCOR; 
  private DropDownBox ddbRCOR;
  
  private FlexTable widgetObjectAttribute;
  
  /**
   * @param model
   *          the model to which the UI will bind itself
   */
  public ObjectAttributeDialog(final DesignerModel model, ObjectAttribute oa) {
    super(model, oa);
    getModel().addObjectTypeObserver(this);
    getModel().addValueTypeObserver(this);
    getModel().addUnitObserver(this);
    getModel().addObjectRelationObserver(this);
    
    getHeader().add(new Label((getObjectAttribute() == null ? Main.constants.newItem() + " " 
        : "") + Main.constants.objectAttribute()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.objectAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName("dialog-box-navigation-item dialog-box-navigation-item-selected");

    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetObjectAttribute());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getObjectAttribute() == null)
                setItem(new ObjectAttribute(getTbName().getText(),
                    getDdbObjectType().getSelection().getId(),
                    getDdbValueType().getSelection().getId()));
              fill(getObjectAttribute());
              model.createOrUpdateObjectAttribute(getObjectAttribute());
              ObjectAttributeDialog.this.hide();
            }
        }, ClickEvent.getType());
  }

  @Override
  public void close() {
    getModel().removeObjectTypeObserver(this);
    getModel().removeValueTypeObserver(this);
    getModel().removeUnitObserver(this);
    getModel().removeObjectRelationObserver(this);
    hide();
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
  public void onObjectRelationsLoaded(int otId,
      Collection<ObjectRelation> objectRelations) {
    fillObjectRelations(objectRelations);
  }
  
  /**
   * @return the objectAttribute
   */
  public ObjectAttribute getObjectAttribute() {
    return (ObjectAttribute)getItem();
  }
  
  /**
   * Getter for property 'ddbObjectType'.
   * 
   * @return Value for property 'ddbObjectType'.
   */
  protected DropDownBox getDdbObjectType() {
    if (ddbObjectType == null) {
      ddbObjectType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      if (getObjectAttribute() != null && getObjectAttribute().getOt() != null)  
        ddbObjectType.setSelection(new DropDownObjectImpl(getObjectAttribute().getOtId(), 
            getObjectAttribute().getOt().getName(), getObjectAttribute().getOt()));
      else
        ddbObjectType.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectType()));
      
      if (getModel().getObjectTypes() == null) {
        ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
        otl.start();    
      } else
        fillObjectTypes(getModel().getObjectTypes());
    }
    return ddbObjectType;
  }  
  
  /**
   * Getter for property 'ddbValueType'.
   * 
   * @return Value for property 'ddbValueType'.
   */
  protected DropDownBox getDdbValueType() {
    if (ddbValueType == null) {
      ddbValueType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER,
          new ChangeHandler() {
        
        @Override
        public void onChange(ChangeEvent event) {
          setVisibility();
        }
      });
      
      if (getObjectAttribute() != null && getObjectAttribute().getVt() != null) { 
        ddbValueType.setSelection(new DropDownObjectImpl(getObjectAttribute().getVtId(), 
            getObjectAttribute().getVt().getName(), getObjectAttribute().getVt()));
        setVisibility();
      } else
        ddbValueType.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseValueType()));
      
      if (getModel().getValueTypes() == null) {
        ValueTypeLoader vtl = new ValueTypeLoader(getModel());
        vtl.start();    
      } else
        fillValueTypes(getModel().getValueTypes());
    }
    return ddbValueType;
  }
  
  /**
   * Getter for property 'ddbUnit'.
   * 
   * @return Value for property 'ddbUnit'.
   */
  protected DropDownBox getDdbUnit() {
    if (ddbUnit == null) {
      ddbUnit = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      if (getObjectAttribute() != null && getObjectAttribute().getUnit() != null)  
        ddbUnit.setSelection(new DropDownObjectImpl(getObjectAttribute().getUnitId(), 
            getObjectAttribute().getUnit().getName(), getObjectAttribute().getUnit()));
      else
        ddbUnit.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseUnit()));
      
      if (getModel().getUnits() == null) {
        UnitLoader ul = new UnitLoader(getModel());
        ul.start();    
      } else
        fillUnits(getModel().getUnits());
    }
    return ddbUnit;
  }
  
  /**
   * Getter for property 'lblRCOT'.
   * 
   * @return Value for property 'lblRCOT'.
   */
  protected Label getLblRCOT() {
    if (lblRCOT == null) {
      lblRCOT = new Label(Main.constants.objectAttributeRCOT());
    }
    return lblRCOT;
  } 
  
  /**
   * Getter for property 'ddbRCOT'.
   * 
   * @return Value for property 'ddbRCOT'.
   */
  protected DropDownBox getDdbRCOT() {
    if (ddbRCOT == null) {
      ddbRCOT = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
              loadRelations();
            }
      });
    }
    
    return ddbRCOT;
  }  

  /**
   * Getter for property 'lblRCOR'.
   * 
   * @return Value for property 'lblRCOR'.
   */
  protected Label getLblRCOR() {
    if (lblRCOR == null) {
      lblRCOR = new Label(Main.constants.objectAttributeRCOR());
    }
    return lblRCOR;
  } 
  
  /**
   * Getter for property 'ddbRCOR'.
   * 
   * @return Value for property 'ddbRCOR'.
   */
  protected DropDownBox getDdbRCOR() {
    if (ddbRCOR == null) {
      ddbRCOR = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    }
    return ddbRCOR;
  } 
  
  /**
   * Getter for property 'widgetObjectAttribute'.
   * 
   * @return Value for property 'widgetObjectAttribute'.
   */
  public FlexTable getWidgetObjectAttribute() {
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
      widgetObjectAttribute.setWidget(3, 1, getDdbObjectType());
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblType = new Label(Main.constants.objectAttributeType());
      widgetObjectAttribute.setWidget(4, 0, lblType);
      widgetObjectAttribute.setWidget(4, 1, getDdbValueType());
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);
      
      Label lblUnit = new Label(Main.constants.objectAttributeUnit());
      widgetObjectAttribute.setWidget(5, 0, lblUnit);
      widgetObjectAttribute.setWidget(5, 1, getDdbUnit());
      widgetObjectAttribute.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }
    
    return widgetObjectAttribute;
  }
  
  // private methods
  
  private void fill(ObjectAttribute objectAttribute) {
    super.fill(objectAttribute);

    // object type
    objectAttribute.setOtId(getDdbObjectType().getSelection().getId());
    objectAttribute.setOt((ObjectType)getDdbObjectType().getSelection().getUserObject());
    // value type
    objectAttribute.setVtId(getDdbValueType().getSelection().getId());
    objectAttribute.setVt((ValueType)getDdbValueType().getSelection().getUserObject());
    // unit
    objectAttribute.setUnitId(getDdbUnit().getSelection().getId());
    objectAttribute.setUnit((Unit)getDdbUnit().getSelection().getUserObject());
    
    // shared
    switch (objectAttribute.getVt().getType()) {
      case ValueType.VT_REF:
        objectAttribute.setShared1(getDdbRCOT().getSelection().getId());
        objectAttribute.setShared2(getDdbRCOR().getSelection().getId());
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
  
  private void fillObjectTypes (Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
      if (getObjectAttribute() != null && 
          getObjectAttribute().getShared1() == ot.getId())
        getDdbRCOT().setSelection(items.get(items.size()-1));
    }
    getDdbObjectType().setItems(items);
    getDdbRCOT().setItems(items);
    loadRelations();
  }

  private void fillValueTypes (Collection<ValueType> valueTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseValueType()));
    for (ValueType vt : valueTypes) {
      items.add(new DropDownObjectImpl(vt.getId(), vt.getName(), vt));
    }
    getDdbValueType().setItems(items);
  }

  private void fillUnits (Collection<Unit> units) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    items.add(new DropDownObjectImpl(0, Main.constants.chooseUnit()));
    for (Unit u : units) {
      items.add(new DropDownObjectImpl(u.getId(), u.getName(), u));
    }
    getDdbUnit().setItems(items);
  }
  
  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ObjectRelation or : objectRelations) {
      if (or.getOt2Id() == getDdbRCOT().getSelection().getId()
           && or.getOt1Id() == getDdbObjectType().getSelection().getId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getObjectAttribute() != null && getObjectAttribute().getShared2() == or.getId())
          getDdbRCOR().setSelection(items.get(items.size()-1));
      }
    }
    getDdbRCOR().setItems(items);
    if (items.size() == 1)
      getDdbRCOR().setSelection(items.get(0));
    else
      getDdbRCOR().setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
  }

  private void setVisibility() {
    ValueType vt = (ValueType)getDdbValueType().getSelection().getUserObject();
    if (vt != null && vt.getType() == ValueType.VT_REF) {
      getWidgetObjectAttribute().setWidget(6, 0, getLblRCOT());
      getWidgetObjectAttribute().setWidget(6, 1, getDdbRCOT());
      getWidgetObjectAttribute().getFlexCellFormatter().addStyleName(6, 1, ClientUtils.CSS_ALIGN_RIGHT);

      getWidgetObjectAttribute().setWidget(7, 0, getLblRCOR());
      getWidgetObjectAttribute().setWidget(7, 1, getDdbRCOR());
      getWidgetObjectAttribute().getFlexCellFormatter().addStyleName(7, 1, ClientUtils.CSS_ALIGN_RIGHT);
      
      getDdbRCOT().setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    } else {
      if (getWidgetObjectAttribute().getRowCount() == 8) {
        getWidgetObjectAttribute().removeRow(7);
        getWidgetObjectAttribute().removeRow(6);
      }
    }
  }
  
  private void loadRelations() {
    if (getDdbRCOT().getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations()
          .get(getDdbRCOT().getSelection().getId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(), 
            getDdbRCOT().getSelection().getId());
        orl.start();    
      } else
        fillObjectRelations(ors);
    }
  }
}
