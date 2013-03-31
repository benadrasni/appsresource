package sk.benko.appsresource.client.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import sk.benko.appsresource.client.designer.layout.DesignerView;

/**
 * A widget to display the button.
 */
public class CreateMenuView extends FlowPanel {

  /**
   * @param designerView the top level view
   */
  public CreateMenuView(final DesignerView designerView) {
    setStyleName("menu-designer");

    add(new MenuItemView(Main.constants.application(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        Main.status.showTaskStatus(Main.constants.loading());
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getApplicationDialog().setItem(null);
        designerView.getApplicationDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.template(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getTemplateDialog().setItem(null);
        designerView.getTemplateDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.templateGroup(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getTemplateGroupDialog().setItem(null);
        designerView.getTemplateGroupDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.templateAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getTemplateAttributeDialog().setItem(null);
        designerView.getTemplateAttributeDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.templateRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getTemplateRelationDialog().setItem(null);
        designerView.getTemplateRelationDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.objectType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getObjectTypeDialog().setItem(null);
        designerView.getObjectTypeDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.objectAttribute(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getObjectAttributeDialog().setItem(null);
        designerView.getObjectAttributeDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.objectRelation(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getObjectRelationDialog().setItem(null);
        designerView.getObjectRelationDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.valueType(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getValueTypeDialog().setItem(null);
        designerView.getValueTypeDialog().show();
      }
    }));

    add(new MenuItemView(Main.constants.unit(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        designerView.getDesignerModel().notifyMenuItemClicked();
        designerView.getUnitDialog().setItem(null);
        designerView.getUnitDialog().show();
      }
    }));
  }
}
