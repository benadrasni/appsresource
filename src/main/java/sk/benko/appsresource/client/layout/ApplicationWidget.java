package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.model.Application;
import sk.benko.appsresource.client.model.ApplicationModel;
import sk.benko.appsresource.client.model.ApplicationUser;
import sk.benko.appsresource.client.model.UserModel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget for displaying a single {@link Application}.
 */
public class ApplicationWidget {
  private PickupDragController appDragController;
  private final ApplicationUser appu;
  private final Application app;

  private int zIndex = 1;

  private final DivElement titleElement = Document.get().createDivElement();
  private final Label title = new Label();
  private final Label content = new Label();

  /**
   * @param appDragController
   * @param appu
   */
  public ApplicationWidget(PickupDragController appDragController, 
      ApplicationUser appu) {
    this.app = appu.getApp();
    this.appu = appu;
    this.appDragController = appDragController; 
    
    title.setText(app.getName());
    final String appDesc = app.getDesc();
    content.setText((appDesc == null) ? "" : appDesc);
  }

  public ApplicationWidget(Application app) {
    this.app = app;
    this.appu = null;
    this.appDragController = null; 
    
    titleElement.setInnerHTML(app.getName());
    final String appDesc = app.getDesc();
    content.setText((appDesc == null) ? "" : appDesc);
  }

  public HTML getApplicationAsHTML(final UserModel umodel) {
    HTML widget = new HTML();
    
    widget.setStyleName("application");
    widget.getElement().setAttribute("id", ""+appu.getId());

    title.setStyleName("application-title");
    widget.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        invokeApp(umodel);
      }
    });
    widget.getElement().appendChild(title.getElement());
    content.setStyleName("application-content");
    widget.getElement().appendChild(content.getElement());
    widget.getElement().getStyle().setProperty("zIndex", "" + nextZIndex());
    
    setPixelPosition(widget, appu.getLeft(), appu.getTop());
    widget.setPixelSize(appu.getWidth(), appu.getHeight());
    content.setWidth((appu.getWidth()-15)+"px");
    content.setHeight((appu.getHeight()-50)+"px");

    appDragController.makeDraggable(widget);
    return widget;
  }

  public SimplePanel getApplicationAsSimplePanel() {
    final SimplePanel widget = new SimplePanel();
    
    widget.setStyleName("application-sub");
    widget.getElement().setAttribute("id", ""+app.getId());

    final Element elem = widget.getElement();
    elem.appendChild(titleElement);
    titleElement.setClassName("application-sub-title");
    content.setStyleName("application-content");
    content.setHeight(((content.getText().length() / 20)+1)*20+"px");

    widget.setWidget(content);

    return widget;
  }

  private void invokeApp(UserModel umodel) {
    final RootPanel root = RootPanel.get();
    Document.get().getElementById("appcontent").removeFromParent();
    ApplicationView av = new ApplicationView(new ApplicationModel(umodel, appu));
    av.initialize();
    root.add(av);
  }
  
  private void setPixelPosition(Widget widget, int left, int top) {
    final Style style = widget.getElement().getStyle();
    style.setPropertyPx("left", left);
    style.setPropertyPx("top", top);
  }

  private int nextZIndex() {
    return zIndex++;
  }
}
