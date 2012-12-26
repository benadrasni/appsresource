package sk.benko.appsresource.client.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.layout.ObjectView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.ui.ObjectListener;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;

public class ObjectPicker extends TextButtonPanel implements ObjectListener<ObjectsTree>, HasChangeHandlers {

  /** tree popup panels */
  private PopupPanel objectsTreePanel;
  private PopupPanel triangleOuterPanel;
  private PopupPanel triangleInnerPanel;
  /** objects tree widget */
  private ObjectsTree objectsTree;
  /** open calendar event handler */
  private ClickHandler openTreeClickHandler;
  /** the object template */
  private ObjectTemplate objectTemplate;
  /** the template attribute */
  private TemplateAttribute templateAttribute;
  /** the object */
  private AObject object;
  /** the values */
  private Map<Integer, Map<Integer, List<AValue>>> values;

  /**
   * Creates an instance of this class.
   * 
   * @param objectTemplate
   * @param ta
   * @param isDisabled
   */
  public ObjectPicker(ObjectTemplate objectTemplate, TemplateAttribute ta, boolean isDisabled) {
    setObjectTemplate(objectTemplate);
    setTemplateAttribute(ta);
    
    setWidth(ta.getWidth()+ta.getWidthUnit());
    getChoiceButton().setTabIndex(ta.getTabIndex());

    addStyleName(ClientUtils.CSS_OBJECT_LISTBOX);
    if (isDisabled) {
      getSelectedValue().addStyleName(ClientUtils.CSS_DISABLED);
      getChoiceButton().addStyleName(ClientUtils.CSS_DISABLED);
    }
    if (ta.isMandatory())
      getSelectedValue().addStyleName(ClientUtils.CSS_MANDATORY);
  }
  
  /** {@inheritDoc} */
  protected String getDefaultImageName() {
    return "picker.png";
  }
  
  @Override
  protected void addComponentListeners() {
    if (openTreeClickHandler == null) {
      openTreeClickHandler = new OpenTreeClickHandler();

      ToggleButton treeButton = getChoiceButton();
      treeButton.addClickHandler(openTreeClickHandler);
      TextBox box = getSelectedValue();
      box.addClickHandler(openTreeClickHandler);
    }

    getObjectsTree().addObjectListener(this);
  }

  /** {@inheritDoc} */
  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  /** {@inheritDoc} */
  @Override
  public void onChange(ObjectsTree sender, AObject oldValue) {
    getObjectsTreePanel().hide();
    getTriangleOuterPanel().hide();
    getTriangleInnerPanel().hide();
    setObject(getObjectsTree().getObject());
    fireEvent(new ChangeEvent() {});
  }

  /** {@inheritDoc} */
  @Override
  public void onCancel(ObjectsTree sender) {
    getObjectsTreePanel().hide();
    getTriangleOuterPanel().hide();
    getTriangleInnerPanel().hide();
  }

  /** {@inheritDoc} */
  @Override
  public void onLoad() {
    getSelectedValue().setTabIndex(-1);
  }

  // getters and setters
  
  /**
   * Getter for property 'objectsTreePanel'.
   * 
   * @return Value for property 'objectsTreePanel'.
   */
  protected PopupPanel getObjectsTreePanel() {
    if (objectsTreePanel == null) {
      objectsTreePanel = new PopupPanel(true, true);
      objectsTreePanel.add(getObjectsTree());
      objectsTreePanel.setStyleName(ClientUtils.CSS_CHOOSEBOX);
    }
    return objectsTreePanel;
  }

  /**
   * Getter for property 'triangleOuterPanel'.
   * 
   * @return Value for property 'triangleOuterPanel'.
   */
  protected PopupPanel getTriangleOuterPanel() {
    if (triangleOuterPanel == null) {
      triangleOuterPanel = new PopupPanel();
      triangleOuterPanel.setStyleName("triangle-outer");
    }
    return triangleOuterPanel;
  }

  /**
   * Getter for property 'triangleInnerPanel'.
   * 
   * @return Value for property 'triangleInnerPanel'.
   */
  protected PopupPanel getTriangleInnerPanel() {
    if (triangleInnerPanel == null) {
      triangleInnerPanel = new PopupPanel();
      triangleInnerPanel.setStyleName("triangle-inner");
    }
    return triangleInnerPanel;
  }

  /**
   * Getter for property 'objectsTree'.
   * 
   * @return Value for property 'objectsTree'.
   */
  protected ObjectsTree getObjectsTree() {
    if (objectsTree == null) {
      objectsTree = new ObjectsTree();
      if (getObject() != null)
        objectsTree.setObject(getObject());
    }

    return objectsTree;
  }

  
  /**
   * @return the objectView
   */
  public ObjectView getObjectView() {
    return getObjectTemplate().getObjectView();
  }

  /**
   * @return the objectTemplate
   */
  public ObjectTemplate getObjectTemplate() {
    return objectTemplate;
  }

  /**
   * @return the objectTemplate
   */
  public ApplicationModel getModel() {
    return getObjectTemplate().getModel();
  }

  /**
   * @param objectTemplate the objectTemplate to set
   */
  public void setObjectTemplate(ObjectTemplate objectTemplate) {
    this.objectTemplate = objectTemplate;
  }

  /**
   * @return the templateAttribute
   */
  public TemplateAttribute getTemplateAttribute() {
    return templateAttribute;
  }

  /**
   * @param templateAttribute the templateAttribute to set
   */
  public void setTemplateAttribute(TemplateAttribute templateAttribute) {
    this.templateAttribute = templateAttribute;
  }

  /**
   * @return the object
   */
  public AObject getObject() {
    return object;
  }

  /**
   * @param object the object to set
   */
  public void setObject(AObject object) {
    this.object = object;
  }

  /**
   * @return the values
   */
  public Map<Integer, Map<Integer, List<AValue>>> getValues() {
    return values;
  }

  /**
   * @param values the values to set
   */
  public void setValues(Map<Integer, Map<Integer, List<AValue>>> values) {
    this.values = values;
  }



  /**
   * This is an open objects tree event handler implementation.
   * 
   */
  protected class OpenTreeClickHandler implements ClickHandler {
    /** {@inheritDoc} */
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == getSelectedValue() && isChoiceButtonVisible())
        return;
      if (getModel().getObject() == null)
        return;
      
      ObjectsTree tree = getObjectsTree();

      tree.setObjectTemplate(getObjectTemplate());
      tree.setTemplateId(getTemplateAttribute().getShared1());
      tree.setObject(getObject());
      tree.setValues(getValues());

      tree.display();
      getObjectsTreePanel().show();
      getTriangleOuterPanel().show();
      getTriangleInnerPanel().show();
      
      int x = getAbsoluteLeft()+getOffsetWidth();
      int y = getAbsoluteTop()+getOffsetHeight()/2-10;
      int top = ((Window.getClientHeight() -
          getObjectsTreePanel().getOffsetHeight()) / 2);
      getObjectsTreePanel().setPopupPosition(x+10, top);

      getTriangleOuterPanel().setPopupPosition(x, y);
      getTriangleInnerPanel().setPopupPosition(x+2, y);

      getChoiceButton().setDown(false);
    }
  }
}
