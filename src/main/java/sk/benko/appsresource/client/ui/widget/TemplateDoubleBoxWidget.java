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

public class TemplateDoubleBoxWidget extends TemplateWidget implements HasBlurHandlers {
  
  private DoubleTextBox doubleTextBox;
  private String oldValue;

  public TemplateDoubleBoxWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setDoubleTextBox(new DoubleTextBox(widgetToHighlight, getObjectTemplate()));
    getDoubleTextBox().setWidth(ta.getWidth()+ta.getWidthUnit());
    getDoubleTextBox().setTabIndex(getTemplateAttribute().getTabIndex());
    if (isDisabled() || !getModel().getAppu().isWrite()) 
      getDoubleTextBox().getTextBox() .addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
    if (ta.isMandatory())
      getDoubleTextBox().getTextBox().addStyleDependentName(CSSConstants.SUFFIX_MANDATORY);

    getDoubleTextBox().getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (!isInvalid()) {
          if (getDoubleTextBox().getText().trim().length() > 0) {
            Double valueDouble = new Double(ClientUtils
                .parseDouble(getDoubleTextBox().getText().trim()));
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
    
    add(getDoubleTextBox());
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return getDoubleTextBox().getTextBox().addBlurHandler(handler);
  }

  @Override
  public boolean isEdited() {
    return getDoubleTextBox().isValid() 
        && !getOldValue().equals(ClientUtils.parseDouble(
            getDoubleTextBox().getText()));
  }
  
  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null
        || (getDoubleTextBox().getText().trim().length() == 0
          && getTemplateAttribute().isMandatory())
        && !getDoubleTextBox().isValid();
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);

    if (value != null && value.getRank() == getRank() 
        && value.getValueDouble() != null) {
      setDouble(value.getValueDouble());
      setValue(value);
      if (value.getId() > 0)
        setOldValue(ClientUtils.parseDouble(getDoubleTextBox().getDf().format(
            value.getValueDouble().doubleValue())));
    } else { 
      getDoubleTextBox().setText("");
      setOldValue("");
      setValue(null);
    }
  }

  @Override
  public void clearValue() {
    super.clearValue();
    getDoubleTextBox().setText("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateDoubleBoxWidget(getObjectTemplate(), getTemplateAttribute(), getRelId(), rank,
        getWidgetToHighlight(), false);
  }

  @Override
  public void setTabIndex(int index) {
    getDoubleTextBox().setTabIndex(index);
  }

  @Override
  public void setFocus() {
    getDoubleTextBox().getTextBox().getElement().focus();
  }

  // private methods

  private void setDouble(double number) {
    if (getDoubleTextBox().isFocused()) 
      setParsed(getDoubleTextBox().getDf().format(number));
    else 
      setFormatted(ClientUtils.parseDouble(getDoubleTextBox().getDf().format(number)));
  }
  
  private void setParsed(String text) {
    getDoubleTextBox().setText(ClientUtils.parseDouble(text.trim()));
  }
  
  private void setFormatted(String text) {
    if (text.trim().length() > 0)
      if (text.trim().length() == ClientUtils.parseDouble(text.trim()).length())
        getDoubleTextBox().setText(
            getDoubleTextBox().getDf().format(
                new Double(ClientUtils.parseDouble(text.trim()))));
      else {
        getDoubleTextBox().setValid(false);
        getDoubleTextBox().setText(text);
      }
    else {
      getDoubleTextBox().setValid(true);
      getDoubleTextBox().setText("");
    }
  }

  // getters and setters
  
  /**
   * @return the doubleTextBox
   */
  public DoubleTextBox getDoubleTextBox() {
    return doubleTextBox;
  }

  /**
   * @param doubleTextBox the doubleTextBox to set
   */
  public void setDoubleTextBox(DoubleTextBox doubleTextBox) {
    this.doubleTextBox = doubleTextBox;
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
