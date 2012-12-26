package sk.benko.appsresource.client.ui;

import sk.benko.appsresource.client.ui.widget.Calendar;

import java.util.Date;

/**
 * This is a calendar listener.
 *
 */
public interface CalendarListener<T extends Calendar> {
    /**
     * This method is invoked when a user chooses a date.
     *
     * @param sender is a calendar which sent the event.
     * @param oldValue is an old date value.
     */
    void onChange(T sender, Date oldValue);

    /**
     * This method is invoked on cancel.
     *
     * @param sender is a calendar which sent the event.
     */
    void onCancel(T sender);
}
