package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TextAreaPanel extends SimplePanel {
  private TextArea textArea;
  private boolean isValid;
  
  private Widget widgetToHighlight;
  private Widget widgetToDisable;
  
  public TextAreaPanel(Widget widgetToHighlight, Widget widgetToDisable) {
    setStyleName(CSSConstants.CSS_PANEL_TEXTAREA);
    setTextArea(new TextArea());
    getTextArea().setStyleName(CSSConstants.CSS_TEXTAREA);
    setValid(true);
    setWidgetToHighlight(widgetToHighlight);
    setWidgetToDisable(widgetToDisable);
    
    getTextArea().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        setValid(true);
        if (getWidgetToDisable() != null)
          getWidgetToDisable().fireEvent(new ChangeEvent() {});
      }
    });

    add(getTextArea());
  }

  // TextBox methods

  public void setText(String text) {
    getTextArea().setText(text);
  }

  public String getText() {
    return getTextArea().getText();
  }

  public void setReadOnly(boolean isReadOnly) {
    getTextArea().setReadOnly(isReadOnly);
  }
  
  public void setEnabled(boolean enabled) {
    getTextArea().setEnabled(enabled);
  }

  public void setTabIndex(int index) {
    getTextArea().setTabIndex(index);
  }

  // getters and setters
  
  /**
   * @return the textArea
   */
  public TextArea getTextArea() {
    return textArea;
  }

  /**
   * @param ta the text area to set
   */
  public void setTextArea(TextArea ta) {
    this.textArea = ta;
  }

  /**
   * @return the isInvalid
   */
  public boolean isValid() {
    return isValid;
  }

  /**
   * @param isValid the isInvalid to set
   */
  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }

  /**
   * @return the widgetToHighlight
   */
  public Widget getWidgetToHighlight() {
    return widgetToHighlight;
  }

  /**
   * @param widgetToHighlight the widgetToHighlight to set
   */
  public void setWidgetToHighlight(Widget widgetToHighlight) {
    this.widgetToHighlight = widgetToHighlight;
  }

  /**
   * @return the widgetToDisable
   */
  public Widget getWidgetToDisable() {
    return widgetToDisable;
  }

  /**
   * @param widgetToDisable the widgetToDisable to set
   */
  public void setWidgetToDisable(Widget widgetToDisable) {
    this.widgetToDisable = widgetToDisable;
  }
  
  
}
