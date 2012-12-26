package sk.benko.appsresource.client.layout;

import java.util.ArrayList;
import java.util.HashMap;

import sk.benko.appsresource.client.TreeResource;
import sk.benko.appsresource.client.designer.ApplicationTemplateRowView;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.ApplicationTemplateLoader;
import sk.benko.appsresource.client.model.Model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Tree.Resources;
import com.google.gwt.user.client.ui.TreeItem;

/**
 *
 */
public class ApplicationTreeView extends FlowPanel implements 
    Model.ApplicationObserver {
  private ApplicationView av;
  private Tree apptTree;

  /**
   * 
   */
  public ApplicationTreeView(final ApplicationView av) {
    getElement().setId("application-tree");

    setAv(av);
    setApptTree(new Tree((Resources) GWT.create(TreeResource.class), false));
  }
  
  public void initialize() {
    clear();
    getApptTree().clear();
    add(getApptTree());

    ApplicationTemplateLoader atl = new ApplicationTemplateLoader(getModel(), 
        getModel().getAppu().getApp());
    atl.start();
  }
  
  @Override
  public void onApplicationTemplatesLoaded(ArrayList<ApplicationTemplate> appts) {
    HashMap<Integer, TreeItem> hm = new HashMap<Integer, TreeItem>(); 

    for (ApplicationTemplate appt : appts) {
      // create tree item
      ApplicationTemplateRowView row = new ApplicationTemplateRowView(appt, "tree-row");
      row.generateWidgetTree(getAv().getModel());
      TreeItem treeItem = new TreeItem(row);
      //treeItem.setUserObject(appt);
      hm.put(appt.getTId(), treeItem);
      
      if (appt.getParentMenuId() == 0) {
        getApptTree().addItem(treeItem);
      }
      else {
        TreeItem parent = hm.get(appt.getParentMenuId()); 
        parent.addItem(treeItem);
        // tweak for correct displaying of parent tree item 
        Element elem = ((Element) parent.getElement().getChild(0).getChild(0).getChild(0)
            .getChild(1));
        elem.setAttribute("style", "width:100%; vertical-align: middle;");
      }
    }
  }

  public void onLoad() {
    getModel().addDataObserver(this);
  }

  public void onUnload() {
    getModel().removeDataObserver(this);
  }

  /* Getters and Setters */
  
  public ApplicationView getAv() {
    return av;
  }

  public void setAv(ApplicationView av) {
    this.av = av;
  }

  public Tree getApptTree() {
    return apptTree;
  }

  public void setApptTree(Tree apptTree) {
    this.apptTree = apptTree;
  }
  
  public ApplicationModel getModel() {
    return av.getModel();
  }
}
