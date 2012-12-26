package sk.benko.appsresource.client.model;

import java.util.Comparator;

public class TemplateRelationComparator implements Comparator<TemplateRelation> {

  @Override
  public int compare(TemplateRelation tr1, TemplateRelation tr2) {
    if (tr1.getRank() == tr2.getRank())
      return tr1.getSubrank() - tr2.getSubrank();
    else
      return tr1.getRank() - tr2.getRank();
  }
}