package sk.benko.appsresource.client.layout;

import java.util.ArrayList;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ImportObjectsDialog;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.util.ThemeImage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class ObjectActionButtonsView extends FlowPanel {
  ObjectView objectView;

  private FlexTable toolbar;

  private ButtonView bCreate;
  private ButtonView bImport;

  /**
   * @param objectView
   */
  public ObjectActionButtonsView(final ObjectView objectView) {
    setObjectView(objectView);
    setStyleName("buttons_bar");

    add(getToolbar());
  }
  
  public void initialize() {
  }

  // getters and setters
  
  /**
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return objectView;
  }

  /**
   * @param objectView the objectView to set
   */
  public void setObjectView(ObjectView objectView) {
    this.objectView = objectView;
  }
  
  /**
   * @return the applicationModel
   */
  public ApplicationModel getModel() {
    return getObjectView().getModel();
  }  
  
  /**
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  protected FlexTable getToolbar() {
    if (toolbar == null) {
      toolbar = new FlexTable();
      toolbar.setWidget(0, 0, getBCreate());
      toolbar.getCellFormatter().setWidth(0, 0, "100px");
      toolbar.setWidget(0, 1, getBImport());
    }
    return toolbar;
  }
  
  /**
   * Getter for property 'bCreate'.
   * 
   * @return Value for property 'bCreate'.
   */
  protected ButtonView getBCreate() {
    if (bCreate == null) {
      bCreate = new ButtonView(Main.constants.create(), 
          "inline-block button button-collapse-right", "84px");
      if (getModel().getAppu().isWrite()) 
        bCreate.addClickHandler( 
            new ClickHandler() {
              public void onClick(ClickEvent event) {
                AObject aobject = new AObject(0, 
                    getModel().getAppt().getT().getOtId(), 0, "");
                ArrayList<AValue> values = new ArrayList<AValue>();
                AValue value = new AValue(0, getModel().getAppt().getT().getOaId(),
                    Main.constants.newItem() + " " + getModel().getAppt().getT().getName(),
                    Main.language);
                values.add(value);
                getModel().createObject(aobject, values);
              }
        });
      else
        bCreate.addStyleName(ClientUtils.CSS_DISABLED);
    }
    return bCreate;
  }
  
  /**
   * Getter for property 'bImport'.
   * 
   * @return Value for property 'bImport'.
   */
  protected ButtonView getBImport() {
    if (bImport == null) {
      bImport = new ButtonView("inline-block button button-collapse-left", "", 
          new ThemeImage("upload.png"));
      if (getModel().getAppu().isWrite()) 
        bImport.addClickHandler( 
            new ClickHandler() {
              public void onClick(ClickEvent event) {
                new ImportObjectsDialog(objectView);
              }
        });
      else
        bImport.addStyleName(ClientUtils.CSS_DISABLED);
    }
    return bImport;
  }
}
