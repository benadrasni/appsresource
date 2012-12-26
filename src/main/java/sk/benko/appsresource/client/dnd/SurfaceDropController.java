package sk.benko.appsresource.client.dnd;

import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.UserModel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * A {@link DropController} which constrains the placement of draggable widgets the grid specified
 * in the constructor.
 */
public class SurfaceDropController extends AbsolutePositionDropController {

  private UserModel model;
  private AbsolutePanel dropTarget;

  public SurfaceDropController(AbsolutePanel dropTarget, UserModel model) {
    super(dropTarget);
    this.model = model;
    this.dropTarget = dropTarget;
  }

  @Override
  public void onDrop(DragContext context) {
    int appuId = Integer.parseInt(context.draggable.getElement().getAttribute("id"));
    ApplicationUser appu = model.getApplicationUser(appuId);
    appu.setTop(Math.max(0, context.desiredDraggableY-dropTarget.getAbsoluteTop()));
    appu.setLeft(Math.max(0, context.desiredDraggableX-dropTarget.getAbsoluteLeft()));
    model.createOrUpdateApplicationUser(appu);
    super.onDrop(context);
  }
  
  @Override
  public void onMove(DragContext context) {
    super.onMove(context);
  }
}