package sk.benko.appsresource.client.dnd;

import sk.benko.appsresource.client.designer.TemplateRowView;
import sk.benko.appsresource.client.model.Template;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationDialogDragController extends PickupDragController {
  private Tree draggableTree;
  private Template dragItem;

  public ApplicationDialogDragController(AbsolutePanel boundaryPanel) {
    super(boundaryPanel, false);
    setBehaviorDragProxy(true);
    setBehaviorMultipleSelection(false);
  }

  @Override
  public void dragEnd() {
    super.dragEnd();

    // cleanup
    draggableTree = null;
  }

  @Override
  public void setBehaviorDragProxy(boolean dragProxyEnabled) {
    if (!dragProxyEnabled) {
      throw new IllegalArgumentException();
    }
    super.setBehaviorDragProxy(dragProxyEnabled);
  }

  @Override
  protected BoundaryDropController newBoundaryDropController(
      AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
    if (allowDroppingOnBoundaryPanel) {
      throw new IllegalArgumentException();
    }
    return super.newBoundaryDropController(boundaryPanel, 
        allowDroppingOnBoundaryPanel);
  }

  @Override
  protected Widget newDragProxy(DragContext context) {
    draggableTree = (Tree) context.draggable.getParent().getParent();
    dragItem = ((TemplateRowView)context.draggable.getParent()).getTemplate();
    return new Label(dragItem.getName());
  }

  Tree getDraggableTree() {
    return draggableTree;
  }

  Template getDragItem() {
    return dragItem;
  }
}
