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
   * @param model
   *          the model to which the Ui will bind itself
   */
  public CreateMenuView(final DesignerModel dmodel) {
    setStyleName("menu-designer");

    add(new MenuItemView(Main.constants.application(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new ApplicationDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.template(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new TemplateDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateGroup(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new TemplateGroupDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new TemplateAttributeDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.templateRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new TemplateRelationDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.objectType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new ObjectTypeDialog(dmodel, null);
      }
    }));
    
    add(new MenuItemView(Main.constants.objectAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new ObjectAttributeDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.objectRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new ObjectRelationDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.valueType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new ValueTypeDialog(dmodel, null);
      }
    }));

    add(new MenuItemView(Main.constants.unit(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        dmodel.notifyMenuItemClicked();
        new UnitDialog(dmodel, null);
      }
    }));
  }
}
