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

public class DoubleTextBox extends TextPanel {
  private NumberFormat df;
  private boolean isFocused;
  
  public DoubleTextBox(Widget widgetToHighlight, Widget widgetToDisable) {
    super(widgetToHighlight, widgetToDisable);
    getTextBox().addStyleDependentName(CSSConstants.SUFFIX_ALIGNRIGHT);
    setDf(NumberFormat.getFormat(ClientUtils.NUMBERFORMAT));
    
    getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (getText().trim().length() == ClientUtils.parseDouble(
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
  
  public void addHandlers() {
  }
  
  public void setDouble(double number) {
    if (isFocused()) 
      setParsed(getDf().format(number));
    else 
      setFormatted(ClientUtils.parseDouble(getDf().format(number)));
  }


  private void setParsed(String text) {
    getTextBox().setText(ClientUtils.parseDouble(text.trim()));
  }
  
  private void setFormatted(String text) {
    if (text.trim().length() > 0)
      if (text.trim().length() == ClientUtils.parseDouble(text.trim()).length())
        setText(getDf().format(new Double(ClientUtils.parseDouble(text.trim()))));
      else {
        setValid(false);
        setText(text.trim());
      }
    else {
      setValid(true);
      setText("");
    }
  }

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
