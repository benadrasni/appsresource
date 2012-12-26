package sk.benko.appsresource.client;

import java.util.ArrayList;

import sk.benko.appsresource.client.model.TreeLevel;

public class TreeItemData {
  private static int KEY_LENGTH = 200;
  public static String KEY_SEPARATOR = "|";

  private boolean isLoaded;
  private ArrayList<TreeLevel> path;

  public TreeItemData() {
    setPath(new ArrayList<TreeLevel>());
  }

  public TreeItemData(ArrayList<TreeLevel> path, TreeLevel item) {
    setPath(new ArrayList<TreeLevel>(path));
    if (item != null) getPath().add(item);
  }
  
  public boolean isLoaded() {
    return isLoaded;
  }
  
  public void setLoaded(boolean isLoaded) {
    this.isLoaded = isLoaded;
  }
  
  public ArrayList<TreeLevel> getPath() {
    return path;
  }
  
  public void setPath(ArrayList<TreeLevel> path) {
    this.path = path;
  }
  
  public String getKey() {
    StringBuilder key = new StringBuilder(KEY_LENGTH);
    for (TreeLevel tl : getPath()) {
      if (tl.getValueString() != null)
        key.append(tl.getValueString());
      else if (tl.getValueDate() != null)
        key.append(tl.getValueDate());
      else if (tl.getValueDouble() != null)
        key.append(tl.getValueDouble());
      key.append(KEY_SEPARATOR);
    }
    return key.toString();
  }
}
