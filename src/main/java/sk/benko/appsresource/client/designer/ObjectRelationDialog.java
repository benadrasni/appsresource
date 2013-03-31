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
import sk.benko.appsresource.client.model.DesignItem;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectRelation;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.loader.ObjectRelationLoader;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A widget to display form for {@link ObjectRelation}.
 */
public class ObjectRelationDialog extends DesignerDialog implements DesignerModel.ObjectTypeObserver,
    DesignerModel.ObjectRelationObserver {
  private static ArrayList<DropDownObject> types;

  static {
    types = new ArrayList<DropDownObject>();
    types.add(new DropDownObjectImpl(ObjectRelation.RT_11, Main.constants.relationType11()));
    types.add(new DropDownObjectImpl(ObjectRelation.RT_1N, Main.constants.relationType1N()));
    types.add(new DropDownObjectImpl(ObjectRelation.RT_N1, Main.constants.relationTypeN1()));
    types.add(new DropDownObjectImpl(ObjectRelation.RT_NN, Main.constants.relationTypeNN()));
  }

  private DropDownBox ddbOt1;
  private DropDownBox ddbOt2;
  private DropDownBox ddbType;
  private DropDownBox ddbComprel;
  private FlexTable widgetObjectRelation;

  /**
   * @param designerView the top level view
   */
  public ObjectRelationDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.objectRelation());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getObjectRelation() == null)
              setItem(new ObjectRelation(getTbName().getText(),
                  ddbOt1.getSelection().getId(),
                  ddbOt2.getSelection().getId(),
                  ddbType.getSelection().getId()));
            fill(getObjectRelation());
            designerView.getDesignerModel().createOrUpdateObjectRelation(getObjectRelation());
            ObjectRelationDialog.this.hide();
          }
        }, ClickEvent.getType());

    ddbOt1 = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        loadRelations();
      }

    });
    ddbOt2 = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        loadRelations();
      }

    });
    ddbType = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    ddbType.setItems(types);
    ddbComprel = new DropDownBox(this, null, CSSConstants.SUFFIX_DESIGNER);
    reset();

    if (getModel().getObjectTypes() == null) {
      ObjectTypeLoader otl = new ObjectTypeLoader(getModel());
      otl.start();
    } else {
      fillObjectTypes(ddbOt1, getModel().getObjectTypes());
      fillObjectTypes(ddbOt2, getModel().getObjectTypes());
    }

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
  }

  @Override
  public void onObjectTypeCreated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
    fillObjectTypes(ddbOt1, objectTypes);
    fillObjectTypes(ddbOt2, objectTypes);
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


  // getters and setters

  /**
   * @return the unit
   */
  public ObjectRelation getObjectRelation() {
    return (ObjectRelation) getItem();
  }

  /**
   * Getter for property 'widgetObjectRelation'.
   *
   * @return Value for property 'widgetObjectRelation'.
   */
  @Override
  public FlexTable getItemWidget() {
    if (widgetObjectRelation == null) {
      widgetObjectRelation = new FlexTable();
      widgetObjectRelation.setWidget(0, 0, new Label(Main.constants.objectRelationCode()));
      widgetObjectRelation.setWidget(0, 1, getLblCodeValue());

      widgetObjectRelation.setWidget(1, 0, new Label(Main.constants.objectRelationName()));
      widgetObjectRelation.setWidget(1, 1, getTbName());

      widgetObjectRelation.setWidget(2, 0, new Label(Main.constants.objectRelationDesc()));
      widgetObjectRelation.setWidget(2, 1, getTbDesc());

      widgetObjectRelation.setWidget(3, 0, new Label(Main.constants.objectRelationOt1()));
      widgetObjectRelation.setWidget(3, 1, ddbOt1);
      widgetObjectRelation.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(4, 0, new Label(Main.constants.objectRelationOt2()));
      widgetObjectRelation.setWidget(4, 1, ddbOt2);
      widgetObjectRelation.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(5, 0, new Label(Main.constants.objectRelationType()));
      widgetObjectRelation.setWidget(5, 1, ddbType);
      widgetObjectRelation.getFlexCellFormatter().addStyleName(5, 1, ClientUtils.CSS_ALIGN_RIGHT);

      widgetObjectRelation.setWidget(6, 0, new Label(Main.constants.objectRelationComprel()));
      widgetObjectRelation.setWidget(6, 1, ddbComprel);
      widgetObjectRelation.getFlexCellFormatter().addStyleName(6, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }

    return widgetObjectRelation;
  }

  /**
   * Fill {@link ObjectRelation} with values from UI. Parent method must be called first.
   *
   * @param objectRelation    an object relation which should be filled with UI values
   */
  protected void fill(ObjectRelation objectRelation) {
    super.fill(objectRelation);

    objectRelation.setOt1Id(ddbOt1.getSelection().getId());
    objectRelation.setOt1((ObjectType) ddbOt1.getSelection().getUserObject());
    objectRelation.setOt2Id(ddbOt2.getSelection().getId());
    objectRelation.setOt2((ObjectType) ddbOt2.getSelection().getUserObject());
    objectRelation.setType(ddbType.getSelection().getId());
    // complementary relation
    objectRelation.setOrId(ddbComprel.getSelection().getId());
    objectRelation.setOr((ObjectRelation) ddbComprel.getSelection().getUserObject());
  }

  /**
   * Load {@link ObjectRelation} to UI. Parent method must be called first.
   *
   * @param item    the object relation which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    super.load(item);

    ObjectRelation objectRelation = (ObjectRelation) item;
    if (objectRelation != null) {
      if (objectRelation.getOt1() != null) {
        ddbOt1.setSelection(new DropDownObjectImpl(objectRelation.getOt1Id(),
            objectRelation.getOt1().getName(), objectRelation.getOt1()));
      }
      if (objectRelation.getOt2() != null) {
        ddbOt2.setSelection(new DropDownObjectImpl(objectRelation.getOt2Id(),
            objectRelation.getOt2().getName(), objectRelation.getOt2()));
      }
      if (objectRelation.getOr() != null)
        ddbComprel.setSelection(new DropDownObjectImpl(objectRelation.getOrId(),
            objectRelation.getOr().getName(), objectRelation.getOr()));

      ddbType.setSelection(types.get(objectRelation.getType() - 1));

      loadRelations();
    }
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();
    ddbOt1.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    ddbOt2.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectType()));
    ddbType.setSelection(types.get(2));
    ddbComprel.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
  }

  // private methods

  private void fillObjectTypes(DropDownBox ddb, Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    ddb.setItems(items);
  }

  private void fillObjectRelations(Collection<ObjectRelation> objectRelations) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    ddbComprel.setSelection(new DropDownObjectImpl(0, Main.constants.chooseObjectRelation()));
    for (ObjectRelation or : objectRelations) {
      if (or.getOt2Id() == ddbOt1.getSelection().getId()) {
        items.add(new DropDownObjectImpl(or.getId(), or.getName(), or));
        if (getObjectRelation() != null && getObjectRelation().getOrId() == or.getId())
          ddbComprel.setSelection(items.get(items.size() - 1));
      }
    }
    ddbComprel.setItems(items);
  }

  private void loadRelations() {
    if (ddbOt1.getSelection().getId() > 0 && ddbOt2.getSelection().getId() > 0) {
      Collection<ObjectRelation> ors = getModel().getObjectRelations().get(ddbOt2.getSelection().getId());
      if (ors == null) {
        ObjectRelationLoader orl = new ObjectRelationLoader(getModel(), ddbOt2.getSelection().getId());
        orl.start();
      } else
        fillObjectRelations(ors);
    }
  }
}
