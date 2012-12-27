package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.util.ThemeImage;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class ButtonView extends FlowPanel {

  final Element elem;

  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public ButtonView(String styleName) {
    elem = getElement();
    elem.setClassName(styleName);

    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        elem.addClassName("button-hover");
      }}, MouseOverEvent.getType()); 
    addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        elem.removeClassName("button-hover");
      }}, MouseOutEvent.getType()); 
  }

  public ButtonView(String text, String styleName, String width) {
    this(styleName);
    setWidth(width);
    elem.setInnerText(text);
  }

  public ButtonView(String text, String styleName, String width, 
      ClickHandler clickHandler) {
    this(text, styleName, width);
    addClickHandler(clickHandler);
  }

  public ButtonView(String styleName, String width, ThemeImage image) {
    this(styleName);
    setWidth(width);
    add(image);
  }

  public ButtonView(String styleName, String width, ThemeImage image, 
      ClickHandler clickHandler) {
    this(styleName, width, image);
    addClickHandler(clickHandler);
  }
  
  public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
    return addDomHandler(clickHandler, ClickEvent.getType());
  }

}
