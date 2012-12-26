package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Widget;

public class IntegerTextBox extends TextPanel {
  private NumberFormat df;
  private boolean isFocused;
  
  public IntegerTextBox(Widget widgetToHighlight, Widget widgetToDisable) {
    super(widgetToHighlight, widgetToDisable);
    getTextBox().addStyleDependentName(CSSConstants.SUFFIX_ALIGNRIGHT);
    setDf(NumberFormat.getFormat(ClientUtils.INTFORMAT));

    getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (getText().trim().length() == ClientUtils.parseInt(
            getText().trim()).length()) {
          setValid(true);
          if (getWidgetToHighlight() != null)
            getWidgetToHighlight().removeStyleDependentName(CSSConstants.SUFFIX_HIGHLIGHTED);
        }
        else {
          setValid(false);
          if (getWidgetToHighlight() != null)
            getWidgetToHighlight().addStyleDependentName(CSSConstants.SUFFIX_HIGHLIGHTED);
        }
        if (getWidgetToDisable() != null)
          getWidgetToDisable().fireEvent(new ChangeEvent() {});
      }
    });
    
    getTextBox().addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        setFocused(true);
        if (isValid())
          setParsed(getText());
      }
    });
    
    getTextBox().addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (isValid())
          setFormatted(getText());
        setFocused(false);
      }
    });
  }

  public void setInt(double number) {
    if (isFocused()) 
      setParsed(df.format(number));
    else 
      setFormatted(ClientUtils.parseInt(df.format(number)));
  }
  
  private void setParsed(String text) {
    setText(ClientUtils.parseInt(text.trim()));
  }
  
  private void setFormatted(String text) {
    if (text.trim().length() > 0)
      if (text.trim().length() == ClientUtils.parseInt(text.trim()).length())
        setText(df.format(new Double(ClientUtils.parseInt(text.trim()))));
      else {
        setValid(false);
        setText(text);
      }
    else {
      setValid(true);
      setText("");
    }
  }
  
  // getters and setters

  /**
   * @return the df
   */
  public NumberFormat getDf() {
    return df;
  }

  /**
   * @param df the df to set
   */
  public void setDf(NumberFormat df) {
    this.df = df;
  }

  /**
   * @return the isFocused
   */
  public boolean isFocused() {
    return isFocused;
  }

  /**
   * @param isFocused the isFocused to set
   */
  public void setFocused(boolean isFocused) {
    this.isFocused = isFocused;
  }
}
