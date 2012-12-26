package sk.benko.appsresource.client;

import java.util.Date;

import sk.benko.appsresource.client.layout.Main;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientUtils {
  public static int DEFAULT_LANG = 0;
  public static String NUMBERFORMAT = "#,###,###,##0.###";
  public static String INTFORMAT = "#,###,###,##0";
  public static String DECIMALSEPARATOR = LocaleInfo.getCurrentLocale()
      .getNumberConstants().decimalSeparator();
  public static String MINUSSIGN = LocaleInfo.getCurrentLocale()
      .getNumberConstants().minusSign();

  public static int AUTOSAVE_INTERVAL = 15000;

  public static String CSS_LOGO = "logo";
  public static String CSS_SEARCH = "search";
  public static String CSS_FILENAME = "filename";

  
  public static String CLOSE_CHAR = "x";

  public static String CSS_MANDATORY = "widget-mandatory";
  public static String CSS_DISABLED = "widget-disabled";
  public static String CSS_HIGHLIGHTED = "widget-highlighted";
  public static String CSS_ALIGN_RIGHT = "widget-align-right";
  public static String CSS_ALIGN_CENTER = "widget-align-center";
  public static String CSS_ITALIC = "widget-italic";
  public static String CSS_CLICKABLE = "widget-clickable";
  
  public static String CSS_NAVITEM_SELECTED = "navigation-item-selected";
    
  public static String CSS_BUTTON = "button";

  public static String CSS_USERDIALOGBOX = "user-dialog-box";
  public static String CSS_USERDIALOGBOX_TOP = "user-dialog-box-top";
  public static String CSS_USERDIALOGBOX_BOTTOM = "user-dialog-box-bottom";

  public static String CSS_OBJECTDIALOGBOX_CONTENT = "object-dialog-box-content";
  public static String CSS_OBJECTDIALOGBOX_BOTTOM = "object-dialog-box-bottom";
  public static String CSS_OBJECTDIALOGBOX_BUTTONS = "object-dialog-box-buttons";

  public static String CSS_IMPORTDIALOGBOX_CONTENT = "import-dialog-box-content";
  public static String CSS_IMPORTDIALOGBOX_BOTTOM = "import-dialog-box-bottom";
  public static String CSS_IMPORTDIALOGBOX_BUTTONS = "import-dialog-box-buttons";

  public static String CSS_DIALOG_BUTTON = "dialog-button";
  public static String CSS_DIALOG_BUTTONOK = "dialog-button-ok";
  public static String CSS_DIALOG_BUTTONCANCEL = "dialog-button-cancel";
  public static String CSS_DIALOG_LABEL_DISABLED = "dialog-label-disabled";
  public static String CSS_DIALOGBOX = "dialog-box";
  public static String CSS_DIALOGBOX_GLASS = "dialog-box-glass";
  public static String CSS_DIALOGBOX_HEADER = "dialog-box-header";
  public static String CSS_DIALOGBOX_X = "dialog-box-x";
  public static String CSS_DIALOGBOX_BODY = "dialog-box-body";
  public static String CSS_DIALOGBOX_BODYLEFT = "dialog-box-left";
  public static String CSS_DIALOGBOX_BODYRIGHT = "dialog-box-right";
  public static String CSS_DIALOGBOX_BODYBOTTOM = "dialog-box-bottom";
  public static String CSS_DIALOGBOX_BODYBUTTONS = "dialog-box-buttons";
  public static String CSS_DIALOGBOX_NAVIGATIONITEM = "dialog-box-navigation-item";
  public static String CSS_DIALOGBOX_NAVIGATIONITEM_SELECTED = "dialog-box-navigation-item-selected";
  public static String CSS_DIALOGBOX_LABEL = "dialog-box-label";

  public static String CSS_CHOOSEBOX = "choose-box";
  public static String CSS_CHOOSEBOX_GLASS = "choose-box-glass";
  public static String CSS_CHOOSEBOX_HEADER = "choose-box-header";
  public static String CSS_CHOOSEBOX_X = "choose-box-x";
  public static String CSS_CHOOSEBOX_BODY = "choose-box-body";

  public static String CSS_OBJECT_LABEL = "object-label";
  public static String CSS_OBJECT_LISTBOX = "object-listbox";
  public static String CSS_OBJECT_UNIT = "object-unit";
  public static String CSS_OBJECT_DATEPICKER = "object-datepicker";

  public static String CSS_OBJECTLIST = "object-list";
  public static String CSS_OBJECTLIST_TOOLBAR = "object-list-toolbar";

  public static String CSS_TOOLBAR_OUTERBOX = "toolbar-outer-box";
  public static String CSS_TOOLBAR_INNERBOX = "toolbar-inner-box";
  public static String CSS_TOOLBAR_INLINE = "toolbar-inline-block";
  
  public static String CSS_DROPDOWN_HEAD = "dropdown-head";
  public static String CSS_DROPDOWN_HEAD_LIST = "dropdown-head-list";
  public static String CSS_DROPDOWN_HEAD_LIST_HOVER = "dropdown-head-list-hover";
  public static String CSS_DROPDOWN_HEAD_DESIGNER = "dropdown-head-designer";
  public static String CSS_DROPDOWN_HEAD_DESIGNER_HOVER = "dropdown-head-designer-hover";
  public static String CSS_DROPDOWN_HEAD_MAIN = "dropdown-head-main";
  public static String CSS_DROPDOWN_HEAD_MAIN_HOVER = "dropdown-head-main-hover";
  public static String CSS_DROPDOWN_CAPTION = "dropdown-caption";
  public static String CSS_DROPDOWN_BUTTON = "dropdown-button";
  public static String CSS_DROPDOWN_LIST = "dropdown-list";
  public static String CSS_DROPDOWN_CONTENT = "dropdown-content";
  public static String CSS_DROPDOWN_ITEM = "dropdown-item";
  public static String CSS_DROPDOWN_ITEMHIGHLIGHT = "dropdown-item-highlight";

  public static String CSS_NAVIGATION_BOX = "navigation-box";
  public static String CSS_NAVIGATION_IMAGE = "navigation-image";

  public static String CSS_TOOLTIP = "tooltip";
  public static String CSS_TOOLTIP_HIDE = "tooltip-hide";

  public static boolean getFlag(int flag, int flags) { 
    return ((flags & (1<<flag)) > 0); 
  }
  
  public static int setFlag(int flag, int flags) {
    int result = flags;
    result |= (1<<flag);
    return result;
  }

  public static int unsetFlag(int flag, int flags) {
    int result = flags;
    result &= ~(1<<flag);
    return result;
  }

  public static void showWaitCursor() {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
  }
 
  public static void showDefaultCursor() {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
  }
  
  public static String parseInt(String text) {
    String result = "";
    for (int i = 0; i < text.length(); i++) {
      if (Character.isDigit(text.charAt(i))
          || MINUSSIGN.equals(""+text.charAt(i)))
        result = result + text.charAt(i); 
    }
    
    return result;
  }
  
  public static String parseDouble(String text) {
    String result = "";
    for (int i = 0; i < text.length(); i++) {
      if (Character.isDigit(text.charAt(i)) 
          || DECIMALSEPARATOR.equals(""+text.charAt(i))
          || MINUSSIGN.equals(""+text.charAt(i)))
        result = result + text.charAt(i); 
    }
    
    return result;
  }

  public static String getTimeFrame(Date lastUpdatedAt, String userName) {
    long diff = new Date().getTime() - lastUpdatedAt.getTime();
    //long diffSeconds = diff / 1000;
    long diffMinutes = diff / (60 * 1000);
    long diffHours = diff / (60 * 60 * 1000);
    long diffDays = diff / (24 * 60 * 60 * 1000);
    
    if (diffDays > 1)
      return Main.messages.lastUpdateDays(""+diffDays, userName);
    else if (diffHours > 1)
      return Main.messages.lastUpdateHours(""+diffHours, userName);
    else if (diffMinutes > 1)
      return Main.messages.lastUpdateMinutes(""+diffMinutes, userName);
    else
      return Main.messages.lastUpdateSeconds(userName);
  }
  
  public static NumberFormat getNumberFormat() {
    return NumberFormat.getFormat(ClientUtils.NUMBERFORMAT);
  }

}
