package sk.benko.appsresource.client.designer;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.layout.ButtonView;
import sk.benko.appsresource.client.layout.Main;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A generic widget to display dialog.
 *
 */
public abstract class CommonDialog extends PopupPanel {

  /** layout */
  private AbsolutePanel main;
  private Label x;
  private FlowPanel header;
  private FlowPanel body;
  private FlowPanel bodyLeft;
  private FlowPanel bodyRight;
  private FlowPanel bodyBottom;
  private FlowPanel bodyButtons;
  private ButtonView bOk;
  private ButtonView bCancel; 

  /**
   * 
   */
  public CommonDialog() {
    setStyleName(ClientUtils.CSS_DIALOGBOX);

    // Enable glass background.
    setGlassStyleName(ClientUtils.CSS_DIALOGBOX_GLASS);
    setGlassEnabled(true);
    
    add(getMain());
    
    setPopupPositionAndShow(new PopupPanel.PositionCallback() { 
      public void setPosition(int offsetWidth, int offsetHeight) { 
              int left = ((Window.getClientWidth() - offsetWidth) / 2);
              int top = ((Window.getClientHeight() - offsetHeight) / 2);
              setPopupPosition(left, top); 
          } 
    }); 
  }
  
  public void close() {
    hide();
  }
  
  /**
   * Getter for property 'main'.
   * 
   * @return Value for property 'main'.
   */
  protected AbsolutePanel getMain() {
    if (main == null) {
      main = new AbsolutePanel();
      main.add(getHeader());
      main.add(getX());
      main.add(getBody());
    }
    return main;
  }
  

  /**
   * Getter for property 'x'.
   * 
   * @return Value for property 'x'.
   */
  protected Label getX() {
    if (x == null) {
      x = new Label(ClientUtils.CLOSE_CHAR);
      x.setStyleName(ClientUtils.CSS_DIALOGBOX_X);
      x.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          close();
        }
      });
    }

    return x;
  }

  /**
   * Getter for property 'header'.
   * 
   * @return Value for property 'header'.
   */
  protected FlowPanel getHeader() {
    if (header == null) {
      header = new FlowPanel();
      header.setStyleName(ClientUtils.CSS_DIALOGBOX_HEADER);
    }

    return header;
  }

  /**
   * Getter for property 'body'.
   * 
   * @return Value for property 'body'.
   */
  protected FlowPanel getBody() {
    if (body == null) {
      body = new FlowPanel();
      body.setStyleName(ClientUtils.CSS_DIALOGBOX_BODY);
      
      int width = Window.getClientWidth()*2 / 3;
      int height = Window.getClientHeight()*2 / 3;
      body.setHeight(height + WidthUnitListBox.WIDTH_UNITPX);
      body.setWidth(width + WidthUnitListBox.WIDTH_UNITPX);
      
      body.add(getBodyLeft());
      body.add(getBodyRight());
      body.add(getBodyBottom());

    }

    return body;
  }
  
  /**
   * Getter for property 'bodyLeft'.
   * 
   * @return Value for property 'bodyLeft'.
   */
  protected FlowPanel getBodyLeft() {
    if (bodyLeft == null) {
      bodyLeft = new FlowPanel();
      bodyLeft.setStyleName(ClientUtils.CSS_DIALOGBOX_BODYLEFT);
    }

    return bodyLeft;
  }
  
  /**
   * Getter for property 'bodyRight'.
   * 
   * @return Value for property 'bodyRight'.
   */
  protected FlowPanel getBodyRight() {
    if (bodyRight == null) {
      bodyRight = new FlowPanel();
      bodyRight.setStyleName(ClientUtils.CSS_DIALOGBOX_BODYRIGHT);
    }

    return bodyRight;
  }
  
  /**
   * Getter for property 'bodyBottom'.
   * 
   * @return Value for property 'bodyBottom'.
   */
  protected FlowPanel getBodyBottom() {
    if (bodyBottom == null) {
      bodyBottom = new FlowPanel();
      bodyBottom.setStyleName(ClientUtils.CSS_DIALOGBOX_BODYBOTTOM);
      bodyBottom.add(getBodyButtons());
    }

    return bodyBottom;
  }
  
  /**
   * Getter for property 'bodyButtons'.
   * 
   * @return Value for property 'bodyButtons'.
   */
  protected FlowPanel getBodyButtons() {
    if (bodyButtons == null) {
      bodyButtons = new FlowPanel();
      bodyButtons.setStyleName(ClientUtils.CSS_DIALOGBOX_BODYBUTTONS);
      bodyButtons.add(getBCancel());
      bodyButtons.add(getBOk());

    }

    return bodyButtons;
  }
  
  /**
   * Getter for property 'bOk'.
   * 
   * @return Value for property 'bOk'.
   */
  protected ButtonView getBOk() {
    if (bOk == null) {
      bOk = new ButtonView(ClientUtils.CSS_BUTTON + " " 
          + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONOK);
    }

    return bOk;
  }
  
  /**
   * Getter for property 'bCancel'.
   * 
   * @return Value for property 'bCancel'.
   */
  protected ButtonView getBCancel() {
    if (bCancel == null) {
      bCancel = new ButtonView(ClientUtils.CSS_BUTTON + " " 
          + ClientUtils.CSS_DIALOG_BUTTON + " " + ClientUtils.CSS_DIALOG_BUTTONCANCEL);
      bCancel.addDomHandler(
          new ClickHandler() {
            public void onClick(ClickEvent event) {
              close();
            }
          }, ClickEvent.getType());
      bCancel.getElement().setInnerText(Main.constants.cancel());
    }

    return bCancel;
  }
}
