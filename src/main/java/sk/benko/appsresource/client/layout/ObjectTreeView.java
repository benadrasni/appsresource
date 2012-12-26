package sk.benko.appsresource.client.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.TreeItemData;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.designer.TreeLevelRowView;
import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateTree;
import sk.benko.appsresource.client.model.TemplateTreeItem;
import sk.benko.appsresource.client.model.TemplateTreeItemLoader;
import sk.benko.appsresource.client.model.TemplateTreeLoader;
import sk.benko.appsresource.client.model.TreeLevel;
import sk.benko.appsresource.client.model.loader.ObjectLoader;
import sk.benko.appsresource.client.model.loader.RelatedObjectCountLoader;
import sk.benko.appsresource.client.model.loader.TreeLevelLoader;
import sk.benko.appsresource.client.model.loader.ValueLoader;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 *
 */
public class ObjectTreeView extends FlowPanel implements
    Model.TemplateTreeObserver, 
    Model.TemplateTreeItemObserver,
    ApplicationModel.ObjectObserver, 
    ApplicationModel.ObjectsObserver,
    ApplicationModel.ValueObserver,
    OpenHandler<TreeItem>, 
    SelectionHandler<TreeItem>, 
    ChangeHandler {
  
  private ObjectView objectView;
  private ArrayList<TemplateTreeItem> actualTreeItems;
  private TreeItem selectedItem;
  private ScrollPanel sc; 
  private DropDownBox ddbTemplate;
  
  private boolean isTreeReloading = false;

  /**
   * @param objectView
   */
  public ObjectTreeView(final ObjectView objectView) {
    setObjectView(objectView);
    getElement().setId("object-tree");
    this.addDomHandler(this, ChangeEvent.getType());

    add(getDdbTemplate());
    
    this.sc = new ScrollPanel(getObjectView().getObjectTree());
    this.sc.setSize("184px", Window.getClientHeight()-245+"px");
    add(sc);

    getObjectView().getObjectTree().addOpenHandler(this);
    getObjectView().getObjectTree().addSelectionHandler(this);
    
    getModel().addDataObserver(this);
    getModel().addObjectObserver(this);
    getModel().addValueObserver(this);
  }

  public void initialize() {
    setSelectedItem(null);
    getObjectView().getObjectTree().clear();
    
    setActualTreeItems(getModel().getTreeItems().get(getAppt().getTId()));
    if (getActualTreeItems() != null) {
      setItems();
      loadFirstLevel();
    } else {
      TemplateTreeLoader ttl = new TemplateTreeLoader(getModel(),
          getAppt().getTId());
      ttl.start();
    }
  }

  @Override
  public void onObjectCreated(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    isTreeReloading = true;
    loadFirstLevel();
  }

  @Override
  public void onObjectDeleted(AObject object) {
    if (getSelectedItem() != null 
        && ((AObject)getSelectedItem().getUserObject()).getId() == object.getId()) {
      TreeItem helper = getSelectedItem();
      TreeItem helperParent = getSelectedItem().getParentItem();
      helper.remove();
      
      while (helperParent != null && helperParent.getChildCount() == 0) {
        helper = helperParent;
        helperParent = helper.getParentItem();
        helper.remove();
      }

      if (helperParent != null) 
        setSelectedItem(helperParent.getChild(0));
      else 
        if (getObjectView().getObjectTree().getItemCount() > 0) 
          setSelectedItem(getObjectView().getObjectTree().getItem(0));
        else
          setSelectedItem(null);
      
      getObjectView().getObjectTree().setSelectedItem(getSelectedItem());
    }
  }

  @Override
  public void onValuesLoaded(AObject object, Map<Integer, Map<Integer, List<AValue>>> values) {
    if (getActualTreeItems() != null && 
        (getSelectedItem() == null 
        || ((AObject)getSelectedItem().getUserObject()).getId() != object.getId()))
      loadFirstLevel();
  }
  
  @Override
  public void onValueUpdated(AValue value) {
    if (value != null && isTreeAttribute(value.getOaId())) {
      isTreeReloading = true;
      loadFirstLevel();
    }
  }

  @Override
  public void onTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items) {
    TreeItemData tid = (TreeItemData)ti.getUserObject();
    if (tid.getPath().size() == 0)
      getObjectView().getObjectTree().clear();

    ti.removeItems();
    tid.setLoaded(true);

    AValue value = null;
    String derivedValue = null;
    if (getModel().getValues() != null) {
      List<AValue> values = getModel().getValues().get(ta.getOaId());
      if (values != null && values.size() > 0) {
        value = values.get(0);
        if (ta.isDerived()) {
          if (ta.getDef().equals(TemplateAttribute.FUCTION_YEAR))
            derivedValue = DateTimeFormat.getFormat("yyyy").format(value.getValueDate());
          else if (ta.getDef().startsWith(TemplateAttribute.FUCTION_SUBSTRING)) {
            int pos = ta.getDef().indexOf(',');
            int pos1 = ta.getDef().indexOf(',',pos+1); 
            int beginIndex = Integer.parseInt(ta.getDef().substring(pos+1, pos1))-1;
            int endIndex = beginIndex+Integer.parseInt(ta.getDef().substring(pos1+1, ta.getDef().length()-1));
            derivedValue = value.getValueString().substring(beginIndex, endIndex);
          }
        }
      }
    }
    
    for (TreeLevel item : items) {
      // create tree item
      TreeLevelRowView treeItem = new TreeLevelRowView(item);
      treeItem.setUserObject(new TreeItemData(tid.getPath(), item));
      
      // fake item to show tree arrows
      treeItem.addItem(new Label());
      if (tid.getPath().size() == 0) 
        getObjectView().getObjectTree().addItem(treeItem);
      else 
        ti.addItem(treeItem);
      
      if (items.size() == 1 
          || (getModel().getObject() != null && value == null && item.isEmpty()) 
          || (value != null && value.equals(item, ta.isDerived(), derivedValue)))
        treeItem.setState(true);
    }
    
    if (items.size() == 0) {
      Label noobjs = new Label(Main.constants.noObjects()); 
      noobjs.setStyleName(ClientUtils.CSS_ITALIC);
      if (tid.getPath().size() == 0) 
        getObjectView().getObjectTree().addItem(noobjs);
      else 
        ti.addItem(noobjs);
    }
  }

  @Override
  public void onObjectsLoaded(TreeItem ti, List<AObject> objects) {
    TreeItemData tid = (TreeItemData)ti.getUserObject();
    if (tid.getPath().size() == 0)
      getObjectView().getObjectTree().clear();

    ti.removeItems();
    tid.setLoaded(true);

    for (AObject object : objects) {
      // create tree item
      ObjectRowView row = new ObjectRowView(object, "object-row");
      row.generateWidgetShort();
      TreeItem treeItem = new TreeItem(row);
      treeItem.setUserObject(object);
      treeItem.setStyleName("object-tree-row");
      if (tid.getPath().size() == 0) 
        getObjectView().getObjectTree().addItem(treeItem);
      else 
        ti.addItem(treeItem);
      
      if (getModel().getObject() != null 
          && getModel().getObject().getId() == object.getId()) {
        setSelectedItem(treeItem);
        getObjectView().getObjectTree().setSelectedItem(treeItem);
        getSelectedItem().getElement().scrollIntoView();
      }
    }
    
    if (objects.size() == 0) {
      Label noobjs = new Label(Main.constants.noObjects()); 
      noobjs.setStyleName(ClientUtils.CSS_ITALIC);
      if (tid.getPath().size() == 0) 
        getObjectView().getObjectTree().addItem(noobjs);
      else 
        ti.addItem(noobjs);
    }
  }

  @Override
  public void onTemplateTreesLoaded(ArrayList<TemplateTree> tts) {
    setItems();
    for (TemplateTree tt : tts) {
      if (getActualTreeItems() == null && tt.isDefault()) { 
        TemplateTreeItemLoader ttil = new TemplateTreeItemLoader(getModel(), tt);
        ttil.start();
      }
    }
  }

  @Override
  public void onTemplateTreeItemsLoaded(TemplateTree tt, ArrayList<TemplateTreeItem> ttis) {
    setActualTreeItems(ttis);
     
    loadFirstLevel();
  }  
  
  @Override
  public void onOpen(OpenEvent<TreeItem> event) {
    TreeItem item = event.getTarget();
    TreeItemData tid = (TreeItemData)item.getUserObject();
    
    if (!tid.isLoaded()) {
      TemplateAttribute ta = getActualTreeItems().get(tid.getPath().size()).getTa();
      String key = getActualTreeItems().get(0).getTtId()  
          + TreeItemData.KEY_SEPARATOR + tid.getKey();
      if (tid.getPath().size() < getActualTreeItems().size()-1) {
        List<TreeLevel> treeLevels = getModel().getTreeLevels().get(key);
        if (!isTreeReloading && treeLevels != null)
          onTreeLevelLoaded(item, ta, treeLevels);
        else {
          TreeLevelLoader tll = new TreeLevelLoader(getModel(), 
              Main.language, getAppt().getTId(), item, key, ta);
          tll.start();
        }
      } else {
        List<AObject> treeObjects = getModel().getTreeObjects().get(key);
        if (!isTreeReloading && treeObjects != null)
          onObjectsLoaded(item, treeObjects);
        else {
          ObjectLoader ol = new ObjectLoader(getModel(), 
              Main.language, getAppt().getTId(), item, key, ta);
          ol.start();
        }
        isTreeReloading = false;
      }
    }
  }

  @Override
  public void onSelection(SelectionEvent<TreeItem> event) {
    TreeItem item = event.getSelectedItem();
    if (item.getUserObject() instanceof AObject) {
      if (getSelectedItem() != null && getSelectedItem() != item) 
        getSelectedItem().removeStyleName(ClientUtils.CSS_NAVITEM_SELECTED);
      
      setSelectedItem(item);
      getSelectedItem().addStyleName(ClientUtils.CSS_NAVITEM_SELECTED);

      AObject obj = (AObject)item.getUserObject();
      if (getModel().getObject() == null 
          || obj.getId() != getModel().getObject().getId()) {
        getModel().setObject(obj);
        ValueLoader vl = new ValueLoader(getModel());
        vl.start();
        
        if (getModel().getTemplateRelation() != null) {
          RelatedObjectCountLoader rocl = new RelatedObjectCountLoader(
              getModel(), getModel().getTemplateRelation().getOrId(), 
              getModel().getTemplateRelation().getT2());
          rocl.start();
        }
      }

      ObjectToolbarView.getObjectStatus().setText(ClientUtils
          .getTimeFrame(obj.getLastUpdatedAt(), obj.getUser().getShortName()));
    }
  }

  
  @Override
  public void onObjectsImported(int count) {
  }

  @Override
  public void onObjectsRemoved(int count) {
  }

  @Override
  public void onLoad() {
    if (getSelectedItem() != null)
      getSelectedItem().getElement().scrollIntoView();    
  }

  @Override
  public void onChange(ChangeEvent event) {
    TemplateTreeItemLoader ttil = new TemplateTreeItemLoader(getModel(), 
        (TemplateTree)getDdbTemplate().getSelection().getUserObject());
    ttil.start();
  }

  // getters and setters

  public ApplicationModel getModel() {
    return getObjectView().getModel();
  }

  public ApplicationTemplate getAppt() {
    return getModel().getAppt();
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
   * @return the actualTreeItems
   */
  public ArrayList<TemplateTreeItem> getActualTreeItems() {
    return actualTreeItems;
  }

  /**
   * @param actualTreeItems the actualTreeItems to set
   */
  public void setActualTreeItems(ArrayList<TemplateTreeItem> actualTreeItems) {
    this.actualTreeItems = actualTreeItems;
  }

  /**
   * @return the selectedItem
   */
  public TreeItem getSelectedItem() {
    return selectedItem;
  }

  /**
   * @param selectedItem the selectedItem to set
   */
  public void setSelectedItem(TreeItem selectedItem) {
    this.selectedItem = selectedItem;
  }

  /**
   * Getter for property 'ddbTemplate'.
   * 
   * @return Value for property 'ddbTemplate'.
   */
  protected DropDownBox getDdbTemplate() {
    if (ddbTemplate == null) {
      ddbTemplate = new DropDownBox(this, Main.constants.listView(), 
          CSSConstants.SUFFIX_LIST);
      ddbTemplate.setVisible(false);
    }
    return ddbTemplate;
  }
  
  // private methods

  private void loadFirstLevel() {
    TreeItem ti = new TreeItem();
    ti.setUserObject(new TreeItemData());

    String key = getActualTreeItems().get(0).getTtId() + TreeItemData.KEY_SEPARATOR;
    if (getActualTreeItems().size() > 1) {
      List<TreeLevel> treeLevels = getModel().getTreeLevels().get(key);
      if (!isTreeReloading && treeLevels != null)
        onTreeLevelLoaded(ti, 
            getActualTreeItems().get(0).getTa(), treeLevels);
      else {  
        TreeLevelLoader tll = new TreeLevelLoader(getModel(), 
            Main.language, getAppt().getTId(), ti, key, 
            getActualTreeItems().get(0).getTa());
        tll.start();
      }
    } else {
      List<AObject> treeObjects = getModel().getTreeObjects().get(key);
      if (!isTreeReloading && treeObjects != null)
        onObjectsLoaded(ti, treeObjects);
      else {
        ObjectLoader ol = new ObjectLoader(getModel(), Main.language, 
            getAppt().getTId(), ti, key,
            getActualTreeItems().get(0).getTa());
        ol.start();
      }
      isTreeReloading = false;
    }
  }

  private void setItems() {
    ArrayList<TemplateTree> tts = getModel().getTrees().get(getAppt().getTId());
    if (tts == null) return;
    
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>();
    DropDownObject selection = null;
    for (TemplateTree tt : tts) {
      items.add(new DropDownObjectImpl(tt.getId(), tt.getName(), tt));
      if (getActualTreeItems() != null && 
          getActualTreeItems().get(0).getTtId() == tt.getId())
        selection = items.get(items.size()-1);
    }
        
    getDdbTemplate().setItems(items);
    if (selection != null) getDdbTemplate().setSelection(selection);
    getDdbTemplate().setVisible(tts.size() > 1);
  }
  
  private boolean isTreeAttribute(int oaId) {
    for (TemplateTreeItem tti: getActualTreeItems())
      if (tti.getTa().getOaId() == oaId)
        return true;
    return false;
  }
}
