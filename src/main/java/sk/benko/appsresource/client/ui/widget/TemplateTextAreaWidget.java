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

public class TemplateTextAreaWidget extends TemplateWidget 
    implements HasBlurHandlers {
  
  private TextAreaPanel appTextArea;
  private String oldValue;

  public TemplateTextAreaWidget(ObjectTemplate objectTemplate, TemplateAttribute ta, 
      int relId, int rank, Widget widgetToHighlight, boolean isDisabled) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setAppTextArea(new TextAreaPanel(widgetToHighlight, getObjectTemplate()));
    getAppTextArea().setWidth(ta.getWidth()+ta.getWidthUnit());
    getAppTextArea().setHeight(getTemplateAttribute().getLength()
        + getTemplateAttribute().getWidthUnit());
    getAppTextArea().setTabIndex(getTemplateAttribute().getTabIndex());
    if (isDisabled() || !getModel().getAppu().isWrite())
      getAppTextArea().getTextArea().addStyleDependentName(CSSConstants.SUFFIX_DISABLED);
    if (ta.isMandatory())
      getAppTextArea().getTextArea().addStyleDependentName(CSSConstants.SUFFIX_MANDATORY);

    getAppTextArea().getTextArea().addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        if (!isInvalid()) {
          if (getValue() == null
              && getAppTextArea().getText().trim().length() > 0) {
            setValue(new AValue(getModel().getObject().getId(),
                getTemplateAttribute().getOaId(), getAppTextArea().getText()
                    .trim(), Main.language));
            getValue().setRank(getRank());
          } else {
            if (getAppTextArea().getText().trim().length() > 0) {
              if (getValue().getLangId() != Main.language) {
                AValue value = new AValue(getModel().getObject().getId(),
                    getTemplateAttribute().getOaId(), getAppTextArea().getText()
                    .trim(), Main.language);
                value.setId(getValue().getId());
                value.setRank(getRank());
                setValue(value);
              } else
                getValue().setValueString(getAppTextArea().getText().trim());
            } 
          }
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
    
    add(getAppTextArea());
  }

  @Override
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return getAppTextArea().getTextArea().addBlurHandler(handler);
  }

  @Override
  public boolean isEdited() {
    return !getOldValue().equals(getAppTextArea().getTextArea().getText().trim());
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null ||
        (getAppTextArea().getText().trim().length() == 0
        && getTemplateAttribute().isMandatory());
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);
    if (value != null && value.getRank() == getRank() 
        && value.getValueString() != null) {
      getAppTextArea().setText(value.getValueString());
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
    getAppTextArea().setText("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateTextAreaWidget(getObjectTemplate(), getTemplateAttribute(), getRelId(), rank,
        getWidgetToHighlight(), false);
  }

  @Override
  public void setTabIndex(int index) {
    getAppTextArea().setTabIndex(index);
  }

  @Override
  public void setFocus() {
    getAppTextArea().getTextArea().getElement().focus();
  }

  // getters and setters
  
  /**
   * @return the appTextArea
   */
  public TextAreaPanel getAppTextArea() {
    return appTextArea;
  }

  /**
   * @param appTextArea the appTextArea to set
   */
  public void setAppTextArea(TextAreaPanel appTextArea) {
    this.appTextArea = appTextArea;
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
