package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TextPanel extends SimplePanel {
  private TextBox textBox;
  private boolean isValid;
  
  private Widget widgetToHighlight;
  private Widget widgetToDisable;
  
  public TextPanel(Widget widgetToHighlight, Widget widgetToDisable) {
    setStyleName(CSSConstants.CSS_PANEL_TEXTBOX);
    setTextBox(new TextBox());
    getTextBox().setStyleName(CSSConstants.CSS_TEXTBOX);
    setValid(true);
    setWidgetToHighlight(widgetToHighlight);
    setWidgetToDisable(widgetToDisable);
    
    getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        setValid(true);
        if (getWidgetToDisable() != null)
          getWidgetToDisable().fireEvent(new ChangeEvent() {});
      }
    });

    add(getTextBox());
  }

  // TextBox methods

  public void setText(String text) {
    getTextBox().setText(text);
  }

  public String getText() {
    return getTextBox().getText();
  }

  public void setReadOnly(boolean isReadOnly) {
    getTextBox().setReadOnly(isReadOnly);
  }
  
  public int getMaxLength() {
    return getTextBox().getMaxLength();
  }
  
  public void setMaxLength(int length) {
    getTextBox().setMaxLength(length);
  }
  
  public void setEnabled(boolean enabled) {
    getTextBox().setEnabled(enabled);
  }

  public void setTabIndex(int index) {
    getTextBox().setTabIndex(index);
  }

  // getters and setters
  
  /**
   * @return the textBox
   */
  public TextBox getTextBox() {
    return textBox;
  }

  /**
   * @param tb the text box to set
   */
  public void setTextBox(TextBox tb) {
    this.textBox = tb;
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
