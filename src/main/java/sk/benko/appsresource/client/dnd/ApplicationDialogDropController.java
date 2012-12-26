package sk.benko.appsresource.client.dnd;

import sk.benko.appsresource.client.designer.ApplicationDialog;
import sk.benko.appsresource.client.designer.ApplicationTemplateRowView;
import sk.benko.appsresource.client.model.ApplicationTemplate;
import sk.benko.appsresource.client.model.Template;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetArea;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationDialogDropController extends AbstractPositioningDropController {

  private static final String CSS_TABLE_POSITIONER = "tree-positioner";
  private static final String CSS_TABLE_POSITIONER1 = "tree-positioner1";

  private ApplicationDialog appDialog;

  private Widget positioner = null;
  private int targetRow;
  private Widget targetItem;
  private boolean subtree;
  
  private class WidgetCount {
    private Widget widget;
    private int count;
    
    public WidgetCount() {
      count = 0;
      widget = null;
    }
    
    public void countPlus() {
      count++;
    }

    public int getCount() {
      return count;
    }

    public Widget getWidget() {
      return widget;
    }
    
    public void setWidget(Widget widget) {
      this.widget = widget;
    }
  }
  
  private InsertPanel treeItemsAsIndexPanel = new InsertPanel() {

    @Override
    public void add(Widget w) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Widget getWidget(int index) {
      return getTreeWidget(index);
    }
    
    @Override
    public int getWidgetCount() {
      int count = appDialog.getApptTree().getItemCount();
      for (int i = 0; i < appDialog.getApptTree().getItemCount(); i++) {
        if (appDialog.getApptTree().getItem(i).getState())
          count = count + getWidgetCount(appDialog.getApptTree().getItem(i));
      }
      return count;
    }

    private int getWidgetCount(TreeItem item) {
      int count = item.getChildCount();
      for (int i = 0; i < item.getChildCount(); i++) {
        if (item.getChild(i).getState())
          count = count + getWidgetCount(item.getChild(i));
      }
      return count;
    }

    @Override
    public int getWidgetIndex(Widget child) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(int index) {
      throw new UnsupportedOperationException();
    }
  };

  public ApplicationDialogDropController(ApplicationDialog appDialog) {
    super(appDialog.getApptPanel());
    this.appDialog = appDialog;
  }

  @Override
  public void onDrop(DragContext context) {
    ApplicationDialogDragController templateDragController = 
        (ApplicationDialogDragController) context.dragController;
    final Template sourceTemplate = templateDragController.getDragItem();
    if (targetItem != null) targetItem.removeStyleName(CSS_TABLE_POSITIONER1);
    
    // create application template connection
    ApplicationTemplate appt = new ApplicationTemplate(
        appDialog.getModel().getApplication().getId(), sourceTemplate.getId());
    appt.setApp(appDialog.getModel().getApplication());
    appt.setT(sourceTemplate);
    appt.setRank(targetRow);
    // parentMenu
    int targetApptId = 0;
    if (targetItem != null)
      targetApptId = ((ApplicationTemplateRowView)targetItem).getAppt().getTId();
    if (subtree) appt.setParentMenuId(targetApptId);
    else if (targetItem != null) {
      TreeItem ti = appDialog.getHmAppts().get(targetApptId);
      if (ti.getParentItem() != null) 
        appt.setParentMenuId(((ApplicationTemplate)ti.getParentItem().getUserObject()).getTId());
    }
    
    // insert
    appDialog.insertItem(appt);
    
    super.onDrop(context);
  }

  @Override
  public void onEnter(DragContext context) {
    super.onEnter(context);
    positioner = newPositioner(context);
  }

  @Override
  public void onLeave(DragContext context) {
    positioner.removeFromParent();
    positioner = null;
    super.onLeave(context);
  }

  @Override
  public void onMove(DragContext context) {
    super.onMove(context);
    targetRow = DOMUtil.findIntersect(treeItemsAsIndexPanel, new CoordinateLocation(
        context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

    if (appDialog.getApptTree().getItemCount() > 0) {
      targetItem = getTreeWidget(targetRow == -1 ? 0 : targetRow);
      WidgetArea widgetArea = new WidgetArea(targetItem, null);
      subtree = (context.mouseY > widgetArea.getTop() + widgetArea.getHeight() / 4) 
          && (context.mouseY < widgetArea.getTop() + widgetArea.getHeight() - widgetArea.getHeight() / 4);
      Location widgetLocation = new WidgetLocation(targetItem, context.boundaryPanel);
      Location tableLocation = new WidgetLocation(appDialog.getApptTree(), context.boundaryPanel);
      if (subtree) {
        context.boundaryPanel.remove(positioner);
        targetItem.addStyleName(CSS_TABLE_POSITIONER1);
      }
      else {
        targetItem.removeStyleName(CSS_TABLE_POSITIONER1); 
        Widget nextWidget = getTreeWidget(targetRow+1);
        if (nextWidget != null) nextWidget.removeStyleName(CSS_TABLE_POSITIONER1);
        context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
            + (targetRow == -1 ? 0 : targetItem.getOffsetHeight()));
      }
    } 
  }

  Widget newPositioner(DragContext context) {
    Widget p = new SimplePanel();
    p.addStyleName(CSS_TABLE_POSITIONER);
    p.setPixelSize(appDialog.getApptTree().getOffsetWidth(), 1);
    return p;
  }
  
  public Widget getTreeWidget(int index) {
    WidgetCount widget = new WidgetCount();
    for (int i = 0; i < appDialog.getApptTree().getItemCount(); i++) {
      if (widget.getCount() == index) {
        widget.setWidget(appDialog.getApptTree().getItem(i).getWidget());
        break;
      }
      widget.countPlus();
      if (appDialog.getApptTree().getItem(i).getState())
        getTreeWidget(appDialog.getApptTree().getItem(i), index, widget);
      if (widget.getWidget() != null) break;
    }
    return widget.getWidget();
  }
  
  private void getTreeWidget(TreeItem item, int index, WidgetCount widget) {
    for (int i = 0; i < item.getChildCount(); i++) {
      if (widget.getCount() == index) {
        widget.setWidget(item.getChild(i).getWidget());
        break;
      }
      widget.countPlus();
      if (item.getChild(i).getState())
        getTreeWidget(item.getChild(i), index, widget);
      if (widget.getWidget() != null) break;
    }
  }

}
