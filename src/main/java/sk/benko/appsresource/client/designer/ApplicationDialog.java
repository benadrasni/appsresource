package sk.benko.appsresource.client.designer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Tree.Resources;
import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.dnd.ApplicationDialogDragController;
import sk.benko.appsresource.client.dnd.ApplicationDialogDropController;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabel;
import sk.benko.appsresource.client.model.*;
import sk.benko.appsresource.client.model.loader.TemplateLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ApplicationDialog extends DesignerDialog implements Model.ApplicationObserver,
    DesignerModel.ApplicationObserver, DesignerModel.TemplateObserver {

  private static FlowPanel apptMsg;

  static {
    apptMsg = new FlowPanel();
    apptMsg.setStyleName("hint-frame");
    Label lblMessage = new Label(Main.messages.dragTemplates());
    lblMessage.setStyleName("hint-message");
    apptMsg.add(lblMessage);
  }

  private TextBox tbCat;
  private CheckBox cbPublic;
  private CheckBox cbRecommended;
  private FlexTable widgetApp;
  private FlexTable widgetTemplates;
  private FlowPanel apptPanel;
  private FlowPanel templatePanel;
  private Tree templateTree;
  private Tree apptTree;
  private HashMap<Integer, TreeItem> hmAppts;
  private ApplicationDialogDragController templateDragController;
  private NavigationLabel menu2;

  /**
   * @param designerView the top level view
   */
  public ApplicationDialog(final DesignerView designerView) {
    super(designerView);
    setHeaderText(Main.constants.application());

    menu2 = new NavigationLabel(designerView, Main.constants.templates(),
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getApplication() != null) {
              designerView.getDesignerModel().notifyDialogNavigationItemClicked(event.getRelativeElement());
              getBodyRight().clear();
              getBodyRight().add(getWidgetTemplates());
            }
          }
    });
    getMenu().add(menu2);

    getBOk().addDomHandler(
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (getModel().getApplication() == null)
              setItem(new Application(getTbName().getText()));

            fill(getApplication());
            designerView.getDesignerModel().createOrUpdateApplication(getApplication(), getAppts());
            close();
          }
        }, ClickEvent.getType());

    templateDragController = new ApplicationDialogDragController(getMain());
    templateDragController.registerDropController(new ApplicationDialogDropController(this));
    hmAppts = new HashMap<Integer, TreeItem>();
    templateTree = new Tree((Resources) GWT.create(TreeResource.class), false);
    apptTree = new Tree((Resources) GWT.create(TreeResource.class), false);
    apptTree.setHeight("100%");
    apptTree.setWidth("100%");

    tbCat = new TextBox();
    cbPublic = new CheckBox(Main.constants.applicationPublic());
    cbRecommended = new CheckBox(Main.constants.applicationRecommended());

    if (getModel().getTemplates() == null) {
      TemplateLoader tl = new TemplateLoader(getModel());
      tl.start();
    } else {
      onTemplatesLoaded(getModel().getTemplates());
    }

    // must be called after initializing UI components
    getBodyRight().add(getItemWidget());
    reset();
  }

  @Override
  public void onApplicationCreated(Application application) {
  }

  @Override
  public void onApplicationUpdated(Application application) {
  }

  @Override
  public void onApplicationTemplatesLoaded(List<ApplicationTemplate> appts) {
    apptTree.clear();
    for (ApplicationTemplate appt : appts) {
      insertItem(appt);
    }
    getDesignerView().getDesignerModel().getStatusObserver().onTaskFinished();
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

      templateDragController.makeDraggable(leafWidget.getWidget(0, 0));
      TreeItem leafItem = new TreeItem(leafWidget);
      leafItem.setUserObject(ti);
      templateTree.addItem(leafItem);
    }
    getDesignerView().getDesignerModel().getStatusObserver().onTaskFinished();
  }

  public void insertItem(ApplicationTemplate appt) {
    if (apptTree.getItemCount() == 0) {
      apptMsg.removeFromParent();
      getApptPanel().add(apptTree);
    }

    TreeItem treeItem = createTreeItem(appt);
    treeItem.setUserObject(appt);
    hmAppts.put(appt.getTId(), treeItem);

    if (appt.getParentMenuId() == 0) {
      if (appt.getRank() == -1)
        apptTree.insertItem(0, treeItem);
      else
        apptTree.addItem(treeItem);
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
   * @return the objectType
   */
  public Application getApplication() {
    return (Application) getItem();
  }

  /**
   * Getter for property 'hmAppts'.
   *
   * @return Value for property 'hmAppts'.
   */
  public HashMap<Integer, TreeItem> getHmAppts() {
    return hmAppts;
  }

  /**
   * Getter for property 'apptTree'.
   *
   * @return Value for property 'apptTree'.
   */
  public Tree getApptTree() {
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
      apptPanel.add(apptMsg);
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
      templatePanel.add(templateTree);
    }

    return templatePanel;
  }

  /**
   * Getter for property 'widgetApp'.
   *
   * @return Value for property 'widgetApp'.
   */
  @Override
  public FlexTable getItemWidget() {
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
      widgetApp.setWidget(2, 1, tbCat);

      widgetApp.setWidget(3, 0, cbPublic);
      widgetApp.setWidget(3, 1, cbRecommended);

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
    }
    return widgetTemplates;
  }

  /**
   * Fill {@link Application} with values from UI. Parent method must be called first.
   *
   * @param app the application which should be filled with UI values
   */
  protected void fill(Application app) {
    super.fill(app);

    // category
    app.setCategory(tbCat.getText().trim());

    // flags
    int flags = 0;
    if (cbPublic.getValue())
      flags = ClientUtils.setFlag(Application.FLAG_PUBLIC, flags);
    else
      flags = ClientUtils.unsetFlag(Application.FLAG_PUBLIC, flags);
    if (cbRecommended.getValue())
      flags = ClientUtils.setFlag(Application.FLAG_RECOMMENDED, flags);
    else
      flags = ClientUtils.unsetFlag(Application.FLAG_RECOMMENDED, flags);
    app.setFlags(flags);
  }

  /**
   * Load {@link Application} to UI. Parent method must be called first.
   *
   * @param item the application which should be loaded to UI
   */
  @Override
  protected void load(DesignItem item) {
    assert (item != null);
    super.load(item);

    Application application = (Application) item;
    tbCat.setText(application.getCategory());
    cbPublic.setValue(ClientUtils.getFlag(Application.FLAG_PUBLIC, application.getFlags()));
    cbRecommended.setValue(ClientUtils.getFlag(Application.FLAG_RECOMMENDED, application.getFlags()));

    if (getModel().getAppTemplatesByApp().get(application.getId()) == null) {
      ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), application);
      atl.start();
    } else {
      onApplicationTemplatesLoaded(getModel().getAppTemplatesByApp().get(application.getId()));
    }

    menu2.removeStyleName("dialog-box-navigation-item-disabled");
    menu2.addStyleName("dialog-box-navigation-item");
  }

  /**
   * Reset UI fields. Parent method must be called first.
   */
  @Override
  protected void reset() {
    super.reset();
    tbCat.setText("");
    cbPublic.setValue(false);
    cbRecommended.setValue(false);
    templateTree.clear();
    apptTree.clear();

    menu2.removeStyleName("dialog-box-navigation-item");
    menu2.addStyleName("dialog-box-navigation-item-disabled");
  }

  // private methods

  private ArrayList<ApplicationTemplate> getAppts() {
    ArrayList<ApplicationTemplate> appts = new ArrayList<ApplicationTemplate>();
    if (apptTree != null)
      for (int i = 0; i < apptTree.getItemCount(); i++) {
        TreeItem ti = apptTree.getItem(i);
        ApplicationTemplate appt = (ApplicationTemplate) ti.getUserObject();
        appt.setRank(i);
        // flags
        int flags = 0;
        if (((FlexTable) ti.getWidget()).getWidget(0, 1) instanceof CheckBox) {
          if (((CheckBox) ((FlexTable) ti.getWidget()).getWidget(0, 1)).getValue())
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
      ApplicationTemplate appt = (ApplicationTemplate) tci.getUserObject();
      appt.setRank(i);
      int flags = 0;
      if (((CheckBox) ((FlexTable) tci.getWidget()).getWidget(0, 1)).getValue())
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
    row.getCellFormatter().addStyleName(0, row.getCellCount(0) - 1, "tree-row-x");
    TreeItem ti = new TreeItem(row);
    ti.setUserObject(appt.getT());
    return ti;
  }

  private void deleteItem(ApplicationTemplate appt) {
    TreeItem ti = getHmAppts().get(appt.getTId());
    ti.remove();
    getHmAppts().remove(appt.getTId());

    if (apptTree.getItemCount() == 0) {
      apptTree.removeFromParent();
      getApptPanel().add(apptMsg);
    }

    setVisibility(appt, true);
  }

  private void setVisibility(ApplicationTemplate appt, boolean b) {
    for (int i = 0; i < templateTree.getItemCount(); i++) {
      TreeItem ti = templateTree.getItem(i);
      if (appt.getTId() == ((Template) ti.getUserObject()).getId()) {
        ti.setVisible(b);
      }
    }
  }
}
