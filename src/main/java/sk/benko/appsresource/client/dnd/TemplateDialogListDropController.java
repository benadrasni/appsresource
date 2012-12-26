package sk.benko.appsresource.client.dnd;

import sk.benko.appsresource.client.designer.TemplateDialog;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.google.gwt.user.client.ui.Panel;

public class TemplateDialogListDropController extends AbstractPositioningDropController {

  private TemplateDialog templateDialog;

  public TemplateDialogListDropController(TemplateDialog templateDialog) {
    super(templateDialog.getListPanel());
    this.templateDialog = templateDialog;
  }

  @Override
  public void onDrop(DragContext context) {
    TemplateDialogDragController templateDragController = 
        (TemplateDialogDragController) context.dragController;
    final TemplateAttribute sourceTemplateAttribute = templateDragController.getDragItem();
    
    templateDialog.insertItemList(sourceTemplateAttribute);
    
    super.onDrop(context);
  }
}
