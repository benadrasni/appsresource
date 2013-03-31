package sk.benko.appsresource.client.designer.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.CreateMenuView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.Model;

/**
 * A widget to display the button for new application component.
 */
public class DesignerActionButtonsView extends FlowPanel implements Model.ViewObserver {

  private final FlowPanel createMenu;
  private Element element;

  /**
   * @param designerView the top level view
   */
  public DesignerActionButtonsView(final DesignerView designerView) {
    designerView.getDesignerModel().addViewObserver(this);

    setStyleName("buttons_bar");

    add(new ButtonView(Main.constants.create(),
        "inline-block button button-collapse-right", "80px",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (element != null) {
              Main.status.showTaskStatus(Main.constants.loading());
              if (element.getFirstChild().getNodeValue().equals(Main.constants.applications())) {
                designerView.getApplicationDialog().setItem(null);
                designerView.getApplicationDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.templates())) {
                designerView.getTemplateDialog().setItem(null);
                designerView.getTemplateDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateGroups())) {
                designerView.getTemplateGroupDialog().setItem(null);
                designerView.getTemplateGroupDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateAttributes())) {
                designerView.getTemplateAttributeDialog().setItem(null);
                designerView.getTemplateAttributeDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateRelations())) {
                designerView.getTemplateRelationDialog().setItem(null);
                designerView.getTemplateRelationDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectTypes())) {
                designerView.getObjectTypeDialog().setItem(null);
                designerView.getObjectTypeDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectAttributes())) {
                designerView.getObjectAttributeDialog().setItem(null);
                designerView.getObjectAttributeDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectRelations())) {
                designerView.getObjectRelationDialog().setItem(null);
                designerView.getObjectRelationDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.valueTypes())) {
                designerView.getValueTypeDialog().setItem(null);
                designerView.getValueTypeDialog().show();
              } else if (element.getFirstChild().getNodeValue().equals(Main.constants.units())) {
                designerView.getUnitDialog().setItem(null);
                designerView.getUnitDialog().show();
              }
            }
          }
        }));
    add(new ButtonView("▼", "inline-block button button-collapse-left", "",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            createMenu.setVisible(!createMenu.isVisible());
          }
        }));

    createMenu = new CreateMenuView(designerView);
    createMenu.setVisible(false);
    add(createMenu);

  }

  @Override
  public void onMenuItemClicked() {
    createMenu.setVisible(false);
  }

  @Override
  public void onNavigationItemClicked(Element element) {
    this.element = element;
  }

  @Override
  public void onDialogNavigationItemClicked(Element element) {
  }

}
