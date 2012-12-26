package sk.benko.appsresource.client.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.ToolTipBox;
import sk.benko.appsresource.client.application.ImportObjectsDialog;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.application.RemoveDuplicatesDialog;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.ui.widget.theme.ThemeImage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A widget to display the section name.
 *
 */
public class ObjectToolbarView extends FlowPanel implements ApplicationModel.ObjectObserver {
  private static Label objectStatus;

  private ObjectView objectView;
  private FlexTable toolbar;
  
  private ButtonView bAddCopy;
  private ButtonView bDelete;
  private ButtonView bTools;
  
  private PopupPanel toolsMenu;
  private FlowPanel toolsContent;
  private Label lblRemoveDuplicates;
  private Label lblImport;

  /**
   * @param objectView    the object view to which the UI will bind itself
   */
  public ObjectToolbarView(final ObjectView objectView) {
    setObjectView(objectView);
    
    getElement().setId(CSSConstants.CSS_CONTENT_TOOLBAR);

    add(getToolbar());
  }
  
  public void initialize(ApplicationTemplate appt) {
    getObjectStatus().setText("");
  }

  
  private void setItems() {
    getToolsContent().add(getLblRemoveDuplicates());
    getToolsContent().add(getLblImport());
  }
  
  private void showList() {
    getToolsMenu().show();
    int top = getBTools().getAbsoluteTop()+getBTools().getOffsetHeight();
    int left = getBTools().getAbsoluteLeft()+getBTools().getOffsetWidth()-getToolsMenu().getOffsetWidth();
    getToolsMenu().setPopupPosition(left, top);
  }

  @Override
  public void onObjectCreated(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    getObjectStatus().setText(Main.constants.allDataSaved());
    Main.status.hideTaskStatus();
  }

  @Override
  public void onObjectDeleted(AObject object) {
    getObjectStatus().setText(Main.constants.objectDeleted());
  }

  public void onLoad() {
    getModel().addObjectObserver(this);
  }

  public void onUnload() {
    getModel().removeObjectObserver(this);
  }

  // getters and setters
  
  public ApplicationModel getModel() {
    return getObjectView().getModel();
  }
  
  public ObjectTemplate getObjectTemplate() {
    return getObjectView().getOcv().getOtempv().getOtemp();
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
   * Getter for property 'toolbar'.
   * 
   * @return Value for property 'toolbar'.
   */
  protected FlexTable getToolbar() {
    if (toolbar == null) {
      toolbar = new FlexTable();
      toolbar.setWidget(0, 0, getBAddCopy());
      toolbar.getCellFormatter().setWidth(0, 0, "100px");
      toolbar.setWidget(0, 1, getBDelete());
      toolbar.getCellFormatter().setWidth(0, 1, "100px");
      toolbar.setWidget(0, 2, objectStatus);
      toolbar.setWidget(0, 4, getBTools());
      toolbar.getCellFormatter().setStyleName(0, 4, ClientUtils.CSS_ALIGN_RIGHT);
    }
    return toolbar;
  }
  
  /**
   * Getter for property 'bAddCopy'.
   * 
   * @return Value for property 'bAddCopy'.
   */
  protected ButtonView getBAddCopy() {
    if (bAddCopy == null) {
      bAddCopy = new ButtonView("inline-block button", 
          "100px", new ThemeImage("addcopy.png"));
      if (getModel().getAppu().isWrite()) 
        bAddCopy.addClickHandler( 
            new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (getModel().getObject() != null) {
                  AObject aobject = new AObject(0, 
                      getModel().getAppt().getT().getOtId(), 0, "");
                  List<AValue> values = copyValues(getModel().getObject(), getModel().getAllValues());
                  getModel().createObject(aobject, values);
                }
              }
        });
      else
        bAddCopy.addStyleName(ClientUtils.CSS_DISABLED);
      
      final ToolTipBox tooltipbox = new ToolTipBox(bAddCopy, Main.constants.copy());
      bAddCopy.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          tooltipbox.show();
        }}, MouseOverEvent.getType()); 
      bAddCopy.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          tooltipbox.hide();
        }}, MouseOutEvent.getType());
    }
    return bAddCopy;
  }
  
  /**
   * Getter for property 'bDelete'.
   * 
   * @return Value for property 'bDelete'.
   */
  protected ButtonView getBDelete() {
    if (bDelete == null) {
      bDelete = new ButtonView("inline-block button", 
          "100px", new ThemeImage("delete.png"));
      if (getModel().getAppu().isWrite()) 
        bDelete.addClickHandler( 
            new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (getModel().getObject() != null) 
                  getModel().deleteObject();
              }
        });
      else
        bDelete.addStyleName(ClientUtils.CSS_DISABLED);
      
      final ToolTipBox tooltipboxDelete = new ToolTipBox(bDelete, Main.constants.delete());
      bDelete.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          tooltipboxDelete.show();
        }}, MouseOverEvent.getType()); 
      bDelete.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          tooltipboxDelete.hide();
        }}, MouseOutEvent.getType());
    }
    return bDelete;
  }
  
  /**
   * Getter for property 'objectStatus'.
   * 
   * @return Value for property 'objectStatus'.
   */
  public static Label getObjectStatus() {
    if (objectStatus == null) {
      objectStatus = new Label();
      objectStatus.setStyleName("object-message");
    }
    return objectStatus;
  }
  
  /**
   * Getter for property 'bTools'.
   * 
   * @return Value for property 'bTools'.
   */
  protected ButtonView getBTools() {
    if (bTools == null) {
      final ToolTipBox tooltipboxTools = new ToolTipBox(bTools, 
          Main.constants.tools());
      bTools = new ButtonView("inline-block button", 
          "50px", new ThemeImage("tools.png"));
      if (getModel().getAppu().isWrite()) 
        bTools.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            tooltipboxTools.hide();
            showList();
          }
        });
      else
        bTools.addStyleName(ClientUtils.CSS_DISABLED);
      
      bTools.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          tooltipboxTools.show();
        }}, MouseOverEvent.getType()); 
      bTools.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          tooltipboxTools.hide();
        }}, MouseOutEvent.getType());
    }
    return bTools;
  }
  
  /**
   * Getter for property 'toolsMenu'.
   * 
   * @return Value for property 'toolsMenu'.
   */
  protected PopupPanel getToolsMenu() {
    if (toolsMenu == null) {
      toolsMenu = new PopupPanel(true);
      toolsMenu.setStyleName(ClientUtils.CSS_DROPDOWN_LIST);
      toolsMenu.add(getToolsContent());
    }
    return toolsMenu;
  }
  
  /**
   * Getter for property 'toolsContent'.
   * 
   * @return Value for property 'toolsContent'.
   */
  protected FlowPanel getToolsContent() {
    if (toolsContent == null) {
      toolsContent = new FlowPanel();
      toolsContent.setStyleName(ClientUtils.CSS_DROPDOWN_CONTENT);
      setItems();
    }
    return toolsContent;
  }
  
  /**
   * Getter for property 'lblRemoveDuplicates'.
   * 
   * @return Value for property 'lblRemoveDuplicates'.
   */
  public Label getLblRemoveDuplicates() {
    if (lblRemoveDuplicates == null) {
      lblRemoveDuplicates = new Label(Main.constants.eliminateDuplicates());
      lblRemoveDuplicates.setStyleName(ClientUtils.CSS_DROPDOWN_ITEM);
      lblRemoveDuplicates.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          getToolsMenu().hide();
          new RemoveDuplicatesDialog(getObjectView());
        }
      });
      
      lblRemoveDuplicates.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          lblRemoveDuplicates.addStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }}, MouseOverEvent.getType()); 
      lblRemoveDuplicates.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          lblRemoveDuplicates.removeStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }}, MouseOutEvent.getType()); 

    }
    return lblRemoveDuplicates;
  }
  
  /**
   * Getter for property 'lblImport'.
   * 
   * @return Value for property 'lblImport'.
   */
  public Label getLblImport() {
    if (lblImport == null) {
      lblImport = new Label(Main.constants.importObjects());
      lblImport.setStyleName(ClientUtils.CSS_DROPDOWN_ITEM);
      lblImport.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          getToolsMenu().hide();
          new ImportObjectsDialog(getObjectView());
        }
      });
      
      lblImport.addDomHandler(new MouseOverHandler() {
        public void onMouseOver(MouseOverEvent event) {
          lblImport.addStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }}, MouseOverEvent.getType()); 
      lblImport.addDomHandler(new MouseOutHandler() {
        public void onMouseOut(MouseOutEvent event) {
          lblImport.removeStyleName(ClientUtils.CSS_DROPDOWN_ITEMHIGHLIGHT);
        }}, MouseOutEvent.getType()); 

    }
    return lblImport;
  }
  
  // private methods
  
  private List<AValue> copyValues(AObject object, Map<Integer, Map<Integer, List<AValue>>> allValues) {
    
    List<AValue> result = new ArrayList<AValue>();
    Map<Integer, List<AValue>> vals = allValues.get(object.getId());
    for (List<AValue> attrvals : vals.values()) {
      for (AValue value : attrvals) {
        AValue newvalue = value.copy();
        newvalue.setId(0);
        newvalue.setOId(0);
        if (newvalue.getOaId() == getModel().getAppt().getT().getOaId())
          newvalue.setValueString(Main.constants.newItem() + " " 
              + getModel().getAppt().getT().getName());
        result.add(newvalue);
      }
    }
    
    return result;
  }

}
