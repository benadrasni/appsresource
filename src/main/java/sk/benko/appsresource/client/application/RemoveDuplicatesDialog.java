package sk.benko.appsresource.client.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.designer.WidthUnitListBox;
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TreeLevel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * A generic widget to display designer dialog.
 *
 */
public class RemoveDuplicatesDialog extends PopupPanel implements ApplicationModel.ObjectsObserver {
  
  private static int ROWS = 5;
  private int removeCount = 0;
  
  ObjectView objectView;
  
  List<TemplateAttribute> tas  = new ArrayList<TemplateAttribute>();

  final AbsolutePanel main = new AbsolutePanel();
  HorizontalPanel header = new HorizontalPanel();
  Label x = new Label(ClientUtils.CLOSE_CHAR);

  FlowPanel body = new FlowPanel();
  FlowPanel bodyContent = new FlowPanel();
  FlowPanel bodyBottom = new FlowPanel();
  FlowPanel bodyButtons = new FlowPanel();
  FlexTable table = new FlexTable(); 
  
  final ButtonView bRemove = new ButtonView(ClientUtils.CSS_BUTTON + " " 
      + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONOK);
  final ButtonView bCancel = new ButtonView(ClientUtils.CSS_BUTTON + " " 
       + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONCANCEL);

  /**
   * 
   */
  public RemoveDuplicatesDialog(final ObjectView objectView) {
    setStyleName(ClientUtils.CSS_DIALOGBOX);
    setObjectView(objectView);

    // Enable glass background.
    setGlassStyleName(ClientUtils.CSS_DIALOGBOX_GLASS);
    setGlassEnabled(true);
    
    
    // Set the dialog box's header.
    header.setStyleName(ClientUtils.CSS_DIALOGBOX_HEADER);
    header.add(new Label(Main.constants.eliminateDuplicates()));
    
    main.add(header);
    x.setStyleName(ClientUtils.CSS_DIALOGBOX_X);
    main.add(x);

    // Set the dialog box's body.
    int width = Window.getClientWidth()*2 / 3;
    int height = Window.getClientHeight()*2 / 3;
    body.setHeight(height + WidthUnitListBox.WIDTH_UNITPX);
    body.setWidth(width + WidthUnitListBox.WIDTH_UNITPX);
    body.setStyleName(ClientUtils.CSS_DIALOGBOX_BODY);

    bodyContent.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_CONTENT);
    bodyBottom.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_BOTTOM);
    bodyButtons.setStyleName(ClientUtils.CSS_IMPORTDIALOGBOX_BUTTONS);

    bodyButtons.add(bCancel);
    bodyButtons.add(bRemove);
    bodyBottom.add(bodyButtons);
    
    body.add(bodyContent);
    body.add(bodyBottom);
    main.add(body);
    
    add(main);
    
    setPopupPositionAndShow(new PopupPanel.PositionCallback() { 
      public void setPosition(int offsetWidth, int offsetHeight) { 
              int left = (Window.getClientWidth() - offsetWidth) / 2;
              int top = (Window.getClientHeight() - offsetHeight) / 2;
              setPopupPosition(left, top); 
          } 
    }); 
    
    initialize();
  }
  
  private void initialize() {
    
    x.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        RemoveDuplicatesDialog.this.hide();
      }
    });
    
    // load template attributes for actual template
    tas = objectView.getModel().getAttrsByTemplate().get(getModel().getAppt().getTId());
    
    int offset = -2;
    for (int i = 0; i < tas.size(); i++) {
      TemplateAttribute ta = tas.get(i);
      if (i%ROWS == 0) {
        offset = offset+2;
        table.setWidget(0, offset+1, new Label(Main.constants.key()));
      }

      table.setWidget(i%ROWS+1, offset, new Label(ta.getName()));
      
      CheckBox cbKey = new CheckBox();
      table.setWidget(i%ROWS+1, offset+1, cbKey);
      
    }

    bodyContent.add(table);
    
    // buttons
    bCancel.addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            RemoveDuplicatesDialog.this.hide();
          }
        }, ClickEvent.getType());
    bCancel.getElement().setInnerText(Main.constants.cancel());
    
    bRemove.addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              bRemove.addStyleName(ClientUtils.CSS_DISABLED);
              Main.status.showTaskStatus(Main.constants.remove());
              removeDuplicates();
              //ImportObjectsDialog.this.hide();
            }
        }, ClickEvent.getType());
    bRemove.getElement().setInnerText(Main.constants.importFile());
  }
  
  private void removeDuplicates() {
    HashMap<Integer, TemplateAttribute> keys = new HashMap<Integer, TemplateAttribute>();

    int offset = -2;
    for (int i = 0; i < tas.size(); i++) {
      if (i%ROWS == 0) offset = offset+2;
      if (((CheckBox)table.getWidget(i%ROWS+1, offset+1)).getValue())
        keys.put(i, tas.get(i));
    }
    
    getModel().removeDuplicates(
        getModel().getAppt().getApp(), 
        getModel().getAppt().getT(), keys); 
  }

  @Override
  public void onObjectsLoaded(TreeItem ti, List<AObject> objects) {
  }

  @Override
  public void onTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items) {
  }

  @Override
  public void onObjectsImported(int count) {
  }
  
  public void onLoad() {
    getModel().addDataObserver(this);
  }

  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  @Override
  public void onObjectsRemoved(int count) {
    removeCount += count;
    if (count == 0) {
      Main.status.hideTaskStatus();
      bodyContent.clear();
      bodyContent.add(new Label(Main.messages.objectsRemoved(removeCount)));
    } else 
      removeDuplicates();
  }

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
  
}
