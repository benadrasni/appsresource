package sk.benko.appsresource.client.ui.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.application.ObjectTemplate;
import sk.benko.appsresource.client.model.AValue;
import sk.benko.appsresource.client.model.TemplateAttribute;
import sk.benko.appsresource.client.ui.CalendarListener;

import java.util.Date;

public class TemplateDatePickerWidget extends TemplateWidget implements CalendarListener<Calendar>, HasChangeHandlers {
  private DatePicker datePicker;
  private Date oldValue;

  public TemplateDatePickerWidget(ObjectTemplate objectTemplate, TemplateAttribute ta,
                                  int relId, int rank, Widget widgetToHighlight, boolean isDisabled, boolean isTime) {
    super(objectTemplate, ta, relId, rank, widgetToHighlight, isDisabled);

    setDatePicker(new DatePicker(isDisabled(), ta.getShared1(), ta.getShared2()));
    getDatePicker().setTimeVisible(isTime);

    getDatePicker().setWidth(ta.getWidth() + ta.getWidthUnit());
    getDatePicker().getChoiceButton().setTabIndex(ta.getTabIndex());
    if (isDisabled() || !getModel().getAppu().isWrite())
      getDatePicker().addStyleName(ClientUtils.CSS_DISABLED);
    if (ta.isMandatory())
      getDatePicker().getSelectedValue().addStyleName(ClientUtils.CSS_MANDATORY);

    addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        if (getModel().getObject() != null && !isInvalid()) {
          if (isEdited()) {
            getModel().updateValue(getValue());
          }
        } else {
          getDatePicker().setDate(getValue().getValueDate());
        }
      }
    });

    add(getDatePicker());
    getDatePicker().getCalendar().addCalendarListener(this);
  }

  @Override
  public boolean isEdited() {
    return !isInvalid()
        && ((getOldValue() != null && (getValue() == null || getValue()
        .getValueDate() == null))
        || (getOldValue() != null && getValue() != null
        && getValue().getValueDate() != null && !getOldValue().equals(
        getValue().getValueDate())) || (getOldValue() == null
        && getValue() != null && getValue().getValueDate() != null));
  }

  @Override
  public boolean isInvalid() {
    return getModel().getObject() == null
        || (getDatePicker().getSelectedValue().getText().trim().length() == 0
        && getTemplateAttribute().isMandatory());
  }

  @Override
  public void initializeValue(AValue value) {
    super.initializeValue(value);

    getDatePicker().cleanSelection();
    if (value != null && value.getRank() == getRank()
        && value.getValueDate() != null) {
      if (getDatePicker().isTimeVisible())
        getDatePicker().setDate(value.getValueTimestamp());
      else
        getDatePicker().setDate(value.getValueDate());
      setValue(value);
      if (value.getId() > 0)
        setOldValue(value.getValueDate());
    } else {
      setOldValue(null);
      getDatePicker().setDate(null);
      setValue(null);
    }

  }

  @Override
  public void clearValue() {
    super.clearValue();
    getDatePicker().getSelectedValue().setText("");
  }

  @Override
  public TemplateWidget copy(int rank) {
    return new TemplateDatePickerWidget(getObjectTemplate(), getTemplateAttribute(), getRelId(), rank,
        getWidgetToHighlight(), false, getDatePicker().isTimeVisible());
  }

  @Override
  public void setTabIndex(int index) {
    getDatePicker().getChoiceButton().setTabIndex(index);
  }

  @Override
  public void setFocus() {
    getDatePicker().getChoiceButton().getElement().focus();
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  @Override
  public void onChange(Calendar sender, Date oldValue) {
    if (sender.getDate() != null) {
      if (getValue() == null) {
        setValue(new AValue(getModel().getObject().getId(),
            getTemplateAttribute().getOaId(), sender.getDate(),
            sender.getDate()));
        getValue().setRank(getRank());
      } else {
        getValue().setValueDate(sender.getDate());
        getValue().setValueTimestamp(sender.getDate());
      }
    } else {
      if (!getTemplateAttribute().isMandatory()) {
        if (getValue() != null)
          getValue().setValueDate(null);
      }
    }

    fireEvent(new ChangeEvent() {
    });
    getObjectTemplate().fireEvent(new
                                      ChangeEvent() {
                                      });
  }

  @Override
  public void onCancel(Calendar sender) {
  }

  // getters and setters

  /**
   * @return the datePicker
   */
  public DatePicker getDatePicker() {
    return datePicker;
  }

  /**
   * @param datePicker the datePicker to set
   */
  public void setDatePicker(DatePicker datePicker) {
    this.datePicker = datePicker;
  }

  /**
   * @return the oldValue
   */
  public Date getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set
   */
  public void setOldValue(Date oldValue) {
    this.oldValue = oldValue;
  }

}
