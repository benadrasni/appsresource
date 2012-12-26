package sk.benko.appsresource.client.model;

import java.util.Comparator;

public class TemplateAttributeComparator implements Comparator<TemplateAttribute> {

  @Override
  public int compare(TemplateAttribute ta1, TemplateAttribute ta2) {
    return ta1.getName().compareTo(ta2.getName());
  }
}