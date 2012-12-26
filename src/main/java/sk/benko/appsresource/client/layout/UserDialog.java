package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class UserDialog extends PopupPanel {
  final AbsolutePanel main = new AbsolutePanel();
  FlowPanel body = new FlowPanel();
  FlowPanel bodyTop = new FlowPanel();
  FlowPanel bodyBottom = new FlowPanel();

  private final Label logout = new Label(Main.constants.logout());
  private final Label name = new Label();
  private final Label email = new Label();

  private final Label designer = new Label(Main.constants.designerMode());

  public UserDialog() {
    setStyleName(ClientUtils.CSS_USERDIALOGBOX);
    setAutoHideEnabled(true);
    addCloseHandler(new CloseHandler<PopupPanel>() {
      @Override
      public void onClose(CloseEvent<PopupPanel> event) {
        designer.removeStyleName("navigation-item-hover");
      }
    });
    
    logout.getElement().setId("login-info-link");
    logout.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        logout.addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType()); 
    logout.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        logout.removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType()); 
    designer.setStyleName("user-dialog-link");
    designer.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        designer.addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType()); 
    designer.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        designer.removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType()); 

    bodyTop.setStyleName(ClientUtils.CSS_USERDIALOGBOX_TOP);
    bodyTop.add(name);
    bodyTop.add(email);

    bodyBottom.setStyleName(ClientUtils.CSS_USERDIALOGBOX_BOTTOM);
    bodyBottom.add(logout);
    
    body.add(bodyTop);
    body.add(bodyBottom);
    main.add(body);
    
    add(main);
    
    setWidth("290px");
    setHeight("190px");
    setPopupPositionAndShow(new PopupPanel.PositionCallback() { 
      public void setPosition(int offsetWidth, int offsetHeight) { 
              setPopupPosition(Window.getClientWidth() - 300, 29); 
          } 
    }); 
    hide();
  }
  
  public void setLogoutClickHandler(ClickHandler clickHandler) {
    logout.addClickHandler(clickHandler);
  }

  public void setDesignerClickHandler(ClickHandler clickHandler) {
    designer.addClickHandler(clickHandler);
  }

  public void addDesignerLink() {
    bodyTop.add(designer);
  }

  public void setName(String name) {
    this.name.setText(name);
  }

  public void setEmail(String email) {
    this.email.setText(email);
  }
}
