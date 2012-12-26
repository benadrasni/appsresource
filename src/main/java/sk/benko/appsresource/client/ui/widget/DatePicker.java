package sk.benko.appsresource.client.ui.widget;

import java.util.Date;

import sk.benko.appsresource.client.ClientUtils;
import sk.benko.appsresource.client.ui.CalendarListener;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * This is a date picker widget.
 * 
 */
public class DatePicker extends TextButtonPanel implements
    CalendarListener<Calendar>, HasChangeHandlers {
  /** calendar popup panel */
  private PopupPanel calendarPanel;
  /** calendar widget */
  private Calendar calendar;
  /** open calendar event handler */
  private ClickHandler openCalendarClickHandler;
  /** the date */
  private Date date;
  /** time visibility flag */
  private boolean timeVisible = false;
  /** date and time format for the widget */
  private String format;

  /**
   * Creates an instance of this class and does nothing else.
   */
  public DatePicker(boolean isDisabled, int startYear, int endYear) {
    this(null, isDisabled, startYear, endYear);
  }

  /**
   * Creates an instance of this class.
   * 
   * @param initialDate
   *          is an initial date.
   */
  public DatePicker(Date initialDate, boolean isDisabled, int startYear, 
      int endYear) {
    super();
    this.date = initialDate;
    getCalendar().setShowTime(isTimeVisible());
    getCalendar().setStartYear(startYear);
    getCalendar().setEndYear(endYear);
    addStyleName(ClientUtils.CSS_OBJECT_DATEPICKER);
    
    if (isDisabled) {
      getSelectedValue().addStyleName(ClientUtils.CSS_DISABLED);
      getChoiceButton().addStyleName(ClientUtils.CSS_DISABLED);
    }
  }

  /**
   * Getter for property 'timeVisible'.
   * 
   * @return Value for property 'timeVisible'.
   */
  public boolean isTimeVisible() {
    return timeVisible;
  }

  /**
   * Setter for property 'timeVisible'.
   * 
   * @param timeVisible
   *          Value to set for property 'timeVisible'.
   */
  public void setTimeVisible(boolean timeVisible) {
    this.timeVisible = timeVisible;
    if (getDate() != null)
      getSelectedValue().setText(getFormat().format(date));
    getCalendar().setShowTime(timeVisible);
  }

  /** {@inheritDoc} */
  @Override
  public void onChange(Calendar sender, Date oldValue) {
    getCalendarPanel().hide();
    Date date = getCalendar().getDate();
    if (date != null)
      getSelectedValue().setText(getFormat().format(date));
    else 
      getSelectedValue().setText("");
    this.date = date;
    fireEvent(new ChangeEvent() {});
  }

  /** {@inheritDoc} */
  @Override
  public void onCancel(Calendar sender) {
    getCalendarPanel().hide();
  }

  /** {@inheritDoc} */
  @Override
  public void onLoad() {
    getSelectedValue().setTabIndex(-1);
  }

  /** {@inheritDoc} */
  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addHandler(handler, ChangeEvent.getType());
  }

  /**
   * Getter for property 'date'.
   * 
   * @return Value for property 'date'.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets a date for this date picker.
   * 
   * @param date
   *          is a date to set.
   */
  public void setDate(Date date) {
    this.date = date;
    if (date != null)
      getSelectedValue().setText(getFormat().format(date));
    else
      getSelectedValue().setText("");
  }

  /**
   * Gets a textual representation of the date using format properties.
   * 
   * @return textual representation.
   */
  public String getTextualDate() {
    if (getDate() != null)
      return getFormat().format(getDate());
    else
      return "";
  }

  /**
   * Sets a format string for the displaying date and time.
   * <p/>
   * It overrides the format specified in the resource file. For more details
   * see docs for the <code>DateTimeFormat</code> class.
   * 
   * @param format
   *          is a format string.
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /** {@inheritDoc} */
  protected void prepareSelectedValue() {
    super.prepareSelectedValue();
    getSelectedValue().setReadOnly(true);
    Date date = getDate();
    if (date != null)
      getSelectedValue().setText(getFormat().format(date));
  }

  /** {@inheritDoc} */
  protected String getDefaultImageName() {
    return "calendar.png";
  }

  /**
   * This method adds different listeners to elements of the widget.
   */
  protected void addComponentListeners() {
    if (openCalendarClickHandler == null) {
      openCalendarClickHandler = new OpenCalendarClickHandler();

      ToggleButton calendarButton = getChoiceButton();
      calendarButton.addClickHandler(openCalendarClickHandler);
      TextBox box = getSelectedValue();
      box.addClickHandler(openCalendarClickHandler);
    }

    getCalendar().addCalendarListener(this);
  }

  /**
   * Getter for property 'calendar'.
   * 
   * @return Value for property 'calendar'.
   */
  protected Calendar getCalendar() {
    if (calendar == null) {
      calendar = new Calendar();
      if (getDate() != null)
        calendar.setDate(getDate());
      else
        calendar.setDate(new Date());
    }

    return calendar;
  }

  /**
   * Getter for property 'calendarPanel'.
   * 
   * @return Value for property 'calendarPanel'.
   */
  protected PopupPanel getCalendarPanel() {
    if (calendarPanel == null) {
      calendarPanel = new PopupPanel(true, true);
      calendarPanel.add(getCalendar());
    }
    return calendarPanel;
  }

  /**
   * Getter for property 'format'.
   * 
   * @return Value for property 'format'.
   */
  protected DateTimeFormat getFormat() {
    if (this.format != null)
      return DateTimeFormat.getFormat(this.format);

    DateTimeFormat format;
    if (isTimeVisible())
      format = DateTimeFormat.getFormat(Calendar.constants.dateTimeFormat());
    else
      format = DateTimeFormat.getFormat(Calendar.constants.dateFormat());
    return format;
  }

  /**
   * This is an open calendar evbent handler implementation.
   * 
   * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
   */
  protected class OpenCalendarClickHandler implements ClickHandler {
    /** {@inheritDoc} */
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == getSelectedValue() && isChoiceButtonVisible())
        return;

      if (getDate() != null) 
        getCalendar().setDate(getDate());
      else 
        getCalendar().setDate(new Date());
      getCalendar().setSelectedDate(CalendarUtil.copyDate(getCalendar().getDate()));

      getCalendar().display();
      getCalendarPanel().show();

      int left = getAbsoluteLeft() + getSelectedValue().getOffsetWidth();
      if (left + getCalendar().getOffsetWidth() > Window.getClientWidth())
        left -= getCalendar().getOffsetWidth();
      int top = Math.min(getAbsoluteTop(), Window.getClientHeight()-270);
      
      getCalendarPanel().setPopupPosition(left, top);
      getChoiceButton().setDown(false);
    }
  }
}
