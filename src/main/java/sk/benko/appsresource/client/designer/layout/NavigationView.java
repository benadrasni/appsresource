package sk.benko.appsresource.client.designer.layout;

import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabelView;
import sk.benko.appsresource.client.model.DesignerModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class NavigationView extends FlowPanel {
  
  private DesignerContentView content;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public NavigationView(final DesignerModel model) {
    setStyleName("navigation-designer");
    content = new DesignerContentView(model);
    
    add(new NavigationLabelView(model, Main.constants.applications(), 
        createClickHandler(model, Sections.APPLICATION)));

    add(new NavigationLabelView(model, Main.constants.templates(), 
        createClickHandler(model, Sections.TEMPLATE)));

    add(new NavigationLabelView(model, Main.constants.templateGroups(), 
        createClickHandler(model, Sections.TEMPLATEGROUP)));

    add(new NavigationLabelView(model, Main.constants.templateAttributes(), 
        createClickHandler(model, Sections.TEMPLATEATTRIBUTE)));

    add(new NavigationLabelView(model, Main.constants.templateRelations(), 
        createClickHandler(model, Sections.TEMPLATERELATION)));

    add(new NavigationLabelView(model, Main.constants.objectTypes(), 
        createClickHandler(model, Sections.OBJECTTYPE)));

    add(new NavigationLabelView(model, Main.constants.objectAttributes(), 
        createClickHandler(model, Sections.OBJECTATTRIBUTE)));

    add(new NavigationLabelView(model, Main.constants.objectRelations(), 
        createClickHandler(model, Sections.OBJECTRELATION)));

    add(new NavigationLabelView(model, Main.constants.valueTypes(), 
        createClickHandler(model, Sections.VALUETYPE)));

    add(new NavigationLabelView(model, Main.constants.units(), 
        createClickHandler(model, Sections.UNIT)));
  }
  
  // private methods
  
  private ClickHandler createClickHandler(final DesignerModel model, 
      final Sections section) {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (content != null) ((FlowPanel) getParent().getParent()).remove(content);
        model.notifyNavigationItemClicked(event.getRelativeElement());
        content.initialize(section);
        ((FlowPanel) getParent().getParent()).add(content);
      }
    };
  }
}
