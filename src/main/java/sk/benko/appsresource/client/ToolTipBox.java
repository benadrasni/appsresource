package sk.benko.appsresource.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolTipBox extends FlowPanel {
  Widget parent;
  FlowPanel triangle;
  Label lblTooltip;

  public ToolTipBox(Widget parent, String tooltip) {
    this.parent = parent; 
    setStyleName(ClientUtils.CSS_TOOLTIP);
    addStyleName(ClientUtils.CSS_TOOLTIP_HIDE);

    triangle = new FlowPanel();
    triangle.setStyleName("tooltip-triangle");
    triangle.addStyleName(ClientUtils.CSS_TOOLTIP_HIDE);
    
    lblTooltip = new Label(tooltip); 
    add(lblTooltip);  
  }
  
  public void show() {
    removeStyleName(ClientUtils.CSS_TOOLTIP_HIDE);
    triangle.removeStyleName(ClientUtils.CSS_TOOLTIP_HIDE);

    int x = parent.getAbsoluteLeft()+parent.getOffsetWidth()/2-10;
    int y = parent.getAbsoluteTop()+parent.getOffsetHeight();
    RootPanel root = RootPanel.get();
    root.add(this,x-lblTooltip.getText().length()*3, y+2);
    root.add(triangle, x, y-3);
  }
  
  public void hide() {
    addStyleName(ClientUtils.CSS_TOOLTIP_HIDE);
    triangle.addStyleName(ClientUtils.CSS_TOOLTIP_HIDE);

    RootPanel root = RootPanel.get();
    root.remove(this);
    root.remove(triangle);
  }
  
}
