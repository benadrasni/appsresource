package sk.benko.appsresource.client.layout;

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
public class NavigationLabelView extends Label implements Model.ViewObserver {
  
  private ClickHandler clickHandler;
  
  /**
   * @param model
   *          the model to which the Ui will bind itself
   */
  public NavigationLabelView(Model model, String text, ClickHandler clickHandler) {
    model.addViewObserver(this);
    
    setStyleName("navigation-item");
    setText(text);
    setClickHandler(clickHandler);
    addDomHandler(getClickHandler(), ClickEvent.getType()); 

    addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        addStyleName("navigation-item-hover");
      }}, MouseOverEvent.getType()); 
    addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        removeStyleName("navigation-item-hover");
      }}, MouseOutEvent.getType()); 
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
      getElement().addClassName("dialog-box-navigation-item-selected");
    else
      getElement().removeClassName("dialog-box-navigation-item-selected");
  }

  /**
   * @return the clickHandler
   */
  public ClickHandler getClickHandler() {
    return clickHandler;
  }

  /**
   * @param clickHandler the clickHandler to set
   */
  public void setClickHandler(ClickHandler clickHandler) {
    this.clickHandler = clickHandler;
  }
}
