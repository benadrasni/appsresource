package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.designer.ApplicationDialog;
import sk.benko.appsresource.client.designer.ObjectAttributeDialog;
import sk.benko.appsresource.client.designer.ObjectRelationDialog;
import sk.benko.appsresource.client.designer.ObjectTypeDialog;
import sk.benko.appsresource.client.designer.TemplateAttributeDialog;
import sk.benko.appsresource.client.designer.TemplateDialog;
import sk.benko.appsresource.client.designer.TemplateGroupDialog;
import sk.benko.appsresource.client.designer.TemplateRelationDialog;
import sk.benko.appsresource.client.designer.UnitDialog;
import sk.benko.appsresource.client.designer.ValueTypeDialog;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class CreateMenuView extends FlowPanel {

  /**
   * @param dModel
   *          the model to which the Ui will bind itself
   */
  public CreateMenuView(final DesignerModel dModel) {
    setStyleName("menu-designer");

    add(new MenuItemView(Main.constants.application(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new ApplicationDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.template(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new TemplateDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateGroup(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new TemplateGroupDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new TemplateAttributeDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new TemplateRelationDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.objectType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new ObjectTypeDialog(dModel, null);
      }
    }));
    
    add(new MenuItemView(Main.constants.objectAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new ObjectAttributeDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.objectRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new ObjectRelationDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.valueType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new ValueTypeDialog(dModel, null);
      }
    }));

    add(new MenuItemView(Main.constants.unit(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dModel.notifyMenuItemClicked();
        new UnitDialog(dModel, null);
      }
    }));
  }
}
