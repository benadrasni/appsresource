package sk.benko.appsresource.server;

import java.util.Comparator;

import sk.benko.appsresource.server.StoreDB.AObject;

public class AObjectComparator implements Comparator<AObject> {

  @Override
  public int compare(AObject object1, AObject object2) {
    return object1.getLeaf().compareTo(object2.getLeaf());
  }
}