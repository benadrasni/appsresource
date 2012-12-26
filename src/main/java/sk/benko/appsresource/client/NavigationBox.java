package sk.benko.appsresource.client;

import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.ui.widget.theme.ThemeImage;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class NavigationBox extends FlowPanel {
  private FlowPanel outerbox;
  private FlowPanel innerbox;
  
  private FlexTable navtable;
  
  private Image start;
  private Image end;
  private Image backward;
  private Image forward;
  
  private ToolTipBox tooltipboxStart;
  private ToolTipBox tooltipboxForward;
  private ToolTipBox tooltipboxBackward;
  private ToolTipBox tooltipboxEnd;

  public NavigationBox() {
    setStyleName(ClientUtils.CSS_NAVIGATION_BOX);
    addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);

    outerbox = new FlowPanel();
    outerbox.setStyleName(ClientUtils.CSS_TOOLBAR_OUTERBOX);
    outerbox.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);
    
    innerbox = new FlowPanel();
    innerbox.setStyleName(ClientUtils.CSS_TOOLBAR_INNERBOX);
    innerbox.addStyleName(ClientUtils.CSS_TOOLBAR_INLINE);

    navtable = new FlexTable();
    
    start = new ThemeImage("start.png");
    start.setStyleName(ClientUtils.CSS_NAVIGATION_IMAGE);
    tooltipboxStart = new ToolTipBox(start, Main.constants.listNavStart());
    start.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        tooltipboxStart.show();
      }}, MouseOverEvent.getType()); 
    start.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        tooltipboxStart.hide();
      }}, MouseOutEvent.getType()); 
    navtable.setWidget(0, 0, start);
    navtable.getCellFormatter().setWidth(0, 0, "30px");

    backward = new ThemeImage("backward.png");
    backward.setStyleName(ClientUtils.CSS_NAVIGATION_IMAGE);
    tooltipboxBackward = new ToolTipBox(backward, Main.constants.listNavPrev());
    backward.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        tooltipboxBackward.show();
      }}, MouseOverEvent.getType()); 
    backward.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        tooltipboxBackward.hide();
      }}, MouseOutEvent.getType()); 
    navtable.setWidget(0, 1, backward);
    navtable.getCellFormatter().setWidth(0, 1, "30px");
    
    forward = new ThemeImage("forward.png");
    forward.setStyleName(ClientUtils.CSS_NAVIGATION_IMAGE);
    tooltipboxForward = new ToolTipBox(forward, Main.constants.listNavNext());
    forward.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        tooltipboxForward.show();
      }}, MouseOverEvent.getType()); 
    forward.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        tooltipboxForward.hide();
      }}, MouseOutEvent.getType()); 
    navtable.setWidget(0, 2, forward);
    navtable.getCellFormatter().setWidth(0, 2, "30px");
    
    end = new ThemeImage("end.png");
    end.setStyleName(ClientUtils.CSS_NAVIGATION_IMAGE);
    tooltipboxEnd = new ToolTipBox(end, Main.constants.listNavEnd());
    end.addDomHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        tooltipboxEnd.show();
      }}, MouseOverEvent.getType()); 
    end.addDomHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        tooltipboxEnd.hide();
      }}, MouseOutEvent.getType()); 
    navtable.setWidget(0, 3, end);
    navtable.getCellFormatter().setWidth(0, 3, "30px");
    
    innerbox.add(navtable);
    outerbox.add(innerbox);
    add(outerbox);
  }

  public void setStartClickHandler(ClickHandler clickHandler) {
    start.addClickHandler(clickHandler);
  }

  public void setBackwardClickHandler(ClickHandler clickHandler) {
    backward.addClickHandler(clickHandler);
  }

  public void setForwardClickHandler(ClickHandler clickHandler) {
    forward.addClickHandler(clickHandler);
  }

  public void setEndClickHandler(ClickHandler clickHandler) {
    end.addClickHandler(clickHandler);
  }
}
