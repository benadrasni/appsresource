package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class TemplateIntegerBoxWidget extends TemplateWidget implements HasBlurHandlers {
  
  private IntegerTextBox intTextBox;
  private String oldValue;

  public TemplateIntegerBoxWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setIntTextBox(new IntegerTextBox(widgetToHighlight, getObjectTemplate()));
    getIntTextBox().setWidth(ta.getWidth()+ta.getWidthUnit());
    getIntTextBox().setTabIndex(getTemplateAttribute().getTabIndex());
    if (isDisabled() || !getModel().getAppu().isWrite()) 
      getIntTextBox().getTextBox().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
    if (ta.isMandatory())
      getIntTextBox().getTextBox().addStyleDependentName(CSSConstants.SUFFIX_MANDATORY);

    getIntTextBox().getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
            if (!isInvalid()) {
              if (getIntTextBox().getText().trim().length() > 0) {
                Double valueDouble = new Double(ClientUtils
                    .parseInt(getIntTextBox().getText().trim()));
                if (getValue() == null) {
                  setValue(new AValue(getModel().getObject().getId(), 
                      getTemplateAttribute().getOaId(), valueDouble));
                  getValue().setRank(getRank());
                } else 
                  getValue().setValueDouble(valueDouble);
              } else if (getValue() != null)
                getValue().setValueDouble(null);
            }
          }
        });

    setOldValue("");
 
    addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (getModel().getObject() != null && !isInvalid() && isEdited()) {
          getModel().updateValue(getValue());
        }
      }
    });
    
    add(getIntTextBox());
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return getIntTextBox().getTextBox().addBlurHandler(handler);
  }

  @Override
  public boolean isEdited() {
    return getIntTextBox().isValid()
        && !getOldValue().equals(
            ClientUtils.parseInt(getIntTextBox().getText()));
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null
        || (getIntTextBox().getText().trim().length() == 0
            && getTemplateAttribute().isMandatory())
        || !getIntTextBox().isValid();
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);

    if (value != null && value.getRank() == getRank() 
        && value.getValueDouble() != null) {
      setInt(value.getValueDouble().doubleValue());
      setValue(value);
      if (value.getId() > 0)
        setOldValue(ClientUtils.parseInt(getIntTextBox().getDf().format(
            value.getValueDouble().doubleValue())));
    } else { 
      getIntTextBox().setText("");
      setOldValue("");
      setValue(null);
    }
    
  }

  @Override
  public void clearValue() {
    super.clearValue();
    getIntTextBox().setText("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateIntegerBoxWidget(getObjectTemplate(), getTemplateAttribute(), getRelId(), rank,
        getWidgetToHighlight(), false);
  }
  
  @Override
  public void setTabIndex(int index) {
    getIntTextBox().setTabIndex(index);
  }
  
  @Override
  public void setFocus() {
    getIntTextBox().getTextBox().getElement().focus();
  }

  // private methods
  
  private void setInt(double number) {
    if (getIntTextBox().isFocused())
      setParsed(getIntTextBox().getDf().format(number));
    else
      setFormatted(ClientUtils.parseInt(getIntTextBox().getDf().format(number)));
  }
  
  private void setParsed(String text) {
    getIntTextBox().setText(ClientUtils.parseInt(text.trim()));
  }
  
  private void setFormatted(String text) {
    if (text.trim().length() > 0)
      if (text.trim().length() == ClientUtils.parseInt(text.trim()).length())
        getIntTextBox().setText(
            getIntTextBox().getDf().format(
                new Double(ClientUtils.parseInt(text.trim()))));
      else {
        getIntTextBox().setValid(false);
        getIntTextBox().setText(text);
      }
    else {
      getIntTextBox().setValid(true);
      getIntTextBox().setText("");
    }
  }

  // getters and setters
  
  /**
   * @return the intTextBox
   */
  public IntegerTextBox getIntTextBox() {
    return intTextBox;
  }

  /**
   * @param intTextBox the intTextBox to set
   */
  public void setIntTextBox(IntegerTextBox intTextBox) {
    this.intTextBox = intTextBox;
  }

  /**
   * @return the oldValue
   */
  public String getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set
   */
  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }
}
