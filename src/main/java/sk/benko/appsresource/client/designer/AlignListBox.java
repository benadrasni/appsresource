package sk.benko.appsresource.client.designer;

import com.google.gwt.user.client.ui.ListBox;

public class AlignListBox extends ListBox {
  public static final String ALIGN_LEFT = "left";
  public static final String ALIGN_RIGHT = "right";
  public static final String ALIGN_CENTER = "center";
  
  public AlignListBox() {
    addItem(ALIGN_LEFT);
    addItem(ALIGN_CENTER);
    addItem(ALIGN_RIGHT);
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
