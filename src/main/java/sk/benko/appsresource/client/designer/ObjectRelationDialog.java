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
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 *
 */
public class ObjectRelationDialog extends DesignerDialog implements 
    DesignerModel.ObjectTypeObserver,
    DesignerModel.ObjectRelationObserver {
  
  private DropDownBox ddbOt1;
  private DropDownBox ddbOt2;
  private DropDownBox ddbType;
  private DropDownBox ddbComprel;
  
  private FlexTable widgetObjectRelation;
  
  /**
   * @param model
   *          the model to which the UI will bind itself
   * @param or
   *          the object relation
   */
  public ObjectRelationDialog(final DesignerModel model, ObjectRelation or) {
    super(model, or);
    getModel().addObjectTypeObserver(this);
    getModel().addObjectRelationObserver(this);
    
    getHeader().add(new Label((getObjectRelation() == null ? Main.constants.newItem() + " " 
        : "") + Main.constants.objectRelation()));

    NavigationLabelView menu1 = new NavigationLabelView(
        getModel(), Main.constants.objectRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
      }
    }); 
    menu1.addStyleName("dialog-box-navigation-item dialog-box-navigation-item-selected");

    getBodyLeft().add(menu1);
    getBodyRight().add(getWidgetObjectRelation());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getObjectRelation() == null)
                setItem(new ObjectRelation(getTbName().getText(),
                    getDdbOt1().getSelection().getId(),
                    getDdbOt2().getSelection().getId(),
                    getDdbType().getSelection().getId()));
              fill(getObjectRelation());
              getModel().createOrUpdateObjectRelation(getObjectRelation());
              ObjectRelationDialog.this.hide();
            }
        }, ClickEvent.getType());
  }

  @Override
  public void close() {
    getModel().removeObjectTypeObserver(this);
    getModel().removeObjectRelationObserver(this);
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
    fillObjectTypes(getDdbOt1(), objectTypes);
    fillObjectTypes(getDdbOt2(), objectTypes);
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

  
  // getters and setters

  /**
   * @return the unit
   */
  public ObjectRelation getObjectRelation() {
    return (ObjectRelation)getItem();
  }  
  
  /**
   * Getter for property 'ddbOt1'.
   * 
   * @return Value for property 'ddbOt1'.
   */
  protected DropDownBox getDdbOt1() {
    if (ddbOt1 == null) {
      ddbOt1 = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          loadRelations();
        }
  
      });
      if (getObjectRelation() != null && getObjectRelation().getOt1() != null)   
        ddbOt1.setSelection(new DropDownObjectImpl(getObjectRelation().getOt1Id(), 
            getObjectRelation().getOt1().getName(), getObjectRelation().getOt1()));
      else
        ddbOt1.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectType()));
      
      if (getModel().getObjectTypes() == null) {
        ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
        otl.start();    
      } else
        fillObjectTypes(ddbOt1, getModel().getObjectTypes());
    }
    return ddbOt1;
  }    
  
  /**
   * Getter for property 'ddbOt2'.
   * 
   * @return Value for property 'ddbOt2'.
   */
  protected DropDownBox getDdbOt2() {
    if (ddbOt2 == null) {
      ddbOt2 = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          loadRelations();
        }
  
      });
      if (getObjectRelation() != null && getObjectRelation().getOt2() != null) {  
        ddbOt2.setSelection(new DropDownObjectImpl(getObjectRelation().getOt2Id(), 
            getObjectRelation().getOt2().getName(), getObjectRelation().getOt2()));
        loadRelations();
      } else
        ddbOt2.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectType()));
      
      if (getModel().getObjectTypes() != null) 
        fillObjectTypes(ddbOt2, getModel().getObjectTypes());
    }
    return ddbOt2;
  }     
  
  /**
   * Getter for property 'ddbType'.
   * 
   * @return Value for property 'ddbType'.
   */
  protected DropDownBox getDdbType() {
    if (ddbType == null) {
      ddbType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      
      ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
        
      items.add(new DropDownObjectImpl(ObjectRelation.RT_11, 
          Main.constants.relationType11()));
      items.add(new DropDownObjectImpl(ObjectRelation.RT_1N, 
          Main.constants.relationType1N()));
      items.add(new DropDownObjectImpl(ObjectRelation.RT_N1, 
          Main.constants.relationTypeN1()));
      items.add(new DropDownObjectImpl(ObjectRelation.RT_NN, 
          Main.constants.relationTypeNN()));
      
      if (getObjectRelation() != null)
        ddbType.setSelection(items.get(getObjectRelation().getType()-1));
      else
        ddbType.setSelection(items.get(2));
      ddbType.setItems(items);
 
    }
    return ddbType;
  }   
  
  /**
   * Getter for property 'ddbComprel'.
   * 
   * @return Value for property 'ddbComprel'.
   */
  protected DropDownBox getDdbComprel() {
    if (ddbComprel == null) {
      ddbComprel = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
      if (getObjectRelation() != null && getObjectRelation().getOr() != null)  
        ddbComprel.setSelection(new DropDownObjectImpl(getObjectRelation().getOrId(), 
            getObjectRelation().getOr().getName(), getObjectRelation().getOr()));
      else
        ddbComprel.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectRelation()));
    }
      
    return ddbComprel;
  } 

  /**
   * Getter for property 'widgetObjectRelation'.
   * 
   * @return Value for property 'widgetObjectRelation'.
   */
  public FlexTable getWidgetObjectRelation() {
    if (widgetObjectRelation == null) {
      widgetObjectRelation = new FlexTable();
      widgetObjectRelation.setWidget(0, 0, new Label(Main.constants.objectRelationCode()));
      widgetObjectRelation.setWidget(0, 1, getLblCodeValue());

      widgetObjectRelation.setWidget(1, 0, new Label(Main.constants.objectRelationName()));
      widgetObjectRelation.setWidget(1, 1, getTbName());

      widgetObjectRelation.setWidget(2, 0, new Label(Main.constants.objectRelationDesc()));
      widgetObjectRelation.setWidget(2, 1, getTbDesc());

      widgetObjectRelation.setWidget(3, 0, new Label(Main.constants.objectRelationOt1()));
      widgetObjectRelation.setWidget(3, 1, getDdbOt1());
      widgetObjectRelation.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(4, 0, new Label(Main.constants.objectRelationOt2()));
      widgetObjectRelation.setWidget(4, 1, getDdbOt2());
      widgetObjectRelation.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(5, 0, new Label(Main.constants.objectRelationType()));
      widgetObjectRelation.setWidget(5, 1, getDdbType());
      widgetObjectRelation.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(6, 0, new Label(Main.constants.objectRelationComprel()));
      widgetObjectRelation.setWidget(6, 1, getDdbComprel());    
      widgetObjectRelation.getFlexCellFormatter().addStyleName(6, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }
    
    return widgetObjectRelation;
  }

  // private methods
  
  private void fill(ObjectRelation objectRelation) {
    super.fill(objectRelation);

    objectRelation.setOt1Id(getDdbOt1().getSelection().getId());
    objectRelation.setOt1((ObjectType)getDdbOt1().getSelection().getUserObject());
    objectRelation.setOt2Id(getDdbOt2().getSelection().getId());
    objectRelation.setOt2((ObjectType)getDdbOt2().getSelection().getUserObject());
    objectRelation.setType(getDdbType().getSelection().getId());
    // complementary relation
    objectRelation.setOrId(getDdbComprel().getSelection().getId());
    objectRelation.setOr((ObjectRelation)getDdbComprel().getSelection().getUserObject());
  }

  private void fillObjectTypes(DropDownBox ddb, Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    ddb.setItems(items);
  }

  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    getDdbComprel().setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    for (ObjectRelation or : objectRelations) {
      if (or.getOt2Id() == getDdbOt1().getSelection().getId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getObjectRelation() != null && getObjectRelation().getOrId() == or.getId())
          getDdbComprel().setSelection(items.get(items.size()-1));
      }
    }
    getDdbComprel().setItems(items);
  }

  private void loadRelations() {
    if (getDdbOt1().getSelection().getId() > 0 
        && getDdbOt2().getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations()
          .get(getDdbOt2().getSelection().getId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(), 
            getDdbOt2().getSelection().getId());
        orl.start();    
      } else
        fillObjectRelations(ors);
    }
  }
}
