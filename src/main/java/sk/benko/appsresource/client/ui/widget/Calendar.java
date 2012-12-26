package sk.benko.appsresource.client.ui.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import sk.benko.appsresource.client.CSSConstants;
import sk.benko.appsresource.client.DropDownBox;
import sk.benko.appsresource.client.DropDownObject;
import sk.benko.appsresource.client.DropDownObjectImpl;
import sk.benko.appsresource.client.layout.Main;
import sk.benko.appsresource.client.ui.CalendarListener;
import sk.benko.appsresource.client.ui.resources.CalendarConstants;
import sk.benko.appsresource.client.util.DateHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * This is a calendar widget that shows the calendar panel.
 * 
 */
public class Calendar extends SimplePanel implements ChangeHandler {
  /** calendar constants */
  static CalendarConstants constants = (CalendarConstants) GWT
      .create(CalendarConstants.class);

  /** short week day names */
  public static final String[] SHORT_DAY_NAMES = { constants.sun(),
      constants.mon(), constants.tue(), constants.wed(), constants.thu(),
      constants.fri(), constants.sat() };

  /** months names */
  public static final String[] MONTHS = { constants.january(),
      constants.february(), constants.march(), constants.april(),
      constants.may(), constants.june(), constants.july(), constants.august(),
      constants.september(), constants.october(), constants.november(),
      constants.december() };

  /** layout flex table */
  private FlexTable layout;
  /** days flex table */
  private FlexTable daysTable;
  /** clear date button */
  private ToggleButton clearDateButton;
  /** date choice handler */
  private ClickHandler dateChoiceHandler;
  /** a set of calendar listeners */
  private Set<CalendarListener<Calendar>> calendarListeners;

  /** a dropdown for years */
  private DropDownBox ddbYear;
  /** a dropdown for months */
  private DropDownBox ddbMonth;
  /** a dropdown for am/pm */
  private DropDownBox ddbAMPM;
  /** a dropdown for hours */
  private DropDownBox ddbHour;
  /** a dropdown for minutes */
  private DropDownBox ddbMinute;
  /** a dropdown for seconds */
  private DropDownBox ddbSecond;

  
  /** the date */
  private Date date;
  /** the selected date */
  private Date selectedDate;
  /** a flag of week numbers showing */
  private boolean showWeeksColumn = true;
  /** a flag of time showing */
  private boolean showTime = false;
  /** start year */
  private int startYear;
  /** end year */
  private int endYear;
  
  
  public Calendar() {
    this.addDomHandler(this, ChangeEvent.getType());
  }

  /**
   * Setter for property 'showWeeksColumn'.
   * 
   * @param showWeeksColumn
   *          Value to set for property 'showWeeksColumn'.
   */
  public void setShowWeeksColumn(boolean showWeeksColumn) {
    this.showWeeksColumn = showWeeksColumn;
  }

  /**
   * Setter for property 'showTime'.
   * 
   * @param showTime
   *          Value to set for property 'showTime'.
   */
  public void setShowTime(boolean showTime) {
    this.showTime = showTime;
  }

  /**
   * Getter for property 'showWeeksColumn'.
   * 
   * @return Value for property 'showWeeksColumn'.
   */
  public boolean isShowWeeksColumn() {
    return showWeeksColumn;
  }

  /**
   * Getter for property 'showTime'.
   * 
   * @return Value for property 'showTime'.
   */
  public boolean isShowTime() {
    return showTime;
  }

  /**
   * Setter for property 'date'.
   * 
   * @param date
   *          Value to set for property 'date'.
   */
  protected void setDate(Date date) {
    this.date = date;
  }

  /**
   * Getter for property 'date'.
   * 
   * @return Value for property 'date'.
   */
  protected Date getDate() {
    return date;
  }

  /**
   * Getter for property 'selectedDate'.
   *
   * @return Value for property 'selectedDate'.
   */
  public Date getSelectedDate() {
      return selectedDate;
  }

  /**
   * Getter for property 'startYear'.
   *
   * @return Value for property 'startYear'.
   */
  public int getStartYear() {
    return startYear;
  }

  /**
   * Setter for property 'startYear'.
   *
   * @param startYear Value to set for property 'startYear'.
   */
  public void setStartYear(int startYear) {
    this.startYear = startYear;
  }

  /**
   * Getter for property 'endYear'.
   *
   * @return Value for property 'endYear'.
   */
  public int getEndYear() {
    return endYear;
  }

  /**
   * Setter for property 'endYear'.
   *
   * @param endYear Value to set for property 'endYear'.
   */
  public void setEndYear(int endYear) {
    this.endYear = endYear;
  }

  /**
   * Setter for property 'selectedDate'.
   *
   * @param selectedDate Value to set for property 'selectedDate'.
   */
  public void setSelectedDate(Date selectedDate) {
      this.selectedDate = selectedDate;
  }
  
  public void display() {
    setStyleName(CSSConstants.CSS_CALENDAR);

    cleanLayout();
    prepareLayout();
    add(getLayout());
  }

  /**
   * This method adds a calendar listener.
   * 
   * @param listener
   *          a listener instance.
   */
  public void addCalendarListener(CalendarListener<Calendar> listener) {
    getCalendarListeners().add(listener);
  }

  /**
   * This method removes a calendar listener.
   * 
   * @param listener
   *          a listener instance.
   */
  public void removeCalendarListener(CalendarListener<Calendar> listener) {
    getCalendarListeners().remove(listener);
  }

  /**
   * This method prepares the layout.
   */
  protected void prepareLayout() {
    prepareHeader();
    prepareDays();
    prepareTime();
  }

  /**
   * This method renders the header.
   */
  protected void prepareHeader() {
    DateHelper dateHelper = new DateHelper(getSelectedDate());

    getDdbYear().setSelection(new DropDownObjectImpl(dateHelper.getYear(), 
        ""+dateHelper.getYear()));

    getDdbMonth().setSelection(new DropDownObjectImpl(dateHelper.getMonth(), 
        MONTHS[dateHelper.getMonth()]));
    
    FlexTable layout = getLayout();
    FlexTable.FlexCellFormatter formatter = layout.getFlexCellFormatter();
    
    layout.setWidget(0, 0, getDdbMonth());
    formatter.setStyleName(0, 0, CSSConstants.CSS_CALENDAR_HEADERCELL);
    layout.setText(0, 1, dateHelper.getDay()+",");
    formatter.setStyleName(0, 1, CSSConstants.CSS_CALENDAR_HEADERCELL);
    layout.setWidget(0, 2, getDdbYear());
    formatter.setStyleName(0, 2, CSSConstants.CSS_CALENDAR_HEADERCELL);

    layout.setWidget(0, 3, getClearDateButton());
    formatter.setStyleName(0, 3, CSSConstants.CSS_CALENDAR_CLEARCELL);
    formatter.setColSpan(0, 3, 2);
  }

  /**
   * This method renders the days section.
   */
  protected void prepareDays() {
    int startWeekDay = Integer.valueOf(constants.firstDayOfWeek());

    FlexTable daysTable = getDaysTable();
    FlexTable layout = getLayout();

    layout.setWidget(1, 0, daysTable);
    layout.getFlexCellFormatter().setColSpan(1, 0, 5);

    DateHelper dateHelper = new DateHelper(getSelectedDate());
    Date firstDay = new DateHelper(dateHelper.getFirstDayOfMonth())
        .getFirstDayOfWeek();
    Date lastDay = new DateHelper(dateHelper.getLastDayOfMonth())
        .getLastDayOfWeek();

    int startWeek = new DateHelper(firstDay).getWeekNumber();
    if (startWeek > 51)
      startWeek = 1;
    int endWeek = new DateHelper(lastDay).getWeekNumber();
    if (endWeek == 1)
      endWeek = new DateHelper(dateHelper.getLastDayOfMonth()).getWeekNumber();

    int startColumn = 0;
    if (isShowWeeksColumn()) {
      daysTable.setText(0, 0, "");
      startColumn++;
    }

    FlexTable.FlexCellFormatter formatter = daysTable.getFlexCellFormatter();
    formatter.setStyleName(0, 0, CSSConstants.CSS_CALENDAR_DAYNAMECELL);
    for (int i = startColumn; i < startColumn + SHORT_DAY_NAMES.length; i++) {
      int index = i - startColumn + startWeekDay;
      if (index > 6)
        index = index - 7;
      daysTable.setText(0, i, SHORT_DAY_NAMES[index]);
      formatter.setStyleName(0, i, CSSConstants.CSS_CALENDAR_DAYNAMECELL);
    }

    DateHelper helper = new DateHelper(firstDay);
    DateHelper todayHelper = new DateHelper(new Date());
    for (int i = startWeek; i <= endWeek; i++) {
      int row = i - startWeek + 1;
      if (isShowWeeksColumn()) {
        daysTable.setText(row, 0, String.valueOf(i));
        formatter.setStyleName(row, 0, CSSConstants.CSS_CALENDAR_WEEKNUMBERCELL);
      }

      for (int j = startColumn; j < startColumn + SHORT_DAY_NAMES.length; j++) {
        daysTable.setText(row, j, String.valueOf(helper.getDay()));

        if (dateHelper.getMonth() == helper.getMonth()
            && dateHelper.getYear() == helper.getYear())
          formatter.setStyleName(row, j, CSSConstants.CSS_CALENDAR_DAYCELL);
        else
          formatter.setStyleName(row, j, CSSConstants.CSS_CALENDAR_DISABLEDDAYCELL);

        if (helper.trim().equals(todayHelper.trim()))
          formatter.addStyleName(row, j, CSSConstants.CSS_CALENDAR_TODAYCELL);
        if (helper.trim().equals(dateHelper.trim()))
          formatter.addStyleName(row, j, CSSConstants.CSS_CALENDAR_SELECTEDDAYCELL);
        if (helper.isWeekEndDay())
          formatter.addStyleName(row, j, CSSConstants.CSS_CALENDAR_HOLIDAYCELL);

        helper.addDays(1);
      }
    }
  }

  /**
   * This method renders the time section.
   */
  protected void prepareTime() {
    if (!isShowTime())
      return;
    DateHelper dateHelper = new DateHelper(getSelectedDate());
    if ("12".equals(constants.hoursCircleBasis())) {
      int hours = dateHelper.getHours();
      if (hours >= 12 ) {
        getDdbAMPM().setSelection(new DropDownObjectImpl(1, "PM"));
        if (hours > 12) 
          hours -= 12;
      } else {
        getDdbAMPM().setSelection(new DropDownObjectImpl(0, "AM"));
        if (hours == 0)
          hours = 12;
      }
      getDdbHour().setSelection(new DropDownObjectImpl(hours, 
          ""+hours));
    }
    getDdbMinute().setSelection(new DropDownObjectImpl(dateHelper.getMinutes(), 
        ""+dateHelper.getMinutes()));
    getDdbSecond().setSelection(new DropDownObjectImpl(dateHelper.getSeconds(), 
        ""+dateHelper.getSeconds()));
    
    FlexTable layout = getLayout();
    FlexTable.FlexCellFormatter formatter = layout.getFlexCellFormatter();
    int index = 0;
    if ("12".equals(constants.hoursCircleBasis())) {
      layout.setWidget(2, index, getDdbAMPM());
      formatter.setStyleName(2, index++, CSSConstants.CSS_CALENDAR_TIMECELL);
      layout.setWidget(2, index, getDdbHour());
      formatter.setStyleName(2, index++, CSSConstants.CSS_CALENDAR_TIMECELL);
    } else {
      layout.setWidget(2, index, getDdbHour());
      formatter.setColSpan(2, index, 2);
      formatter.setStyleName(2, index++, CSSConstants.CSS_CALENDAR_TIMECELL);
    }
    layout.setWidget(2, index, getDdbMinute());
    formatter.setStyleName(2, index++, CSSConstants.CSS_CALENDAR_TIMECELL);
    layout.setWidget(2, index, getDdbSecond());
    formatter.setStyleName(2, index, CSSConstants.CSS_CALENDAR_TIMECELL);
    formatter.setColSpan(2, index, 2);    
  }

  /**
   * This method gets the adte string for the header.
   * 
   * @param date
   *          is a date to show.
   * @return a string value.
   */
  protected String getDateString(Date date) {
    if (date == null)
      return "";

    DateHelper dateHelper = new DateHelper(getDate());
    return MONTHS[dateHelper.getMonth()] + " " + dateHelper.getDay() + ", "
        + dateHelper.getYear();
  }

  /**
   * This method clean the layout.
   */
  protected void cleanLayout() {
    if (layout != null) {
      remove(getLayout());
      layout = null;
      daysTable = null;
    }
  }

  /**
   * Getter for property 'clearDateButton'.
   * 
   * @return Value for property 'clearDateButton'.
   */
  public ToggleButton getClearDateButton() {
    if (clearDateButton == null) {
      clearDateButton = new ToggleButton();
      clearDateButton.setText(Main.constants.clearDate());
      clearDateButton.setStyleName(CSSConstants.CSS_CALENDAR_CLEARCELL);
      clearDateButton.addClickHandler(getDateChoiceHandler());
    }

    return clearDateButton;
  }
  
  /**
   * Getter for property 'daysTable'.
   * 
   * @return Value for property 'daysTable'.
   */
  public FlexTable getDaysTable() {
    if (daysTable == null) {
      daysTable = new FlexTable();
      daysTable.setStyleName(CSSConstants.CSS_CALENDAR_DAYSTABLE);
      daysTable.addClickHandler(getDateChoiceHandler());
    }

    return daysTable;
  }

  /**
   * Getter for property 'layout'.
   * 
   * @return Value for property 'layout'.
   */
  protected FlexTable getLayout() {
    if (layout == null) {
      layout = new FlexTable();
      layout.setStyleName(CSSConstants.CSS_CALENDAR_LAYOUT);
    }

    return layout;
  }

  /**
   * Getter for property 'dateChoiceHandler'.
   * 
   * @return Value for property 'dateChoiceHandler'.
   */
  protected ClickHandler getDateChoiceHandler() {
    if (dateChoiceHandler == null)
      dateChoiceHandler = new DateChoiceHandler(this);
    return dateChoiceHandler;
  }

  /**
   * Getter for property 'calendarListeners'.
   * 
   * @return Value for property 'calendarListeners'.
   */
  protected Set<CalendarListener<Calendar>> getCalendarListeners() {
    if (calendarListeners == null)
      calendarListeners = new HashSet<CalendarListener<Calendar>>();
    return calendarListeners;
  }

  @Override
  public void onChange(ChangeEvent event) {
    int hours = getDdbHour().getSelection().getId();
    
    if ("12".equals(constants.hoursCircleBasis())) {
      if (getDdbAMPM().getSelection().getId() == 1 && hours < 12) 
        hours += 12;
      else if (getDdbAMPM().getSelection().getId() == 0 && hours == 12) 
        hours = 0;
    }
    
    DateHelper dateHelper = new DateHelper(getSelectedDate());
    Date newDate = new DateHelper(getDdbYear().getSelection().getId(), 
        getDdbMonth().getSelection().getId(), dateHelper.getDay(), 
        hours, getDdbMinute().getSelection().getId(), 
        getDdbSecond().getSelection().getId()).getDate(); 
    setSelectedDate(newDate);
    display();
  }
  
  /**
   * This is a date choice listener.
   */
  protected class DateChoiceHandler implements ClickHandler {
    /** this calendar */
    private Calendar calendar;

    /**
     * Creates an instance of this class.
     * 
     * @param calendar
     *          is a calendar widget.
     */
    public DateChoiceHandler(Calendar calendar) {
      this.calendar = calendar;
    }

    public void onClick(ClickEvent clickEvent) {
      Date oldValue = getSelectedDate();

      if (clickEvent.getSource() == getClearDateButton()) {
        setDate(null);
      } else {
        FlexTable table = (FlexTable) clickEvent.getSource();
        HTMLTable.Cell td = table.getCellForEvent(clickEvent);
        int cell = td.getCellIndex();
        int row = td.getRowIndex();
  
        if (isShowWeeksColumn() && cell == 0 || row == 0)
          return;
  
        DateHelper dateHelper = new DateHelper(oldValue);
  
        int month = dateHelper.getMonth();
        int year = dateHelper.getYear();
        int day = Integer.parseInt(table.getText(row, cell));
  
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
  
        if (isShowTime()) {
          hours = dateHelper.getHours();
          minutes = dateHelper.getMinutes();
          seconds = dateHelper.getSeconds();
        }
  
        if (row == 1 && day > 22) {
          month--;
        } else if (row == table.getRowCount() - 1 && day < 7) {
          month++;
        }
  
        setDate(new DateHelper(year, month, day, hours, minutes, seconds)
            .getDate());
        display();
      }

      for (CalendarListener<Calendar> calendarListener : getCalendarListeners())
        calendarListener.onChange(getCalendar(), oldValue);
    }

    /**
     * Getter for property 'calendar'.
     * 
     * @return Value for property 'calendar'.
     */
    protected Calendar getCalendar() {
      return calendar;
    }
  }
  
  public void onUnload() {
    for (CalendarListener<Calendar> calendarListener : getCalendarListeners())
      calendarListener.onCancel(this);
  }
  
  /**
   * Getter for property 'ddbYear'.
   * 
   * @return Value for property 'ddbYear'.
   */
  public DropDownBox getDdbYear() {
    if (ddbYear == null) {
      ddbYear = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbYear.setWidth("100%");
      ArrayList<DropDownObject> yearItems = new ArrayList<DropDownObject>();
      for (int i = getStartYear(); i <= getEndYear(); i++) { 
        yearItems.add(new DropDownObjectImpl(i, ""+i));
      }
      ddbYear.setItems(yearItems);
      
    }
    return ddbYear;
  }

  /**
   * Getter for property 'ddbMonth'.
   * 
   * @return Value for property 'ddbMonth'.
   */
  public DropDownBox getDdbMonth() {
    if (ddbMonth == null) {
      ddbMonth = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbMonth.setWidth("100%");
      ArrayList<DropDownObject> monthItems = new ArrayList<DropDownObject>();
      int i = 0;
      for (String month : MONTHS) { 
        monthItems.add(new DropDownObjectImpl(i++, month));
      }
      ddbMonth.setItems(monthItems);

    }
    return ddbMonth;
  }
  
  /**
   * Getter for property 'ddbAMPM'.
   * 
   * @return Value for property 'ddbAMPM'.
   */
  public DropDownBox getDdbAMPM() {
    if (ddbAMPM == null) {
      ddbAMPM = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbAMPM.setWidth("100%");
      ArrayList<DropDownObject> ampmItems = new ArrayList<DropDownObject>();
      ampmItems.add(new DropDownObjectImpl(0, "AM"));
      ampmItems.add(new DropDownObjectImpl(1, "PM"));
      ddbAMPM.setItems(ampmItems);

    }
    return ddbAMPM;
  }
  
  /**
   * Getter for property 'ddbHour'.
   * 
   * @return Value for property 'ddbHour'.
   */
  public DropDownBox getDdbHour() {
    if (ddbHour == null) {
      ddbHour = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbHour.setWidth("100%");
      ArrayList<DropDownObject> hourItems = new ArrayList<DropDownObject>();
      boolean b12 = "12".equals(constants.hoursCircleBasis());
      for (int i = (b12 ? 1 : 0); i <= (b12 ? 12 : 23); i++) { 
        hourItems.add(new DropDownObjectImpl(i, i < 10 ? "0"+i : ""+i));
      }
      ddbHour.setItems(hourItems);
      
    }
    return ddbHour;
  }
  
  /**
   * Getter for property 'ddbMinute'.
   * 
   * @return Value for property 'ddbMinute'.
   */
  public DropDownBox getDdbMinute() {
    if (ddbMinute == null) {
      ddbMinute = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbMinute.setWidth("100%");
      ArrayList<DropDownObject> minuteItems = new ArrayList<DropDownObject>();
      for (int i = 0; i < 60; i++) { 
        minuteItems.add(new DropDownObjectImpl(i, i < 10 ? "0"+i : ""+i));
      }
      ddbMinute.setItems(minuteItems);
      
    }
    return ddbMinute;
  }
  
  /**
   * Getter for property 'ddbSecond'.
   * 
   * @return Value for property 'ddbSecond'.
   */
  public DropDownBox getDdbSecond() {
    if (ddbSecond == null) {
      ddbSecond = new DropDownBox(this, null, CSSConstants.SUFFIX_MAIN);
      ddbSecond.setWidth("100%");
      ArrayList<DropDownObject> secondItems = new ArrayList<DropDownObject>();
      for (int i = 0; i < 60; i++) { 
        secondItems.add(new DropDownObjectImpl(i, i < 10 ? "0"+i : ""+i));
      }
      ddbSecond.setItems(secondItems);
      
    }
    return ddbSecond;
  }

}
