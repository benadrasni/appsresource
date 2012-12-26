package sk.benko.appsresource.client.ui;

import sk.benko.appsresource.client.model.AObject;
import sk.benko.appsresource.client.ui.widget.ObjectsTree;

/**
 * This is a object listener.
 *
 */
public interface ObjectListener<T extends ObjectsTree> {
    /**
     * This method is invoked when a user chooses an object.
     *
     * @param sender is a objects tree which sent the event.
     * @param oldValue is an old object value.
     */
    void onChange(T sender, AObject oldValue);

    /**
     * This method is invoked on cancel.
     *
     * @param sender is a objects tree which sent the event.
     */
    void onCancel(T sender);
}
