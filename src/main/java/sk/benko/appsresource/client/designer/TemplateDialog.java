package sk.benko.appsresource.client.designer;

import java.util.*;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.designer.model.ObjectAttributeLoader;
import sk.benko.appsresource.client.dnd.TemplateDialogDragController;
import sk.benko.appsresource.client.dnd.TemplateDialogListDropController;
import sk.benko.appsresource.client.dnd.TemplateDialogTreeDropController;
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.ObjectAttribute;
import sk.benko.appsresource.client.model.ObjectType;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.model.TemplateAttributeComparator;
import sk.benko.appsresource.client.model.TemplateList;
import sk.benko.appsresource.client.model.TemplateListItem;
import sk.benko.appsresource.client.model.TemplateListItemLoader;
import sk.benko.appsresource.client.model.TemplateListLoader;
import sk.benko.appsresource.client.model.TemplateRelation;
import sk.benko.appsresource.client.model.TemplateTree;
import sk.benko.appsresource.client.model.TemplateTreeItem;
import sk.benko.appsresource.client.model.TemplateTreeItemLoader;
import sk.benko.appsresource.client.model.TemplateTreeLoader;
import sk.benko.appsresource.client.model.loader.ObjectTypeLoader;
import sk.benko.appsresource.client.model.loader.TemplateAttributeLoader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Tree.Resources;
import com.google.gwt.user.client.ui.TreeItem;
/**
 *
 */
public class TemplateDialog extends DesignerDialog implements 
    ApplicationModel.TemplateTreeItemObserver,
    ApplicationModel.TemplateListItemObserver,
    DesignerModel.ObjectTypeObserver,
    DesignerModel.ObjectAttributeObserver,
    Model.TemplateTreeObserver,
    Model.TemplateListObserver, 
    Model.TemplateAttributeObserver {
  
  final static FlowPanel attrMsgTree = initializeHintMessage();
  final static FlowPanel attrMsgList = initializeHintMessage();
  
  private ObjectTemplate objectTemplate;

  private DropDownBox ddbObjectType;
  private DropDownBox ddbObjectAttribute;
  private DropDownBox ddbTemplateAttribute;
  
  private Label lblTreeName;
  private TextBox tbTreeName;
  private Label lblTreeDesc;
  private TextBox tbTreeDesc;
  private Label lblTreeSort;

  private Label lblListName;
  private TextBox tbListName;
  private Label lblListDesc;
  private TextBox tbListDesc;

  Tree menu = new Tree((Resources) GWT.create(TreeResource.class), false);
    
  private FlexTable widgetTemplate;
  private FlexTable widgetNewTree;
  private FlexTable widgetNewList;
  
  /* actual state */
  private Tree attributeTree;
  private TemplateTree templateTree;
  private Tree attributeList;
  private TemplateList templateList;
  private NavigationLabelView menuItem;

  private FlowPanel treePanel;
  private FlowPanel listPanel;
  private FlexTable ddTable;
  private Tree attributePanel;

  TemplateDialogDragController attributeDragController;
  TemplateDialogTreeDropController treeDropController;
  TemplateDialogListDropController listDropController;
  
  private HashMap<TemplateTree, ArrayList<TemplateTreeItem>> ttrees;
  private HashMap<TemplateList, ArrayList<TemplateListItem>> tlists;

  private HashMap<Integer, Tree> trees;
  private HashMap<Integer, Tree> lists;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public TemplateDialog(final DesignerModel model, Template t) {
    super(model, t);
    getModel().setTemplate(t);
    getModel().addObjectTypeObserver(this);
    getModel().addObjectAttributeObserver(this);
    getModel().addTemplateAttributeObserver(this);
    getModel().addDataObserver(this);
    
    attributeDragController = new TemplateDialogDragController(getMain());
    treeDropController = new TemplateDialogTreeDropController(this);
    listDropController = new TemplateDialogListDropController(this);

    getHeader().add(new Label((getModel().getTemplate() == null ? 
        Main.constants.newItem() + " " : "") + Main.constants.template()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.template(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        model.notifyDialogNavigationItemClicked(event.getRelativeElement());
        getBodyRight().clear();
        getBodyRight().add(getWidgetTemplate());
      }
    }); 
    menu1.addStyleName("dialog-box-navigation-item dialog-box-navigation-item-selected");
    menu.addItem(menu1);

    if (getModel().getTemplate() != null && getModel().getTemplate().getOtId() > 0) {
      NavigationLabelView menu2 = new NavigationLabelView(
          model, Main.constants.templateTrees(), new ClickHandler() {
        public void onClick(ClickEvent event) {
          model.notifyDialogNavigationItemClicked(event.getRelativeElement());
          getBodyRight().clear();
          getBodyRight().add(getWidgetNewTree());
        }
      });
      menu.addItem(menu2);

      NavigationLabelView menu3 = new NavigationLabelView(
          model, Main.constants.templateLists(), new ClickHandler() {
        public void onClick(ClickEvent event) {
          model.notifyDialogNavigationItemClicked(event.getRelativeElement());
          getBodyRight().clear();
          getBodyRight().add(getWidgetNewList());
        }
      });
      menu.addItem(menu3);

      NavigationLabelView menu4 = new NavigationLabelView(
          model, Main.constants.templateView(), new ClickHandler() {
        public void onClick(ClickEvent event) {
          model.notifyDialogNavigationItemClicked(event.getRelativeElement());
          getBodyRight().clear();
          getBodyRight().add(getObjectTemplate());
        }
      });
      menu.addItem(menu4);
    }

    getBodyLeft().add(menu);
    getBodyRight().add(getWidgetTemplate());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getModel().getTemplate() == null)
                getModel().setTemplate(new Template(getTbName().getText()));
              fill(getModel().getTemplate());
  
              model.createOrUpdateTemplate(getModel().getTemplate(), 
                  getTtrees(), getTlists());
              close();
            }
        }, ClickEvent.getType());
    getBOk().getElement().setInnerText(getModel().getTemplate() == null ? 
        Main.constants.create() : Main.constants.save());
    
    if (getModel().getTemplate() != null) {
      if (getModel().getTrees().get(getModel().getTemplate().getId()) == null) {
        TemplateTreeLoader ttl = new TemplateTreeLoader(getModel(), 
            getModel().getTemplate().getId());
        ttl.start();
      } else
        onTemplateTreesLoaded(getModel().getTrees().get(getModel().getTemplate().getId()));

      if (getModel().getLists().get(getModel().getTemplate().getId()) == null) {
        TemplateListLoader ttl = new TemplateListLoader(toString(), getModel(), 
            getModel().getTemplate().getId());
        ttl.start();
      } else
        onTemplateListsLoaded(toString(), getModel().getTemplate().getId(), 
            getModel().getLists().get(getModel().getTemplate().getId()));
    }
    model.getStatusObserver().onTaskFinished();
  }

  private static FlowPanel initializeHintMessage() {
    FlowPanel apptMsg = new FlowPanel();
    apptMsg.setStyleName("hint-frame");
    Label lblMessage = new Label(Main.messages.dragAttributes());
    lblMessage.setStyleName("hint-message");
    apptMsg.add(lblMessage);
    return apptMsg;
  }
  
  private void initializeTree() {
    if (listDropController != null)
      attributeDragController.unregisterDropController(listDropController);
    attributeDragController.registerDropController(treeDropController);
    getDDTable().setWidget(0, 0, getTreePanel());
  }

  private void initializeList() {
    if (treeDropController != null)
      attributeDragController.unregisterDropController(treeDropController);
    attributeDragController.registerDropController(listDropController);
    getDDTable().setWidget(0, 0, getListPanel());
  }

  private NavigationLabelView createMenuItem(final TemplateTree tt) {
    
    final NavigationLabelView menuTree = new NavigationLabelView(
        getModel(), tt.getName(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        assert getModel().getTemplate() != null;

        getModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
        // set actual state
        setAttributeTree(getTrees().get(tt.getId()));
        setTemplateTree(tt);
        setMenuItem((NavigationLabelView)event.getSource());

        getBodyRight().clear();
        getBodyRight().add(getTreeHeaderTable());

        if (getModel().getTreeItems().get(tt.getId()) == null) {
          TemplateTreeItemLoader ttil = new TemplateTreeItemLoader(getModel(), tt);
          ttil.start();
        } else
          onTemplateTreeItemsLoaded(tt, 
              getModel().getTreeItems().get(tt.getId()));
      }
    });
    menuTree.setTitle(tt.getDesc());
    
    final RadioButton rbDefault = new RadioButton("defaultTree", "");
    rbDefault.setValue(tt.isDefault());
    rbDefault.addClickHandler(new ClickHandler() {
      
      @Override
      public void onClick(ClickEvent event) {
        for (TemplateTree itt : ttrees.keySet()) {
          itt.setFlags(ClientUtils.unsetFlag(TemplateTree.FLAG_DEFAULT, itt.getFlags()));
        }
        tt.setFlags(ClientUtils.setFlag(TemplateTree.FLAG_DEFAULT, tt.getFlags()));
      }
    });
    
    FlexTable menuItem = new FlexTable();
    menuItem.setWidget(0, 0, menuTree);
    menuItem.setWidget(0, 1, rbDefault);
    menuItem.getCellFormatter().setStyleName(0, 1, "tree-row-x");
    menuItem.setWidth("100%");
    
    menu.getItem(1).addItem(menuItem);
    menu.getItem(1).setState(true);
    
    return menuTree;
  }

  private NavigationLabelView createMenuItem(final TemplateList tl) {
    
    final NavigationLabelView menuList = new NavigationLabelView(
        getModel(), tl.getName(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        assert getModel().getTemplate() != null;

        getModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
        // set actual state
        setAttributeList(getLists().get(tl.getId()));
        setTemplateList(tl);
        setMenuItem((NavigationLabelView)event.getSource());

        getBodyRight().clear();
        getBodyRight().add(getListHeaderTable());

        if (getModel().getListItems().get(tl.getId()) == null) {
          TemplateListItemLoader tlil = new TemplateListItemLoader(getModel(), tl);
          tlil.start();
        } else
          onTemplateListItemsLoaded(tl, getModel().getListItems().get(tl.getId()));
      }
    });
    menuList.setTitle(tl.getDesc());
    
    final RadioButton rbDefault = new RadioButton("defaultList", "");
    rbDefault.setValue(tl.isDefault());
    rbDefault.addClickHandler(new ClickHandler() {
      
      @Override
      public void onClick(ClickEvent event) {
        for (Iterator<TemplateList> iterator = tlists.keySet().iterator(); iterator.hasNext();) {
          TemplateList itl = iterator.next();
          itl.setFlags(ClientUtils.unsetFlag(TemplateList.FLAG_DEFAULT, itl.getFlags()));
        }
        tl.setFlags(ClientUtils.setFlag(TemplateTree.FLAG_DEFAULT, tl.getFlags()));
      }
    });
    
    FlexTable menuItem = new FlexTable();
    menuItem.setWidget(0, 0, menuList);
    menuItem.setWidget(0, 1, rbDefault);
    menuItem.getCellFormatter().setStyleName(0, 1, "tree-row-x");
    menuItem.setWidth("100%");
    
    menu.getItem(2).addItem(menuItem);
    menu.getItem(2).setState(true);
    
    return menuList;
  }

  private void setVisibility(int taId, boolean b) {
    for (int i = 0; i < getAttributePanel().getItemCount(); i++) {
      TreeItem ti = getAttributePanel().getItem(i);
      TemplateAttribute ta = (TemplateAttribute)ti.getUserObject();
      if (ta.getId() == taId) {
        ti.setVisible(b);
        break;
      }
    }
  }

  private void synchronize(Tree tree) {
    for (int i = 0; i < getAttributePanel().getItemCount(); i++) {
      getAttributePanel().getItem(i).setVisible(true);
    }
    for (int i = 0; i < tree.getItemCount(); i++) {
      TreeItem ti = tree.getItem(i);
      TemplateAttribute ta = (TemplateAttribute)ti.getUserObject();
      setVisibility(ta.getId(), false);
      while (ti.getChildCount() > 0) {
        ti = ti.getChild(0);
        ta = (TemplateAttribute)ti.getUserObject();
        setVisibility(ta.getId(), false);
      }
    }
  }

  private void deleteItemTree(TemplateAttribute ta) {
    ArrayList<TemplateTreeItem> ttis = getTtrees().get(getTemplateTree());
    for (int i = 0; i < ttis.size(); i++) {
      if (ttis.get(i).getTaId() == ta.getId()) {
        ttis.remove(i);
        break;
      }
    }
    
    TreeItem ti = attributeTree.getItem(0);
    int taId = Integer.parseInt(ti.getWidget().getElement().getAttribute("id"));
    if (taId == ta.getId()) {
      attributeTree.removeItem(ti);
      if (ti.getChildCount() > 0) attributeTree.addItem(ti.getChild(0));
    }
    else {
      while (ti.getChildCount() > 0) {
        ti = ti.getChild(0);
        taId = Integer.parseInt(ti.getWidget().getElement().getAttribute("id"));
        if (taId == ta.getId()) {
          ti.getParentItem().removeItem(ti);
          if (ti.getChildCount() > 0) ti.getParentItem().addItem(ti.getChild(0));
          break;
        }
      }
    }
    if (attributeTree.getItemCount() == 0) {
      getTreePanel().clear();
      getTreePanel().add(attrMsgTree);
    }
    setVisibility(ta.getId(),true);     
  }

  private void deleteItemList(TemplateAttribute ta) {
    ArrayList<TemplateListItem> tlis = getTlists().get(getTemplateList());
    for (int i = 0; i < tlis.size(); i++) {
      if (tlis.get(i).getTaId() == ta.getId()) {
        tlis.remove(i);
        break;
      }
    }
    
    TreeItem ti = attributeList.getItem(0);
    int taId = Integer.parseInt(ti.getWidget().getElement().getAttribute("id"));
    if (taId == ta.getId()) {
      attributeList.removeItem(ti);
      if (ti.getChildCount() > 0) attributeList.addItem(ti.getChild(0));
    }
    else {
      while (ti.getChildCount() > 0) {
        ti = ti.getChild(0);
        taId = Integer.parseInt(ti.getWidget().getElement().getAttribute("id"));
        if (taId == ta.getId()) {
          ti.getParentItem().removeItem(ti);
          if (ti.getChildCount() > 0) ti.getParentItem().addItem(ti.getChild(0));
          break;
        }
      }
    }
    if (attributeList.getItemCount() == 0) {
      getTreePanel().clear();
      getTreePanel().add(attrMsgList);
    }
    setVisibility(ta.getId(),true);     
  }

  private TreeItem createTreeItem(final TemplateAttribute ta) {
    TemplateAttributeRowView row = new TemplateAttributeRowView(
        ta, "tree-row");
    row.generateWidgetTree();
    Label x = new Label("x");
    x.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        deleteItemTree(ta);
      }
    });
    row.setWidget(0, row.getCellCount(0), x);
    row.getCellFormatter().addStyleName(0, row.getCellCount(0)-1, "tree-row-x");
    TreeItem ti = new TreeItem(row);
    ti.setUserObject(ta);
    return ti;
  }

  private TreeItem createListItem(final TemplateAttribute ta) {
    TemplateAttributeRowView row = new TemplateAttributeRowView(
        ta, "tree-row");
    row.generateWidgetTree();
    Label x = new Label("x");
    x.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        deleteItemList(ta);
      }
    });
    row.setWidget(0, row.getCellCount(0), x);
    row.getCellFormatter().addStyleName(0, row.getCellCount(0)-1, "tree-row-x");
    TreeItem ti = new TreeItem(row);
    ti.setUserObject(ta);
    return ti;
  }

  public void insertItemTree(TemplateAttribute ta) {
    TemplateTreeItem tti = new TemplateTreeItem(getTemplateTree().getId(), ta.getId());
    getTtrees().get(getTemplateTree()).add(tti);
    insertItemTree(getAttributeTree(), ta);
  }
  
  public void insertItemTree(Tree aTree, TemplateAttribute ta) {
    TreeItem treeItem = createTreeItem(ta);
    if (aTree.getItemCount() == 0) {
      aTree.addItem(treeItem);
      getTreePanel().clear();
      getTreePanel().add(aTree);
    } else {
      TreeItem ti = aTree.getItem(0); 
      while(ti.getChildCount() > 0)
        ti = ti.getChild(0);
      ti.addItem(treeItem);
      ti.setState(true);
      // tweak for correct displaying of parent tree item 
      Element elem = ((Element) ti.getElement().getChild(0).getChild(0).getChild(0)
          .getChild(1));
      elem.setAttribute("style", "width:100%; vertical-align: middle;");

    }
    // hide item in source tree
    setVisibility(ta.getId(), false);
  }

  public void insertItemList(TemplateAttribute ta) {
    TemplateListItem tli = new TemplateListItem(getTemplateList().getId(), ta.getId());
    getTlists().get(getTemplateList()).add(tli);
    insertItemList(getAttributeList(), ta);
  }

  public void insertItemList(Tree aList, TemplateAttribute ta) {
    TreeItem treeItem = createListItem(ta);
    aList.addItem(treeItem);

    // hide item in source tree
    setVisibility(ta.getId(), false);
    if (aList.getItemCount() == 1) {
      getListPanel().clear();
      getListPanel().add(aList);
    }
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
  public void onObjectAttributeCreated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributeUpdated(ObjectAttribute objectAttribute) {
  }

  @Override
  public void onObjectAttributesLoaded(int otId,
      Collection<ObjectAttribute> objectAttributes) {
    fillObjectAttributes(objectAttributes);
  }
  
  //@Override
  public void onTemplateTreesLoaded(ArrayList<TemplateTree> tts) {
    for (TemplateTree tt : tts) {
      Tree newTree = new Tree((Resources) GWT.create(TreeResource.class), false); 
      newTree.setHeight("100%");
      newTree.setWidth("100%");
      
      getTrees().put(tt.getId(), newTree);
      createMenuItem(tt);
    }
  }

  @Override
  public void onTemplateTreeItemsLoaded(TemplateTree tt, 
      ArrayList<TemplateTreeItem> ttis) {
    
    ArrayList<TemplateTreeItem> items = new ArrayList<TemplateTreeItem>();
    Tree aTree = getTrees().get(tt.getId());
    if (aTree != null && aTree.getItemCount() == 0)
      for (TemplateTreeItem tti : ttis) {
        items.add(tti);
        insertItemTree(aTree, tti.getTa());
      }
    getTtrees().put(tt, items);
    
    if (getAttributeTree() != null) 
      synchronize(getAttributeTree());
    getTreePanel().clear();
    if (getAttributeTree() != null && getAttributeTree().getItemCount() > 0) 
      getTreePanel().add(getAttributeTree());
    else
      getTreePanel().add(attrMsgTree);
    initializeTree();
    getBodyRight().add(getDDTable());
  }

  @Override
  public void onTemplateListsLoaded(String ID, int tId, List<TemplateList> tls) {
    for (TemplateList tl : tls) {
      
      Tree newList = new Tree((Resources) GWT.create(TreeResource.class), false); 
      newList.setHeight("100%");
      newList.setWidth("100%");
      
      getLists().put(tl.getId(), newList);
      createMenuItem(tl);
    }
  }

  @Override
  public void onTemplateListItemsLoaded(TemplateList tl, List<TemplateListItem> tlis) {
      
    ArrayList<TemplateListItem> items = new ArrayList<TemplateListItem>();
    Tree aList = getLists().get(tl.getId());
    if (aList != null && aList.getItemCount() == 0)
      for (TemplateListItem tli : tlis) {
        items.add(tli);
        insertItemList(aList, tli.getTa());
      }
    getTlists().put(tl, items);
    
    if (getAttributeList() != null)
      synchronize(getAttributeList());
    getListPanel().clear();
    if (getAttributeList() != null && getAttributeList().getItemCount() > 0)
      getListPanel().add(getAttributeList());
    else
      getListPanel().add(attrMsgList);
    initializeList();
    getBodyRight().add(getDDTable());
  }
  
  @Override
  public void onTemplateAttributesLoaded(Template t,
      ArrayList<TemplateAttribute> tas, TemplateRelation tr) {
    Collections.sort(tas, new TemplateAttributeComparator());
    for (TemplateAttribute ta : tas) {
      TemplateAttributeRowView leafWidget = new TemplateAttributeRowView(
          ta, "tree-row");
      leafWidget.generateWidgetTree();
      attributeDragController.makeDraggable(leafWidget.getWidget(0, 0));
      
      TreeItem leafItem = new TreeItem(leafWidget);
      leafItem.setUserObject(ta);
      getAttributePanel().addItem(leafItem);
    }
    if (getAttributeTree() != null)
      synchronize(getAttributeTree());
    if (getAttributeList() != null)
      synchronize(getAttributeList());
  }  
  
  @Override
  public void close() {
    getModel().removeObjectTypeObserver(this);
    getModel().removeObjectAttributeObserver(this);
    getModel().removeTemplateAttributeObserver(this);
    getModel().removeDataObserver(this);
    hide();
  }  

  // getters and setters
  
  /**
   * Getter for property 'treePanel'.
   * 
   * @return Value for property 'treePanel'.
   */
  public FlowPanel getTreePanel() {
    if (treePanel == null) {
      treePanel = new FlowPanel();
      treePanel.setWidth("100%");
      treePanel.setHeight("100%");
      treePanel.add(attrMsgTree);
    }
    return treePanel;
  }

  /**
   * Getter for property 'listPanel'.
   * 
   * @return Value for property 'listPanel'.
   */
  public FlowPanel getListPanel() {
    if (listPanel == null) {
      listPanel = new FlowPanel();
      listPanel.setWidth("100%");
      listPanel.setHeight("100%");
      listPanel.add(attrMsgList);
    }
    return listPanel;
  }

  
  /**
   * Getter for property 'attributePanel'.
   * 
   * @return Value for property 'attributePanel'.
   */
  protected Tree getAttributePanel() {
    if (attributePanel == null) {
      attributePanel = new Tree((Resources) GWT.create(TreeResource.class), false);
      
      ArrayList<TemplateAttribute> templateAttributes = 
          getModel().getAttrsByTemplate().get(getModel().getTemplate().getId());

      if (templateAttributes == null) {
        // load all attributes at once
        TemplateAttributeLoader tal = new TemplateAttributeLoader(getModel(),
            getModel().getTemplate());
        tal.start();
      } else
        onTemplateAttributesLoaded(getModel().getTemplate(), templateAttributes, null);
    }
    return attributePanel;
  }
  
  /**
   * Getter for property 'ddTable'.
   * 
   * @return Value for property 'ddTable'.
   */
  protected FlexTable getDDTable() {
    if (ddTable == null) {
      ddTable = new FlexTable();
      ddTable.setWidth("100%");
      ddTable.setHeight("100%");
      ddTable.setCellSpacing(10);
      ddTable.getColumnFormatter().setWidth(0, "50%");
      ddTable.getColumnFormatter().setWidth(1, "50%");
      ddTable.getCellFormatter().setStyleName(0, 0, "td-top");
      ddTable.getCellFormatter().setStyleName(0, 1, "td-top");

      ddTable.setWidget(0, 1, getAttributePanel());
    }
    return ddTable;
  }

  /**
   * Create header for the template tree.
   * 
   * @return FlexTable.
   */
  protected FlexTable getTreeHeaderTable() {
    FlexTable treeHeaderTable = new FlexTable();
    treeHeaderTable.setWidth("100%");
    treeHeaderTable.setCellSpacing(10);

    getTbTreeName().setText(getTemplateTree().getName());
    getTbTreeName().addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (getTbTreeName().getText().trim().length() > 0) {
          getTemplateTree().setName(getTbTreeName().getText().trim());
          getMenuItem().setText(getTbTreeName().getText().trim());
        }
      }
    });
    getTbTreeDesc().setText(getTemplateTree().getDesc());
    getTbTreeDesc().addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        getTemplateTree().setDesc(getTbTreeDesc().getText().trim());
      }
    });
    treeHeaderTable.setWidget(0, 0, getLblTreeName());
    treeHeaderTable.setWidget(0, 1, getTbTreeName());
    treeHeaderTable.setWidget(0, 2, getLblTreeDesc());
    treeHeaderTable.setWidget(0, 3, getTbTreeDesc());
    treeHeaderTable.setWidget(0, 4, getLblTreeSort());
    treeHeaderTable.setWidget(0, 5, getDdbTemplateAttribute());
    
    return treeHeaderTable;
  }

  /**
   * Create header for the template list.
   * 
   * @return FlexTable.
   */
  protected FlexTable getListHeaderTable() {
    FlexTable listHeaderTable = new FlexTable();
    listHeaderTable.setWidth("100%");
    listHeaderTable.setCellSpacing(10);

    getTbListName().setText(getTemplateList().getName());
    getTbListName().addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (getTbListName().getText().trim().length() > 0) {
          getTemplateList().setName(getTbListName().getText().trim());
          getMenuItem().setText(getTbListName().getText().trim());
        }
      }
    });
    getTbListDesc().setText(getTemplateList().getDesc());
    getTbListDesc().addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        getTemplateList().setDesc(getTbListDesc().getText().trim());
      }
    });
    listHeaderTable.setWidget(0, 0, getLblListName());
    listHeaderTable.setWidget(0, 1, getTbListName());
    listHeaderTable.setWidget(0, 2, getLblListDesc());
    listHeaderTable.setWidget(0, 3, getTbListDesc());
    
    return listHeaderTable;
  }
  
  /**
   * @return the attributeTree
   */
  public Tree getAttributeTree() {
    return attributeTree;
  }

  /**
   * @param attributeTree the attributeTree to set
   */
  public void setAttributeTree(Tree attributeTree) {
    this.attributeTree = attributeTree;
  }

  /**
   * @return the templateTree
   */
  public TemplateTree getTemplateTree() {
    return templateTree;
  }

  /**
   * @param templateTree the templateTree to set
   */
  public void setTemplateTree(TemplateTree templateTree) {
    this.templateTree = templateTree;
  }

  /**
   * @return the attributeList
   */
  public Tree getAttributeList() {
    return attributeList;
  }

  /**
   * @param attributeList the attributeList to set
   */
  public void setAttributeList(Tree attributeList) {
    this.attributeList = attributeList;
  }

  /**
   * @return the templateList
   */
  public TemplateList getTemplateList() {
    return templateList;
  }

  /**
   * @param templateList the templateList to set
   */
  public void setTemplateList(TemplateList templateList) {
    this.templateList = templateList;
  }

  /**
   * @return the menuItem
   */
  public NavigationLabelView getMenuItem() {
    return menuItem;
  }

  /**
   * @param menuItem the menuItem to set
   */
  public void setMenuItem(NavigationLabelView menuItem) {
    this.menuItem = menuItem;
  }

  /**
   * Getter for property 'objectTemplate'.
   * 
   * @return Value for property 'objectTemplate'.
   */
  public ObjectTemplate getObjectTemplate() {
    if (objectTemplate == null) {
      //objectTemplate = new ObjectTemplate(amodel);
      //objectTemplate.initialize(getModel().getTemplate());
      //objectTemplate.setVisible(false);
      //objectTemplate.setWidth("100%");
    }
    return objectTemplate;
  }

  /**
   * Getter for property 'lblTreeName'.
   * 
   * @return Value for property 'lblTreeName'.
   */
  protected Label getLblTreeName() {
    if (lblTreeName == null) {
      lblTreeName = new Label(Main.constants.treeName());
    }
    return lblTreeName;
  }
  
  /**
   * Getter for property 'tbTreeName'.
   * 
   * @return Value for property 'tbTreeName'.
   */
  protected TextBox getTbTreeName() {
    if (tbTreeName == null) {
      tbTreeName = new TextBox();
    }
    return tbTreeName;
  }

  /**
   * Getter for property 'lblTreeDesc'.
   * 
   * @return Value for property 'lblTreeDesc'.
   */
  protected Label getLblTreeDesc() {
    if (lblTreeDesc == null) {
      lblTreeDesc = new Label(Main.constants.treeDesc());
    }
    return lblTreeDesc;
  }
  
  /**
   * Getter for property 'tbTreeDesc'.
   * 
   * @return Value for property 'tbTreeDesc'.
   */
  protected TextBox getTbTreeDesc() {
    if (tbTreeDesc == null) {
      tbTreeDesc = new TextBox();
    }
    return tbTreeDesc;
  }

  /**
   * Getter for property 'lblTreeSort'.
   * 
   * @return Value for property 'lblTreeSort'.
   */
  protected Label getLblTreeSort() {
    if (lblTreeSort == null) {
      lblTreeSort = new Label(Main.constants.treeSort());
    }
    return lblTreeSort;
  }
  
  /**
   * Getter for property 'lblListName'.
   * 
   * @return Value for property 'lblListName'.
   */
  protected Label getLblListName() {
    if (lblListName == null) {
      lblListName = new Label(Main.constants.listName());
    }
    return lblListName;
  }
  
  /**
   * Getter for property 'tbListName'.
   * 
   * @return Value for property 'tbListName'.
   */
  protected TextBox getTbListName() {
    if (tbListName == null) {
      tbListName = new TextBox();
    }
    return tbListName;
  }

  /**
   * Getter for property 'lblListDesc'.
   * 
   * @return Value for property 'lblListDesc'.
   */
  protected Label getLblListDesc() {
    if (lblListDesc == null) {
      lblListDesc = new Label(Main.constants.listDesc());
    }
    return lblListDesc;
  }

  /**
   * Getter for property 'tbListDesc'.
   * 
   * @return Value for property 'tbListDesc'.
   */
  protected TextBox getTbListDesc() {
    if (tbListDesc == null) {
      tbListDesc = new TextBox();
    }
    return tbListDesc;
  }

  /**
   * Getter for property 'ddbObjectType'.
   * 
   * @return Value for property 'ddbObjectType'.
   */
  protected DropDownBox getDdbObjectType() {
    if (ddbObjectType == null) {
      ddbObjectType = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER, new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
              getDdbObjectAttribute().setSelection(new DropDownObjectImpl(0, 
                  Main.constants.chooseObjectAttribute()));
              Collection<ObjectAttribute> oas = getModel().getObjectAttributes().get(getDdbObjectType().getSelection());
              if (oas == null) {
                ObjectAttributeLoader oal = new ObjectAttributeLoader(getModel(), 
                    getDdbObjectType().getSelection().getId());
                oal.start();    
              } else
                fillObjectAttributes(oas);
            }
      
      });
      if (getModel().getTemplate() != null && getModel().getTemplate().getOt() != null)  
        ddbObjectType.setSelection(new DropDownObjectImpl(getModel().getTemplate().getOtId(), 
            getModel().getTemplate().getOt().getName(), getModel().getTemplate().getOt()));
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
   * Getter for property 'ddbObjectAttribute'.
   * 
   * @return Value for property 'ddbObjectAttribute'.
   */
  protected DropDownBox getDdbObjectAttribute() {
    if (ddbObjectAttribute == null) {
      ddbObjectAttribute = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);
      if (getModel().getTemplate() != null && getModel().getTemplate().getOa() != null)
        ddbObjectAttribute.setSelection(new DropDownObjectImpl(getModel().getTemplate().getOaId(), 
            getModel().getTemplate().getOa().getName(), getModel().getTemplate().getOa()));
      else
        ddbObjectAttribute.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseObjectAttribute()));
      
      if (getDdbObjectType().getSelection().getId() > 0) {
        Collection<ObjectAttribute> oas = getModel().getObjectAttributes().get(getDdbObjectType().getSelection());
        if (oas == null) {
          ObjectAttributeLoader oal = new ObjectAttributeLoader(getModel(), 
              getDdbObjectType().getSelection().getId());
          oal.start();    
        } else
          fillObjectAttributes(oas);
      }
      
    }
    return ddbObjectAttribute;
  }  
  
  /**
   * Getter for property 'ddbTemplateAttribute'.
   * 
   * @return Value for property 'ddbTemplateAttribute'.
   */
  protected DropDownBox getDdbTemplateAttribute() {
    if (ddbTemplateAttribute == null) {
      ddbTemplateAttribute = new DropDownBox(this, null, 
          CSSConstants.SUFFIX_DESIGNER);
//      if (getModel().getTemplate() != null)
//        ddbTemplateAttribute.setSelection(new DropDownObjectImpl(getModel().getTemplate().getOaId(), 
//            getModel().getTemplate().getOa().getName()));
//      else
        ddbTemplateAttribute.setSelection(new DropDownObjectImpl(0, 
            Main.constants.chooseTemplateAttribute()));
    }
    return ddbObjectAttribute;
  }    
  
  /**
   * Getter for property 'widgetTemplate'.
   * 
   * @return Value for property 'widgetTemplate'.
   */
  public FlexTable getWidgetTemplate() {
    if (widgetTemplate == null) {
      widgetTemplate = new FlexTable();
      
      Label lblCode = new Label(Main.constants.templateCode());
      widgetTemplate.setWidget(0, 0, lblCode);
      widgetTemplate.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.templateName());
      widgetTemplate.setWidget(1, 0, lblName);
      widgetTemplate.setWidget(1, 1, getTbName());

      Label lblDesc = new Label(Main.constants.templateDesc());
      widgetTemplate.setWidget(2, 0, lblDesc);
      widgetTemplate.setWidget(2, 1, getTbDesc());

      Label lblOt = new Label(Main.constants.templateOt());
      widgetTemplate.setWidget(3, 0, lblOt);
      widgetTemplate.setWidget(3, 1, getDdbObjectType());
      widgetTemplate.getFlexCellFormatter().addStyleName(3, 1, ClientUtils.CSS_ALIGN_RIGHT);

      Label lblOa = new Label(Main.constants.templateTa());
      widgetTemplate.setWidget(4, 0, lblOa);
      widgetTemplate.setWidget(4, 1, getDdbObjectAttribute());
      widgetTemplate.getFlexCellFormatter().addStyleName(4, 1, ClientUtils.CSS_ALIGN_RIGHT);
    }
    return widgetTemplate;
  }

  /**
   * Getter for property 'widgetNewTree'.
   * 
   * @return Value for property 'widgetNewTree'.
   */
  public FlexTable getWidgetNewTree() {
    if (widgetNewTree == null) {
      widgetNewTree = new FlexTable();
      
      widgetNewTree.setWidget(0, 0, getLblTreeName());
      widgetNewTree.setWidget(0, 1, getTbTreeName());

      widgetNewTree.setWidget(1, 0, getLblTreeDesc());
      widgetNewTree.setWidget(1, 1, getTbTreeDesc());

      ButtonView bNewTree = new ButtonView(ClientUtils.CSS_BUTTON + " " 
          + ClientUtils.CSS_DIALOG_BUTTON);
      bNewTree.getElement().setInnerText(Main.constants.treeNewTree());
      bNewTree.addDomHandler(
          new ClickHandler() {
            public void onClick(ClickEvent event) {
              final TemplateTree tt = new TemplateTree(getTbTreeName().getText(), 
                  getModel().getTemplate().getId());
              tt.setDesc(getTbTreeDesc().getText());
              tt.setRank(menu.getItem(1).getChildCount());
              
              Tree newTree = new Tree((Resources) GWT.create(TreeResource.class), false); 
              newTree.setHeight("100%");
              newTree.setWidth("100%");
              getTrees().put(tt.getId(), newTree);              
              
              NavigationLabelView menuTree = createMenuItem(tt);
              menuTree.fireEvent(event);
              menuTree.onDialogNavigationItemClicked(menuTree.getElement());
            }
          }, ClickEvent.getType());
      widgetNewTree.setWidget(2, 1, bNewTree);
      widgetNewTree.getCellFormatter().addStyleName(2, 1, "td-right");
    }
    getTbTreeName().setText("");
    getTbTreeDesc().setText("");
    
    return widgetNewTree;
  }

  /**
   * Getter for property 'widgetNewList'.
   * 
   * @return Value for property 'widgetNewList'.
   */
  public FlexTable getWidgetNewList() {
    if (widgetNewList == null) {
      widgetNewList = new FlexTable();
      
      widgetNewList.setWidget(0, 0, getLblListName());
      widgetNewList.setWidget(0, 1, getTbListName());

      widgetNewList.setWidget(1, 0, getLblListDesc());
      widgetNewList.setWidget(1, 1, getTbListDesc());

      ButtonView bNewList = new ButtonView(ClientUtils.CSS_BUTTON + " " 
          + ClientUtils.CSS_DIALOG_BUTTON);
      bNewList.getElement().setInnerText(Main.constants.listNewList());
      bNewList.addDomHandler(
          new ClickHandler() {
            public void onClick(ClickEvent event) {
              final TemplateList tl = new TemplateList(getTbListName().getText(), 
                  getModel().getTemplate().getId());
              tl.setDesc(getTbListDesc().getText());
              tl.setRank(menu.getItem(2).getChildCount());
              
              Tree newList = new Tree((Resources) GWT.create(TreeResource.class), false); 
              newList.setHeight("100%");
              newList.setWidth("100%");
              getLists().put(tl.getId(), newList);
              
              NavigationLabelView menuList = createMenuItem(tl);
              menuList.fireEvent(event);
              menuList.onDialogNavigationItemClicked(menuList.getElement());
            }
          }, ClickEvent.getType());
      widgetNewList.setWidget(2, 1, bNewList);
      widgetNewList.getCellFormatter().addStyleName(2, 1, "td-right");
    }
    getTbListName().setText("");
    getTbListDesc().setText("");
    
    return widgetNewList;
  }

  /**
   * Getter for property 'ttrees'.
   * 
   * @return Value for property 'ttrees'.
   */
  protected HashMap<TemplateTree, ArrayList<TemplateTreeItem>> getTtrees() {
    if (ttrees == null) {
      ttrees = new HashMap<TemplateTree, ArrayList<TemplateTreeItem>>();
    }
    return ttrees;
  }

  /**
   * Getter for property 'trees'.
   * 
   * @return Value for property 'trees'.
   */
  protected HashMap<Integer, Tree> getTrees() {
    if (trees == null) {
      trees = new HashMap<Integer, Tree>();
    }
    return trees;
  }

  /**
   * Getter for property 'tlists'.
   * 
   * @return Value for property 'tlists'.
   */
  protected HashMap<TemplateList, ArrayList<TemplateListItem>> getTlists() {
    if (tlists == null) {
      tlists = new HashMap<TemplateList, ArrayList<TemplateListItem>>();
    }
    return tlists;
  }

  /**
   * Getter for property 'lists'.
   * 
   * @return Value for property 'lists'.
   */
  protected HashMap<Integer, Tree> getLists() {
    if (lists == null) {
      lists = new HashMap<Integer, Tree>();
    }
    return lists;
  }
  
  // private methods

  private void fill(Template template) {
    super.fill(template);

    // object type
    int iot = getDdbObjectType().getSelection().getId();
    if (iot > 0) {
      template.setOtId(iot);
      template.setOt((ObjectType)getDdbObjectType().getSelection().getUserObject());
    }
    // leaf attribute
    int ioa = getDdbObjectAttribute().getSelection().getId();
    if (ioa > 0) {
      template.setOaId(ioa);
      template.setOa((ObjectAttribute)getDdbObjectAttribute().getSelection().getUserObject());
    }
  }
  
  private void fillObjectTypes (Collection<ObjectType> objectTypes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (ObjectType ot : objectTypes) {
      items.add(new DropDownObjectImpl(ot.getId(), ot.getName(), ot));
    }
    getDdbObjectType().setItems(items);
  }

  private void fillObjectAttributes (Collection<ObjectAttribute> objectAttributes) {
    ArrayList<DropDownObject> items = new ArrayList<DropDownObject>(); 
    for (ObjectAttribute oa : objectAttributes) {
      items.add(new DropDownObjectImpl(oa.getId(), oa.getName(), oa));
    }
    getDdbObjectAttribute().setItems(items);
  }
}
