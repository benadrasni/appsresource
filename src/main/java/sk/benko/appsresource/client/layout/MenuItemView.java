package sk.benko.appsresource.client.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A widget to display the button.
 *
 */
public class MenuItemView extends FlowPanel {

  /**
   * @param text
   * @param clickHandler
   */
  public MenuItemView(String text, ClickHandler clickHandler) {
    final Element elem = getElement();
    elem.setClassName("menu-item");
    elem.setInnerText(text);

    addDomHandler(clickHandler, ClickEvent.getType()); 
    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        elem.addClassName("menu-item-hover");
      }}, MouseOverEvent.getType()); 
    addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        elem.removeClassName("menu-item-hover");
      }}, MouseOutEvent.getType()); 
  }
}
