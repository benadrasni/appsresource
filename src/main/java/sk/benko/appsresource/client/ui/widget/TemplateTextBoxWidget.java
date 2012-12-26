package sk.benko.appsresource.client.ui.widget;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class TemplateTextBoxWidget extends TemplateWidget implements HasBlurHandlers {
  
  private TextPanel appTextBox;
  private String oldValue;

  public TemplateTextBoxWidget(ObjectTemplate objectTemplate, TemplateAttribute ta, 
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setAppTextBox(new TextPanel(widgetToHighlight, getObjectTemplate()));
    getAppTextBox().setWidth(ta.getWidth()+ta.getWidthUnit());
    getAppTextBox().setTabIndex(getTemplateAttribute().getTabIndex());
    if (isDisabled() || !getModel().getAppu().isWrite())
      getAppTextBox().getTextBox().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
    if (ta.isMandatory())
      getAppTextBox().getTextBox().addStyleDependentName(CSSConstants.SUFFIX_MANDATORY);

    getAppTextBox().getTextBox().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (!isInvalid()) {
          if (getAppTextBox().getText().trim().length() > 0) {
            if (getValue() == null || getValue().getLangId() != Main.language) {
              AValue value = new AValue(getModel().getObject().getId(),
                  getTemplateAttribute().getOaId(), getAppTextBox().getText()
                  .trim(), Main.language);
              if (getValue() != null) 
                value.setId(getValue().getId());
              value.setRank(getRank());
              setValue(value);
            } else 
              getValue().setValueString(getAppTextBox().getText().trim());
          } else if (getValue() != null)
            getValue().setValueString(null);
        }
      }
    });

    addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        if (getModel().getObject() != null && !isInvalid() && isEdited()) {
          getModel().updateValue(getValue());
        } 
      }
    });
    
    add(getAppTextBox());
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return getAppTextBox().getTextBox().addBlurHandler(handler);
  }

  @Override
  public boolean isEdited() {
    return !getOldValue().equals(getAppTextBox().getTextBox().getText().trim());
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null ||
        (getAppTextBox().getText().trim().length() == 0
        && getTemplateAttribute().isMandatory());
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);
    if (value != null && value.getRank() == getRank() 
        && value.getValueString() != null) {
      getAppTextBox().setText(value.getValueString());
      setValue(value);
      if (value.getId() > 0)
        setOldValue(value.getValueString());
    } else { 
      setOldValue("");
      clearValue();
    }
    
  }

  @Override
  public void clearValue() {
    super.clearValue();
    getAppTextBox().setText("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateTextBoxWidget(getObjectTemplate(), getTemplateAttribute(), getRelId(), rank,
        getWidgetToHighlight(), false);
  }

  @Override
  public void setTabIndex(int index) {
    getAppTextBox().setTabIndex(index);
  }

  @Override
  public void setFocus() {
    getAppTextBox().getTextBox().getElement().focus();
  }

  // getters and setters
  
  /**
   * @return the appTextBox
   */
  public TextPanel getAppTextBox() {
    return appTextBox;
  }

  /**
   * @param appTextBox the appTextBox to set
   */
  public void setAppTextBox(TextPanel appTextBox) {
    this.appTextBox = appTextBox;
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
