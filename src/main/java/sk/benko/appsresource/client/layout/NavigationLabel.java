package sk.benko.appsresource.client.layout;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.designer.layout.DesignerView;
import sk.benko.appsresource.client.model.Model;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Label;


/**
 * A widget to display the button.
 *
 */
public class NavigationLabel extends Label implements Model.ViewObserver {

  /**
   * @param designerView      the top level view
   * @param clickHandler      the clickHandler
   */
  public NavigationLabel(final DesignerView designerView, ClickHandler clickHandler) {
    designerView.getDesignerModel().addViewObserver(this);

    setStyleName("navigation-item");

    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType());
    addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType());
    addDomHandler(clickHandler, ClickEvent.getType());
  }

  /**
   * @param designerView      the top level view
   * @param text              label text
   * @param clickHandler      the clickHandler
   */
  public NavigationLabel(final DesignerView designerView, String text, ClickHandler clickHandler) {
    this(designerView, clickHandler);
    setText(text);
  }

  /**
   * @param model             the top level view
   * @param text              label text
   * @param clickHandler      the clickHandler
   */
  public NavigationLabel(String text, ClickHandler clickHandler) {
    setStyleName("navigation-item");

    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType());
    addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType());
    addDomHandler(clickHandler, ClickEvent.getType());
    setText(text);
  }

  public void onMenuItemClicked() {
  }

  public void onNavigationItemClicked(Element element) {
    if (element == getElement()) 
      getElement().addClassName("navigation-item-selected");
    else
      getElement().removeClassName("navigation-item-selected");
  }

  public void onDialogNavigationItemClicked(Element element) {
    if (element == getElement()) 
      getElement().addClassName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
    else
      getElement().removeClassName(ClientUtils.CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED);
  }
}
