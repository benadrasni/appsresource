package sk.benko.appsresource.client;

import com.google.gwt.i18n.client.Messages;

public interface AppsresourceMessages extends Messages {
  @DefaultMessage("''{0}'' is not a valid symbol.")
  String invalidSymbol(String symbol);

  @DefaultMessage("Last edit was made {0} days ago by {1}")
  String lastUpdateDays(String days, String user);

  @DefaultMessage("Last edit was made {0} hours ago by {1}")
  String lastUpdateHours(String hours, String user);

  @DefaultMessage("Last edit was made {0} minutes ago by {1}")
  String lastUpdateMinutes(String minutes, String user);

  @DefaultMessage("Last edit was made a few seconds ago by {0}")
  String lastUpdateSeconds(String user);

  @DefaultMessage("Drag templates here")
  String dragTemplates();

  @DefaultMessage("Drag filterAttributes here")
  String dragAttributes();
  
  @DefaultMessage("{0} objects was imported")
  String objectsImported(int count);

  @DefaultMessage("{0} object was removed")
  String objectsRemoved(int count);

  @DefaultMessage("The file is invalid")
  String noValidRows();

  @DefaultMessage("No file is chosen")
  String noFileChosen();
}
