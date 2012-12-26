package sk.benko.appsresource.client.model;

import java.util.Comparator;

public class AValueComparator implements Comparator<AValue> {

  @Override
  public int compare(AValue value1, AValue value2) {
    if (value1.getRank() == value2.getRank())
      return value1.getLangId() - value2.getLangId();
    else
      return value1.getRank() - value2.getRank();
  }
}