package sk.benko.appsresource.client.designer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.ObjectType;

import java.util.Collection;

/**
 * A widget to display object type in table row.
 */
public class ObjectTypeRowView extends FlexTable implements
    DesignerModel.ObjectTypeObserver {

  private DesignerView designerView;
  private ObjectType objectType;

  /**
   * @param designerView    the top level view
   * @param objectType      the object type
   */
  public ObjectTypeRowView(final DesignerView designerView, final ObjectType objectType) {
    this.designerView = designerView;
    this.objectType = objectType;

    setStyleName("content-row");

    generateWidget(objectType);

    // invoke dialog on click
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {

        designerView.getObjectTypeDialog().setItem(objectType);
        designerView.getObjectTypeDialog().show();
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
  public void onObjectTypeCreated(ObjectType objectType) {
  }

  @Override
  public void onObjectTypeUpdated(ObjectType objectType) {
    if (this.objectType.getId() == objectType.getId())
      generateWidget(objectType);
  }

  @Override
  public void onObjectTypesLoaded(Collection<ObjectType> objectTypes) {
  }

  @Override
  public void onLoad() {
    designerView.getDesignerModel().addObjectTypeObserver(this);
  }

  @Override
  public void onUnload() {
    designerView.getDesignerModel().removeObjectTypeObserver(this);
  }

  private void generateWidget(ObjectType ot) {
    Label lblCode = new Label(ot.getCode());
    setWidget(0, 0, lblCode);
    getCellFormatter().setWidth(0, 0, "100px");
    Label lblName = new Label(ot.getName());
    setWidget(0, 1, lblName);
    Label lblDesc = new Label(ot.getDesc());
    setWidget(0, 2, lblDesc);
    Label lblParent;
    if (ot.getParent() != null)
      lblParent = new Label(ot.getParent().getName());
    else
      lblParent = new Label("");
    setWidget(0, 3, lblParent);
  }
}
