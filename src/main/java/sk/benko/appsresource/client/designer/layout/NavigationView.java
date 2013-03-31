package sk.benko.appsresource.client.designer.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import sk.benko.appsresource.client.designer.Sections;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.layout.NavigationLabel;

/**
 * A widget to display the button.
 */
public class NavigationView extends FlowPanel {

  private DesignerView designerView;
  private DesignerContentView content;

  /**
   * @param designerView the top level view
   */
  public NavigationView(final DesignerView designerView) {
    setStyleName("navigation-designer");
    this.designerView = designerView;
    content = new DesignerContentView(designerView);

    add(new NavigationLabel(designerView, Main.constants.applications(),
        createClickHandler(Sections.APPLICATION)));

    add(new NavigationLabel(designerView, Main.constants.templates(),
        createClickHandler(Sections.TEMPLATE)));

    add(new NavigationLabel(designerView, Main.constants.templateGroups(),
        createClickHandler(Sections.TEMPLATEGROUP)));

    add(new NavigationLabel(designerView, Main.constants.templateAttributes(),
        createClickHandler(Sections.TEMPLATEATTRIBUTE)));

    add(new NavigationLabel(designerView, Main.constants.templateRelations(),
        createClickHandler(Sections.TEMPLATERELATION)));

    add(new NavigationLabel(designerView, Main.constants.objectTypes(),
        createClickHandler(Sections.OBJECTTYPE)));

    add(new NavigationLabel(designerView, Main.constants.objectAttributes(),
        createClickHandler(Sections.OBJECTATTRIBUTE)));

    add(new NavigationLabel(designerView, Main.constants.objectRelations(),
        createClickHandler(Sections.OBJECTRELATION)));

    add(new NavigationLabel(designerView, Main.constants.valueTypes(),
        createClickHandler(Sections.VALUETYPE)));

    add(new NavigationLabel(designerView, Main.constants.units(),
        createClickHandler(Sections.UNIT)));
  }

  // private methods

  private ClickHandler createClickHandler(final Sections section) {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (content != null) {
          designerView.remove(content);
        }
        designerView.getDesignerModel().notifyNavigationItemClicked(event.getRelativeElement());
        content.initialize(section);
        designerView.add(content);
      }
    };
  }
}
