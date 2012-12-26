package sk.benko.appsresource.client.designer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.dnd.ApplicationDialogDragController;
import sk.benko.appsresource.client.dnd.ApplicationDialogDropController;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.ApplicationTemplateLoader;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;
import sk.benko.appsresource.client.model.Template;
import sk.benko.appsresource.client.model.loader.TemplateLoader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Tree.Resources;
import com.google.gwt.user.client.ui.TreeItem;

/**
 *
 */
public class ApplicationDialog extends DesignerDialog implements
    Model.ApplicationObserver, DesignerModel.ApplicationObserver, 
    DesignerModel.TemplateObserver {
  
  private TextBox tbCat;
  private CheckBox cbPublic;
  private CheckBox cbRecommended;

  private FlexTable widgetApp;
  private FlexTable widgetTemplates;
  
  private FlowPanel apptPanel;
  private FlowPanel templatePanel;
  
  private Tree templateTree;
  private Tree apptTree;
  private FlowPanel apptMsg;

  private HashMap<Integer, TreeItem> hmAppts;

  private ApplicationDialogDragController templateDragController;
  private ApplicationDialogDropController apptDropController;

  /**
   * @param model
   *          the model to which the UI will bind itself
   * @param app
   *          the application for editing
   */
  public ApplicationDialog(final DesignerModel model, Application app) {
    super(model, app);
    getModel().addDataObserver(this);
    getModel().addTemplateObserver(this);
    getModel().setApplication(app);

    getHeader().add(new Label((getModel().getApplication() == null ? 
        Main.constants.newItem() + " " : "") + Main.constants.application()));

    NavigationLabelView menu1 = new NavigationLabelView(
        model, Main.constants.application(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        model.notifyDialogNavigationItemClicked(event.getRelativeElement());
        getBodyRight().clear();
        getBodyRight().add(getWidgetApp());
      }
    }); 
    menu1.addStyleName("dialog-box-navigation-item dialog-box-navigation-item-selected");
    getBodyLeft().add(menu1);

    NavigationLabelView menu2 = new NavigationLabelView(
        model, Main.constants.templates(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (getModel().getApplication() != null) {
          model.notifyDialogNavigationItemClicked(event.getRelativeElement());
          getBodyRight().clear();
          getBodyRight().add(getWidgetTemplates());
        }
      }
    });
    if (getModel().getApplication() != null)
      menu2.addStyleName("dialog-box-navigation-item");
    else
      menu2.addStyleName("dialog-box-navigation-item-disabled");
    getBodyLeft().add(menu2);

    getBodyRight().add(getWidgetApp());

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
              if (getModel().getApplication() == null)
                getModel().setApplication(new Application(getTbName().getText()));
              
              fill(getModel().getApplication());
              model.createOrUpdateApplication(getModel().getApplication(), getAppts());
              ApplicationDialog.this.hide();
            }
        }, ClickEvent.getType());
    getBOk().getElement().setInnerText(getModel().getApplication() == null ? 
        Main.constants.create() : Main.constants.save());
    model.getStatusObserver().onTaskFinished();
  }

  @Override
  public void onApplicationCreated(Application application) {
  }

  @Override
  public void onApplicationUpdated(Application application) {
  }

  //@Override
  public void onApplicationTemplatesLoaded(ArrayList<ApplicationTemplate> appts) {
    for (ApplicationTemplate appt : appts) 
      insertItem(appt);
  }

  @Override
  public void onTemplateCreated(Template template) {
  }

  @Override
  public void onTemplateUpdated(Template template) {
  }

  @Override
  public void onTemplatesLoaded(Collection<Template> templates) {
    for (Template ti : templates) {
      TemplateRowView leafWidget = new TemplateRowView(ti, "tree-row");
      leafWidget.generateWidgetTree();
      
      getTemplateDragController().makeDraggable(leafWidget.getWidget(0, 0));
      TreeItem leafItem = new TreeItem(leafWidget);
      leafItem.setUserObject(ti);
      getTemplateTree().addItem(leafItem);
    }
    
    if (getModel().getAppTemplatesByApp().get(getModel().getApplication().getId()) == null) {
      ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
          getModel().getApplication());
      atl.start();
    } else
      onApplicationTemplatesLoaded(getModel().getAppTemplatesByApp()
          .get(getModel().getApplication().getId()));
  }

  public void insertItem(ApplicationTemplate appt) {
    if (getApptTree().getItemCount() == 0) {
      getApptMsg().removeFromParent();
      getApptPanel().add(getApptTree());
    }
    
    TreeItem treeItem = createTreeItem(appt);
    treeItem.setUserObject(appt);
    getHmAppts().put(appt.getTId(), treeItem);
    
    if (appt.getParentMenuId() == 0) {
      if (appt.getRank() == -1) 
        getApptTree().insertItem(0, treeItem);
      else
        getApptTree().addItem(treeItem);
    } else {
      TreeItem parent = getHmAppts().get(appt.getParentMenuId()); 
      parent.addItem(treeItem);
      parent.setState(true);
      // tweak for correct displaying of parent tree item 
      Element elem = ((Element) parent.getElement().getChild(0).getChild(0).getChild(0)
          .getChild(1));
      elem.setAttribute("style", "width:100%; vertical-align: middle;");
    }
    // hide item in source tree
    setVisibility(appt, false);
  }
  
  /**
   * Getter for property 'templateDragController'.
   * 
   * @return Value for property 'templateDragController'.
   */
  public ApplicationDialogDragController getTemplateDragController() {
    if (templateDragController == null) { 
      templateDragController = new ApplicationDialogDragController(getMain());
      templateDragController.registerDropController(getApptDropController());
    }
    return templateDragController;
  }  

  /**
   * Getter for property 'apptDropController'.
   * 
   * @return Value for property 'apptDropController'.
   */
  public ApplicationDialogDropController getApptDropController() {
    if (apptDropController == null) { 
      apptDropController = new ApplicationDialogDropController(this);
    }
    return apptDropController;
  }  

  /**
   * Getter for property 'hmAppts'.
   * 
   * @return Value for property 'hmAppts'.
   */
  public HashMap<Integer, TreeItem> getHmAppts() {
    if (hmAppts == null) 
      hmAppts = new HashMap<Integer, TreeItem>();
    return hmAppts;
  }
  
  /**
   * Getter for property 'tbCat'.
   * 
   * @return Value for property 'tbCat'.
   */
  public TextBox getTbCat() {
    if (tbCat == null) {
      tbCat = new TextBox();
      if (getModel().getApplication() != null) 
        tbCat.setText(getModel().getApplication().getCategory());
    }  
    return tbCat;
  }

  /**
   * Getter for property 'cbPublic'.
   * 
   * @return Value for property 'cbPublic'.
   */
  public CheckBox getCbPublic() {
    if (cbPublic == null) {
      cbPublic = new CheckBox();
      cbPublic.setText(Main.constants.applicationPublic());
      if (getModel().getApplication() != null) 
        cbPublic.setValue(ClientUtils
            .getFlag(Application.FLAG_PUBLIC, getModel().getApplication().getFlags()));
    }  
    return cbPublic;
  }  

  /**
   * Getter for property 'cbRecommended'.
   * 
   * @return Value for property 'cbRecommended'.
   */
  public CheckBox getCbRecommended() {
    if (cbRecommended == null) {
      cbRecommended = new CheckBox();
      cbRecommended.setText(Main.constants.applicationRecommended());
      if (getModel().getApplication() != null) 
        cbRecommended.setValue(ClientUtils
            .getFlag(Application.FLAG_RECOMMENDED, getModel().getApplication().getFlags()));
    }  
    return cbRecommended;
  }  

  /**
   * Getter for property 'templateTree'.
   * 
   * @return Value for property 'templateTree'.
   */
  public Tree getTemplateTree() {
    if (templateTree == null) {
      templateTree = new Tree((Resources) GWT.create(TreeResource.class), false);
    }  
    return templateTree;
  }  
  
  /**
   * Getter for property 'apptTree'.
   * 
   * @return Value for property 'apptTree'.
   */
  public Tree getApptTree() {
    if (apptTree == null) {
      apptTree = new Tree((Resources) GWT.create(TreeResource.class), false);
      apptTree.setHeight("100%");
      apptTree.setWidth("100%");
    }  
    return apptTree;
  }  
  
  /**
   * Getter for property 'apptPanel'.
   * 
   * @return Value for property 'apptPanel'.
   */
  public FlowPanel getApptPanel() {
    if (apptPanel == null) {
      apptPanel = new FlowPanel();
      apptPanel.setWidth("100%");
      apptPanel.setHeight("100%");

      Label label = new Label(Main.constants.applicationNavigationTree());
      label.setStyleName(ClientUtils.CSS_DIALOGBOX_LABEL);
      label.addStyleDependentName(CSSConstants.SUFFIX_BOLD);
      apptPanel.add(label);
      apptPanel.add(getApptMsg());
    }
    
    return apptPanel;
  }

  /**
   * Getter for property 'templatePanel'.
   * 
   * @return Value for property 'templatePanel'.
   */
  public FlowPanel getTemplatePanel() {
    if (templatePanel == null) {
      templatePanel = new FlowPanel();

      Label label = new Label(Main.constants.applicationAvaiableTemplates());
      label.setStyleName(ClientUtils.CSS_DIALOGBOX_LABEL);
      label.addStyleDependentName(CSSConstants.SUFFIX_BOLD);
      templatePanel.add(label);
      templatePanel.add(getTemplateTree());
    }
    
    return templatePanel;
  }
  
  /**
   * Getter for property 'apptMsg'.
   * 
   * @return Value for property 'apptMsg'.
   */
  public FlowPanel getApptMsg() {
    if (apptMsg == null) {
      apptMsg = new FlowPanel();
      apptMsg.setStyleName("hint-frame");
      Label lblMessage = new Label(Main.messages.dragTemplates());
      lblMessage.setStyleName("hint-message");
      apptMsg.add(lblMessage);
    }
    return apptMsg;
  }

  
  /**
   * Getter for property 'widgetApp'.
   * 
   * @return Value for property 'widgetApp'.
   */
  public FlexTable getWidgetApp() {
    if (widgetApp == null) {
      widgetApp = new FlexTable();
      
      Label lblCode = new Label(Main.constants.applicationCode());
      widgetApp.setWidget(0, 0, lblCode);
      widgetApp.setWidget(0, 1, getLblCodeValue());

      Label lblName = new Label(Main.constants.applicationName());
      widgetApp.setWidget(1, 0, lblName);
      widgetApp.setWidget(1, 1, getTbName());

      Label lblCat = new Label(Main.constants.applicationCat());
      widgetApp.setWidget(2, 0, lblCat);
      widgetApp.setWidget(2, 1, getTbCat());

      widgetApp.setWidget(3, 0, getCbPublic());
      widgetApp.setWidget(3, 1, getCbRecommended());

      Label lblDesc = new Label(Main.constants.applicationDesc());
      widgetApp.setWidget(4, 0, lblDesc);
      widgetApp.setWidget(5, 1, getTbDesc());
    }
    return widgetApp;
  }
  
  /**
   * Getter for property 'widgetTemplates'.
   * 
   * @return Value for property 'widgetTemplates'.
   */
  public FlexTable getWidgetTemplates() {
    if (widgetTemplates == null) {
      widgetTemplates = new FlexTable();

      widgetTemplates.setWidth("100%");
      widgetTemplates.setHeight("100%");
      widgetTemplates.setCellSpacing(10);
      widgetTemplates.getColumnFormatter().setWidth(0, "50%");
      widgetTemplates.getColumnFormatter().setWidth(1, "50%");
      widgetTemplates.getCellFormatter().setStyleName(0, 1, "td-top");
      
      
      widgetTemplates.setWidget(0, 0, getApptPanel());
      widgetTemplates.setWidget(0, 1, getTemplatePanel());
      
      if (getModel().getTemplates() == null) {
        TemplateLoader tl = new TemplateLoader(getModel());
        tl.start();
      } else
        onTemplatesLoaded(getModel().getTemplates());
    }
    return widgetTemplates;
  }

  // private methods
  
  private void fill(Application app) {
    super.fill(app);
    
    // category
    app.setCategory(getTbCat().getText().trim());

    // flags
    int flags = 0;
    if (getCbPublic().getValue())
      flags = ClientUtils.setFlag(Application.FLAG_PUBLIC, flags);
    else
      flags = ClientUtils.unsetFlag(Application.FLAG_PUBLIC, flags);
    if (getCbRecommended().getValue())
      flags = ClientUtils.setFlag(Application.FLAG_RECOMMENDED, flags);
    else
      flags = ClientUtils.unsetFlag(Application.FLAG_RECOMMENDED, flags);
    app.setFlags(flags);
  }
  
  private ArrayList<ApplicationTemplate> getAppts() {
    ArrayList<ApplicationTemplate> appts = new ArrayList<ApplicationTemplate>();
    if (getApptTree() != null)
      for (int i = 0; i < getApptTree().getItemCount(); i++) {
        TreeItem ti = getApptTree().getItem(i);
        ApplicationTemplate appt = (ApplicationTemplate)ti.getUserObject();
        appt.setRank(i);
        // flags
        int flags = 0;
        if (((FlexTable)ti.getWidget()).getWidget(0, 1) instanceof CheckBox) {
          if (((CheckBox)((FlexTable)ti.getWidget()).getWidget(0, 1)).getValue())
            flags = ClientUtils.setFlag(ApplicationTemplate.FLAG_PUBLICDATA, flags);
          else
            flags = ClientUtils.unsetFlag(ApplicationTemplate.FLAG_PUBLICDATA, flags);
        }
        appt.setFlags(flags);
        appts.add(appt);
        appts.addAll(getAppts(ti, appt.getTId()));
      }
    return appts;
  }

  private ArrayList<ApplicationTemplate> getAppts(TreeItem ti, int tId) {
    ArrayList<ApplicationTemplate> appts = new ArrayList<ApplicationTemplate>();
    for (int i = 0; i < ti.getChildCount(); i++) {
      TreeItem tci = ti.getChild(i);
      ApplicationTemplate appt = (ApplicationTemplate)tci.getUserObject();
      appt.setRank(i);
      int flags = 0;
      if (((CheckBox)((FlexTable)tci.getWidget()).getWidget(0, 1)).getValue())
        flags = ClientUtils.setFlag(ApplicationTemplate.FLAG_PUBLICDATA, flags);
      else
        flags = ClientUtils.unsetFlag(ApplicationTemplate.FLAG_PUBLICDATA, flags);
      appt.setFlags(flags);
      appt.setParentMenuId(tId);
      appts.add(appt);
      appts.addAll(getAppts(tci, appt.getTId()));
    }
    return appts;
  }

  private TreeItem createTreeItem(final ApplicationTemplate appt) {
    ApplicationTemplateRowView row = new ApplicationTemplateRowView(
        appt, "tree-row");
    row.generateWidgetDefTree();
    Label x = new Label("x");
    x.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        deleteItem(appt);
      }
    });
    row.setWidget(0, row.getCellCount(0), x);
    row.getCellFormatter().addStyleName(0, row.getCellCount(0)-1, "tree-row-x");
    TreeItem ti = new TreeItem(row);
    ti.setUserObject(appt.getT());
    return ti;
  }
  
  private void deleteItem(ApplicationTemplate appt) {
    TreeItem ti = getHmAppts().get(appt.getTId());
    ti.remove();
    getHmAppts().remove(appt.getTId());

    if (getApptTree().getItemCount() == 0) {
      getApptTree().removeFromParent();
      getApptPanel().add(getApptMsg());
    }
    
    setVisibility(appt, true);
  }

  private void setVisibility(ApplicationTemplate appt, boolean b) {
    for (int i = 0; i < getTemplateTree().getItemCount(); i++) {
      TreeItem ti = getTemplateTree().getItem(i);
      if (appt.getTId() == ((Template)ti.getUserObject()).getId()) {
        ti.setVisible(b);
      }
    }
  }
}
