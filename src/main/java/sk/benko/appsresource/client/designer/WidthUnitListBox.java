package sk.benko.appsresource.client.designer;

import com.google.gwt.user.client.ui.ListBox;

public class WidthUnitListBox extends ListBox {
  public static String WIDTH_UNITPX = "px";
  public static String WIDTH_UNITPERCENT = "%";
  public static String WIDTH_UNITEM = "em";
  
  public WidthUnitListBox() {
    addItem(WIDTH_UNITPX);
    addItem(WIDTH_UNITPERCENT);
    addItem(WIDTH_UNITEM);
  }
  
  public void setText(String text) {
    for (int i = 0; i < getItemCount(); i++) {
      if (getItemText(i).equals(text)) {
        setSelectedIndex(i);
        break;
      }
      
    }
  }
}
