package sk.benko.appsresource.server;

import java.util.Comparator;

import sk.benko.appsresource.server.StoreDB.TreeLevel;

public class TreeLevelComparator implements Comparator<TreeLevel> {

  @Override
  public int compare(TreeLevel level1, TreeLevel level2) {
    if (level1.getValueString() == null && level2.getValueString() == null) 
      return 0;
    else if (level1.getValueString() == null) return -1;
    else if (level2.getValueString() == null) return 1;
    else    
      return level1.getValueString().compareTo(level2.getValueString());
  }
}