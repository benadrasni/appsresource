package sk.benko.appsresource.client.designer.layout;

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
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.CreateMenuView;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.DesignerModel;
import sk.benko.appsresource.client.model.Model;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button for new application component.
 *
 */
public class DesignerActionButtonsView extends FlowPanel implements Model.ViewObserver {
  
  private final FlowPanel createMenu;
  private Element element;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public DesignerActionButtonsView(final DesignerModel model) {
    model.addViewObserver(this);
    
    setStyleName("buttons_bar");

    add(new ButtonView(Main.constants.create(), 
        "inline-block button button-collapse-right", "80px",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            if (element != null) {
              if (element.getFirstChild().getNodeValue().equals(Main.constants.applications())) 
                new ApplicationDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.templates())) 
                new TemplateDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateGroups())) 
                new TemplateGroupDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateAttributes())) 
                new TemplateAttributeDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.templateRelations())) 
                new TemplateRelationDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectTypes())) 
                new ObjectTypeDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectAttributes())) 
                new ObjectAttributeDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.objectRelations())) 
                new ObjectRelationDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.valueTypes())) 
                new ValueTypeDialog(model, null);
              else if (element.getFirstChild().getNodeValue().equals(Main.constants.units())) 
                new UnitDialog(model, null);
            }
          }
    }));
    add(new ButtonView("▼", "inline-block button button-collapse-left", "", 
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            createMenu.setVisible(!createMenu.isVisible());
          }
    }));
    
    createMenu = new CreateMenuView(model);
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
