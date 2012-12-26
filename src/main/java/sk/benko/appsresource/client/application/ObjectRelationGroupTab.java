package sk.benko.appsresource.client.application;

import sk.benko.appsresource.client.model.TemplateGroup;
import sk.benko.appsresource.client.model.TemplateRelation;

public class ObjectRelationGroupTab extends ObjectGroupTab {

  public ObjectRelationGroupTab(ObjectTemplate ot, TemplateRelation tr, 
      TemplateGroup tg) {
    super(ot, tg);

    setTg(tg);
    setTr(tr);
    setDisabled(true);
  }

  public String getTabName() {
    return getTr().getName() + " (" + getTg().getName() + ")";
  }
}
