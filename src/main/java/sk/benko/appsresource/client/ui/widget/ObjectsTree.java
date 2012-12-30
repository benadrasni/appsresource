package sk.benko.appsresource.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Tree.Resources;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.TreeItemData;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.designer.TreeLevelRowView;
import sk.benko.appsresource.client.designer.WidthUnitListBox;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.ObjectRowView;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.ChooseTreeLevelLoader;
import sk.benko.appsresource.client.model.loader.ObjectLoader;
import sk.benko.appsresource.client.ui.ObjectListener;

import java.util.*;

/**
 * A generic widget to display designer dialog.
 */
public class ObjectsTree extends SimplePanel implements
    ApplicationModel.ChooseObjectObserver,
    OpenHandler<TreeItem>,
    SelectionHandler<TreeItem> {

  /**
   * layout absolute panel
   */
  private AbsolutePanel layout;
  /**
   * header panel
   */
  private FlowPanel header;
  /**
   * close button
   */
  private Label x;
  /**
   * body panel
   */
  private FlowPanel body;
  /**
   * object tree
   */
  private Tree objectTree;
  /**
   * scroll panel
   */
  private ScrollPanel sc;
  private int width;
  private int height;
  private ObjectTemplate objectTemplate;
  private AObject object;
  private Map<Integer, Map<Integer, List<AValue>>> values;
  private int templateId;
  private List<TemplateTreeItem> actualTreeItems;
  private TreeItem selectedItem;
  /**
   * a set of calendar listeners
   */
  private Set<ObjectListener<ObjectsTree>> objectListeners;

  private void initializeTree() {

    setActualTreeItems(getModel().getTreeItems().get(getTemplateId()));
    if (getActualTreeItems() != null)
      loadLevel();
    else {
      TemplateTreeLoader ttl = new TemplateTreeLoader(getModel(),
          getTemplateId(), true);
      ttl.start();
    }
  }

  private void loadLevel() {
    TreeItem ti = new TreeItem();
    ti.setUserObject(new TreeItemData());
    String key = getActualTreeItems().get(0).getTtId() + TreeItemData.KEY_SEPARATOR;
    if (getActualTreeItems().size() > 1) {
      List<TreeLevel> treeLevels = getModel().getTreeLevels().get(key);
      if (treeLevels != null)
        onChooseTreeLevelLoaded(ti,
            getActualTreeItems().get(0).getTa(), treeLevels);
      else {
        ChooseTreeLevelLoader tll = new ChooseTreeLevelLoader(getModel(),
            Main.language,
            getObjectTemplate().getTemplate().getId(),
            getValues().get(getModel().getObject().getId()),
            getTemplateId(), ti, key,
            getActualTreeItems().get(0).getTa());
        tll.start();
      }
    } else {
      List<AObject> treeObjects = getModel().getTreeObjects().get(key);
      if (treeObjects != null)
        onChooseObjectsLoaded(ti, treeObjects);
      else {
        ObjectLoader ol = new ObjectLoader(getModel(),
            Main.language, getTemplateId(), ti, key,
            getActualTreeItems().get(0).getTa(), true);
        ol.start();
      }
    }
  }

  /**
   * This method prepares the layout.
   */
  protected void prepareLayout() {
    getLayout().add(getHeader());
    getLayout().add(getX());
    getLayout().add(getBody());

    initializeTree();
  }

  /**
   * This method clean the layout.
   */
  protected void cleanLayout() {
    if (layout != null) {
      remove(getLayout());
      layout = null;
      header = null;
      body = null;
      x = null;
      sc = null;
      objectTree = null;
      selectedItem = null;
    }
  }

  public void display() {
    cleanLayout();
    prepareLayout();
    add(getLayout());
  }

  /**
   * This method adds a object listener.
   *
   * @param listener a listener instance.
   */
  public void addObjectListener(ObjectListener<ObjectsTree> listener) {
    getObjectListeners().add(listener);
  }

  /**
   * This method removes a object listener.
   *
   * @param listener a listener instance.
   */
  public void removeObjectListener(ObjectListener<ObjectsTree> listener) {
    getObjectListeners().remove(listener);
  }

  private void nodefined() {
    AObject object = new AObject(0, 0, 0, Main.constants.noDefined());
    ObjectRowView row = new ObjectRowView(object, "object-row");
    row.generateWidgetShort();
    TreeItem treeItem = new TreeItem(row);
    treeItem.setUserObject(object);
    treeItem.setStyleName("object-tree-row");
    treeItem.addStyleName(ClientUtils.CSS_ITALIC);
    getObjectTree().addItem(treeItem);
  }

  @Override
  public void onChooseObjectsLoaded(TreeItem ti, List<AObject> objects) {
    TreeItemData tid = (TreeItemData) ti.getUserObject();
    if (tid.getPath().size() == 0)
      nodefined();

    ti.removeItems();
    for (AObject object : objects) {
      // create tree item
      ObjectRowView row = new ObjectRowView(object, "object-row");
      row.generateWidgetShort();
      TreeItem treeItem = new TreeItem(row);
      treeItem.setUserObject(object);
      treeItem.setStyleName("object-tree-row");
      if (tid.getPath().size() == 0)
        getObjectTree().addItem(treeItem);
      else
        ti.addItem(treeItem);

      if (values != null && getObject() != null
          && getObject().getId() == object.getId()) {
        setSelectedItem(treeItem);
        getObjectTree().setSelectedItem(treeItem);
        getSelectedItem().getElement().scrollIntoView();
      }
    }
  }

  @Override
  public void onChooseTemplateTreesLoaded(List<TemplateTree> tts) {
    for (TemplateTree tt : tts) {
      if (getActualTreeItems() == null && tt.isDefault()) {
        TemplateTreeItemLoader ttil = new TemplateTreeItemLoader(getModel(), tt, true);
        ttil.start();
      }
    }
  }

  @Override
  public void onChooseTemplateTreeItemsLoaded(TemplateTree tt,
                                              List<TemplateTreeItem> ttis) {
    setActualTreeItems(ttis);

    loadLevel();
  }

  @Override
  public void onChooseTreeLevelLoaded(TreeItem ti, TemplateAttribute ta, List<TreeLevel> items) {
    TreeItemData tid = (TreeItemData) ti.getUserObject();
    if (tid.getPath().size() == 0)
      nodefined();

    AValue value = null;
    String derivedValue = null;
    if (getValues() != null && getObject() != null && getObject().getId() > 0) {
      List<AValue> values = getValues().get(getObject().getId()).get(ta.getOaId());
      if (values != null && values.size() > 0) {
        value = values.get(0);
        if (ta.isDerived()) {
          if (ta.getDef().equals(TemplateAttribute.FUCTION_YEAR))
            derivedValue = DateTimeFormat.getFormat("yyyy").format(value.getValueDate());
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
        getObjectTree().addItem(treeItem);
      else {
        if (ti.getChild(0).getWidget() instanceof Label)
          ti.removeItems();
        ti.addItem(treeItem);
      }

      if (items.size() == 1
          || (getObject() != null && value == null && item.isEmpty())
          || (value != null && value.equals(item, ta.isDerived(), derivedValue)))
        treeItem.setState(true);
    }
  }

  @Override
  public void onOpen(OpenEvent<TreeItem> event) {
    TreeItem item = event.getTarget();
    TreeItemData tid = (TreeItemData) item.getUserObject();

    if (!tid.isLoaded()) {
      TemplateAttribute ta = getActualTreeItems().get(tid.getPath().size()).getTa();
      String key = getActualTreeItems().get(0).getTtId()
          + TreeItemData.KEY_SEPARATOR + tid.getKey();
      if (tid.getPath().size() < getActualTreeItems().size() - 1) {
        List<TreeLevel> treeLevels = getModel().getTreeLevels().get(key);
        if (treeLevels != null)
          onChooseTreeLevelLoaded(item, ta, treeLevels);
        else {
          ChooseTreeLevelLoader tll = new ChooseTreeLevelLoader(getModel(),
              Main.language,
              getObjectTemplate().getTemplate().getId(),
              getValues().get(getModel().getObject().getId()),
              getTemplateId(), item, key, ta);
          tll.start();
        }
      } else {
        List<AObject> treeObjects = getModel().getTreeObjects().get(key);
        if (treeObjects != null)
          onChooseObjectsLoaded(item, treeObjects);
        else {
          ObjectLoader ol = new ObjectLoader(getModel(),
              Main.language, getTemplateId(), item, key, ta, true);
          ol.start();
        }
      }
      tid.setLoaded(true);
    }
  }

  @Override
  public void onSelection(SelectionEvent<TreeItem> event) {
    TreeItem item = event.getSelectedItem();
    if (item.getUserObject() instanceof AObject)
      if (getObject() == null
          || getObject().getId() != ((AObject) item.getUserObject()).getId()) {

        AObject oldValue = getObject();
        setObject((AObject) item.getUserObject());
        setSelectedItem(item);

        for (ObjectListener<ObjectsTree> objectListener : getObjectListeners())
          objectListener.onChange(this, oldValue);
      }
  }

  // getters and setters

  /**
   * Getter for property 'layout'.
   *
   * @return Value for property 'layout'.
   */
  protected AbsolutePanel getLayout() {
    if (layout == null) {
      layout = new AbsolutePanel();
      layout.setStyleName(ClientUtils.CSS_CHOOSEBOX);
    }

    return layout;
  }

  /**
   * Getter for property 'header'.
   *
   * @return Value for property 'header'.
   */
  protected FlowPanel getHeader() {
    if (header == null) {
      header = new FlowPanel();
      header.setStyleName(ClientUtils.CSS_CHOOSEBOX_HEADER);
      header.add(new Label(Main.constants.chooseItem()));
    }

    return header;
  }

  /**
   * Getter for property 'x'.
   *
   * @return Value for property 'x'.
   */
  protected Label getX() {
    if (x == null) {
      x = new Label(ClientUtils.CLOSE_CHAR);
      x.setStyleName(ClientUtils.CSS_CHOOSEBOX_X);
      x.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          for (ObjectListener<ObjectsTree> objectListener : getObjectListeners())
            objectListener.onCancel(ObjectsTree.this);
        }
      });
    }

    return x;
  }

  /**
   * Getter for property 'body'.
   *
   * @return Value for property 'body'.
   */
  protected FlowPanel getBody() {
    if (body == null) {
      body = new FlowPanel();
      body.setHeight(getHeight() + WidthUnitListBox.WIDTH_UNITPX);
      body.setWidth(getWidth() + WidthUnitListBox.WIDTH_UNITPX);
      body.setStyleName(ClientUtils.CSS_CHOOSEBOX_BODY);
      body.add(getSc());
    }

    return body;
  }

  /**
   * Getter for property 'objectTree'.
   *
   * @return Value for property 'objectTree'.
   */
  protected Tree getObjectTree() {
    if (objectTree == null) {
      objectTree = new Tree((Resources) GWT.create(TreeResource.class), false);
      objectTree.addOpenHandler(this);
      objectTree.addSelectionHandler(this);
    }

    return objectTree;
  }

  /**
   * Getter for property 'sc'.
   *
   * @return Value for property 'sc'.
   */
  protected ScrollPanel getSc() {
    if (sc == null) {
      sc = new ScrollPanel(getObjectTree());
      sc.setSize(getWidth() - 10 + WidthUnitListBox.WIDTH_UNITPX,
          getHeight() - 10 + WidthUnitListBox.WIDTH_UNITPX);
    }

    return sc;
  }

  private int getWidth() {
    if (width == 0)
      width = Window.getClientWidth() / 6;
    return width;
  }

  private int getHeight() {
    if (height == 0)
      height = Window.getClientHeight() * 2 / 3;
    return height;
  }

  public ApplicationModel getModel() {
    return getObjectTemplate().getModel();
  }

  /**
   * @return the objectTemplate
   */
  public ObjectTemplate getObjectTemplate() {
    return objectTemplate;
  }

  /**
   * @param objectTemplate the objectTemplate to set
   */
  public void setObjectTemplate(ObjectTemplate objectTemplate) {
    this.objectTemplate = objectTemplate;
  }

  /**
   * @return the templateId
   */
  public int getTemplateId() {
    return templateId;
  }

  /**
   * @param templateId the templateId to set
   */
  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }

  /**
   * @return the actualTreeItems
   */
  public List<TemplateTreeItem> getActualTreeItems() {
    return actualTreeItems;
  }

  /**
   * @param actualTreeItems the actualTreeItems to set
   */
  public void setActualTreeItems(List<TemplateTreeItem> actualTreeItems) {
    this.actualTreeItems = actualTreeItems;
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
   * @return the selectedItem
   */
  public TreeItem getSelectedItem() {
    return selectedItem;
  }

  /**
   * @param selectedItem the selectedItem to set
   */
  public void setSelectedItem(TreeItem selectedItem) {
    if (this.selectedItem != selectedItem) {
      if (this.selectedItem != null)
        this.selectedItem.removeStyleName(ClientUtils.CSS_NAVITEM_SELECTED);
      this.selectedItem = selectedItem;
      this.selectedItem.addStyleName(ClientUtils.CSS_NAVITEM_SELECTED);
    }
  }

  /**
   * Getter for property 'objectListeners'.
   *
   * @return Value for property 'objectListeners'.
   */
  protected Set<ObjectListener<ObjectsTree>> getObjectListeners() {
    if (objectListeners == null)
      objectListeners = new HashSet<ObjectListener<ObjectsTree>>();
    return objectListeners;
  }

  public void onLoad() {
    getModel().addDataObserver(this);
    if (getSelectedItem() != null)
      getSelectedItem().getElement().scrollIntoView();
  }

  public void onUnload() {
    getModel().removeDataObserver(this);
    for (ObjectListener<ObjectsTree> objectListener : getObjectListeners())
      objectListener.onCancel(this);
  }
}
